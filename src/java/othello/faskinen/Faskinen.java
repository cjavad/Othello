package othello.faskinen;

import javafx.util.Pair;
import othello.faskinen.opengl.GL;

import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.MemoryAddress;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.MemorySession;
import java.lang.invoke.MethodType;
import java.util.ArrayList;
import java.util.Map;

/**
 * This is FASKINEN.
 *
 * @author Faskinen
 */
public class Faskinen {	
	/* Shader programs */
	public Shader geometryShader;
	public Shader shadowShader;
	public Shader lightingShader;
	public Shader environmentShader;
	public Shader tonemapShader;

	/* Dímensions */
	public int imageWidth = 1920;
	public int imageHeight = 1080;
	public float supersampling = 2.0f;

	/* Buffers */
	public GBuffer gbuffer;

	public Texture hdrTexture;
	public Framebuffer hdrFramebuffer;

	public Bloom bloom;

	public Texture sdrTexture;
	public Framebuffer sdrFramebuffer;

	/* Scene */
	public Light[] lights;
	public Environment environment;
	public Camera camera = new Camera();

	/**
	 * Creates a new Faskinen instance.
	 *
	 * This creates a new window and a valid OpenGL context.
	 */
	public Faskinen(int width, int height) {
		GL.DebugMessageCallback(Lib.getJavaFuncPointer(
			Faskinen.class,
			"debugCallback",
			MethodType.methodType(
				void.class, 
				int.class, 
				int.class, 
				int.class, 
				int.class, 
				int.class, 
				MemoryAddress.class, 
				MemoryAddress.class
			),
			FunctionDescriptor.ofVoid(
				Lib.C_UINT32_T, 
				Lib.C_UINT32_T, 
				Lib.C_UINT32_T, 
				Lib.C_UINT32_T, 
				Lib.C_UINT32_T, 
				Lib.C_POINTER_T, 
				Lib.C_POINTER_T
			)
		));

		this.imageWidth = width;
		this.imageHeight = height;

		GL.Enable(GL.TEXTURE_CUBE_MAP_SEAMLESS);

		this.geometryShader = new Shader("geometry.vert", "geometry.frag");
		this.shadowShader = new Shader("shadow.vert");
		this.lightingShader = new Shader("quad.vert", "lighting.frag");
		this.environmentShader = new Shader("quad.vert", "environment.frag");
		this.tonemapShader = new Shader("quad.vert", "tonemap.frag");

		this.hdrTexture = Texture.rgba16f(this.supersampledWidth(), this.supersampledHeight());
		this.hdrFramebuffer = new Framebuffer(new Texture[] { this.hdrTexture });


		this.gbuffer = new GBuffer(this.hdrTexture);
		this.bloom = new Bloom(this.imageWidth, this.imageHeight);

		this.sdrTexture = Texture.sbgra8(this.imageWidth, this.imageHeight);
		this.sdrFramebuffer = new Framebuffer(new Texture[] { this.sdrTexture });	

		this.lights = new Light [] {		
			new Light(new Vec3(-1.0f, -2.0f, 0.5f), new Vec3(1.0f, 1.0f, 1.0f)),
		};
		this.environment = Environment.read("misc/sky.env");
	}

	/*
		typedef void APIENTRY funcname(GLenum source, GLenum type, GLuint id,
	   GLenum severity, GLsizei length, const GLchar* message, const void* userParam);
	 */
	public static void debugCallback(
			int source, int type, int id, int severity, int length, MemoryAddress message, MemoryAddress userParam
	) {
		System.out.println("GLError ::=\n\tSource="+source+"\n\tType="+type+"\n\tId="+id+"\n\tSeverity");
		System.out.println(Lib.strToJava(MemorySegment.ofAddress(message, length, MemorySession.global())));
	}

	/**
	 * Resizes the buffers to the given size.
	 * @param width The new width.
	 * @param height The new height.
	 */
	public void resize(int width, int height) {
		this.imageWidth = width;
		this.imageHeight = height;

		this.gbuffer.delete();

		this.hdrTexture.delete();
		this.hdrFramebuffer.delete();

		this.bloom.delete();

		this.sdrTexture.delete();
		this.sdrFramebuffer.delete();

		this.hdrTexture = Texture.rgba16f(this.supersampledWidth(), this.supersampledHeight());
		this.hdrFramebuffer = new Framebuffer(new Texture[] { this.hdrTexture });

		this.gbuffer = new GBuffer(this.hdrTexture);
		this.bloom = new Bloom(this.imageWidth, this.imageHeight);

		this.sdrTexture = Texture.sbgra8(this.imageWidth, this.imageHeight);
		this.sdrFramebuffer = new Framebuffer(new Texture[] { this.sdrTexture });	
	}

	/**
	 * Resizes buffers.
	 *
	 * Use this after changing the supersampling factor.
	 */
	public void resize() {
		this.resize(this.imageWidth, this.imageHeight);
	}

	/**
	 * Returns the width of the supersampled framebuffer.
	 * @return The width of the supersampled framebuffer.
	 */
	public int supersampledWidth() {
		return (int) (this.imageWidth * this.supersampling);
	}

	/**
	 * Returns the height of the supersampled framebuffer.
	 * @return The height of the supersampled framebuffer.
	 */
	public int supersampledHeight() {
		return (int) (this.imageHeight * this.supersampling);
	}

	/**
	 * Clears the gbuffer, hdr texture, sdr texture and shadow maps.
	 */
	public void clear() {	
		for (Light light : this.lights) {
			light.shadowFramebuffer.bind();
			GL.Clear(GL.DEPTH_BUFFER_BIT);
		}

		this.gbuffer.clear();
		this.sdrFramebuffer.clear(0.0f, 0.0f, 0.0f, 1.0f);
		this.hdrFramebuffer.clear(0.0f, 0.0f, 0.0f, 0.0f);
	}


	public void geometryPass(RenderStack stack) {
		GL.Enable(GL.DEPTH_TEST);
		GL.Viewport(0, 0, this.supersampledWidth(), this.supersampledHeight());
		this.gbuffer.bind();

		this.geometryShader.use();

		float aspect = (float) this.imageWidth / (float) this.imageHeight;
		this.geometryShader.setCamera(this.camera, aspect);

		for (Map.Entry<Model, ArrayList<Pair<Mat4, Integer>>> entry : stack.renderStack.entrySet()) {
			Model model = entry.getKey();
			for (Primitive primitive : model.primitives) {
				this.geometryShader.setVec3("baseColor", primitive.material.baseColor);
				this.geometryShader.setFloat("roughness", primitive.material.roughness);
				this.geometryShader.setFloat("metallic", primitive.material.metallic);
				this.geometryShader.setVec3("emissive", primitive.material.emissive);
				this.geometryShader.setFloat("reflectance", primitive.material.reflectance);

				if (primitive.material.baseColorTexture != -1) {
					Texture texture = model.textures[primitive.material.baseColorTexture];
					this.geometryShader.setTexture("baseColorMap", texture);
				} else {
					this.geometryShader.setTexture("baseColorMap", Texture.RGBA8WHITE);
				}

				if (primitive.material.metallicRoughnessTexture != -1) {
					Texture texture = model.textures[primitive.material.metallicRoughnessTexture];
					this.geometryShader.setTexture("metallicRoughnessMap", texture);
				} else {
					this.geometryShader.setTexture("metallicRoughnessMap", Texture.RGBA8WHITE);
				}

				if (primitive.material.normalTexture != -1) {
					Texture texture = model.textures[primitive.material.normalTexture];
					this.geometryShader.setTexture("normalMap", texture);
				} else {
					this.geometryShader.setTexture("normalMap", Texture.RGBA8NORMAL);
				}

				if (primitive.material.emissiveTexture != -1) {
					Texture texture = model.textures[primitive.material.emissiveTexture];
					this.geometryShader.setTexture("emissiveMap", texture);
				} else {
					this.geometryShader.setTexture("emissiveMap", Texture.RGBA8WHITE);
				}

				primitive.mesh.bind();

				for (Pair<Mat4, Integer> instance : entry.getValue()) {
					this.geometryShader.setMat4("model", instance.getKey());
					this.geometryShader.setUint("objectId", instance.getValue() + 1);
					this.geometryShader.drawElements(primitive.mesh.indexCount, 0);
				}
			}
		}
	}

	public void shadowPass(RenderStack stack) {
		GL.Enable(GL.DEPTH_TEST);
		GL.Viewport(0, 0, Light.SHADOWMAP_SIZE, Light.SHADOWMAP_SIZE);

		this.shadowShader.use();

		for (Light light : lights) {
			light.shadowFramebuffer.bind();
			this.shadowShader.setMat4("viewProj", light.viewProj());


			for (Map.Entry<Model, ArrayList<Pair<Mat4, Integer>>> entry : stack.renderStack.entrySet()) {
				Model model = entry.getKey();

				for (Primitive primitive : model.primitives) {
					primitive.mesh.bind();

					for (Pair<Mat4, Integer> instance : entry.getValue()) {
						this.shadowShader.setMat4("model", instance.getKey());
						this.shadowShader.drawElements(primitive.mesh.indexCount, 0);
					}

				}
			}
		}
	}

	/**
	 * Lights the scene.
	 */
	public void light() {
		GL.Enable(GL.BLEND);
		GL.BlendFunc(GL.SRC_ALPHA, GL.ONE);
		GL.Viewport(0, 0, this.supersampledWidth(), this.supersampledHeight());

		float aspect = (float) this.imageWidth / (float) this.imageHeight;

		this.hdrFramebuffer.bind();	

		for (int i = 0; i < this.lights.length; i++) {
			Light light = this.lights[i];

			this.lightingShader.use();

			this.lightingShader.setCamera(this.camera, aspect);
			this.lightingShader.setGBuffer(this.gbuffer);

			this.lightingShader.setVec3("lightDirection", light.direction.normalize());
			this.lightingShader.setVec3("lightColor", light.color);
			this.lightingShader.setFloat("lightIntensity", light.intensity);
			this.lightingShader.setFloat("lightSoftness", light.shadowSoftness);
			this.lightingShader.setFloat("lightFalloff", light.shadowFalloff);
			
			this.lightingShader.setTexture("shadowMap", light.shadowMap);
			this.lightingShader.setMat4("lightViewProj", light.viewProj());
			this.lightingShader.setFloat("lightSize", Light.SIZE);
			this.lightingShader.setFloat("lightDepth", Light.DEPTH);

			this.lightingShader.setInt("blockerSearchSamples", light.blockerSearchSamples);
			this.lightingShader.setInt("penumbraSearchSamples", light.penumbraSearchSamples);

			this.lightingShader.drawArrays(0, 6);
		}

		this.environmentShader.use();

		this.environmentShader.setCamera(this.camera, aspect);
		this.environmentShader.setGBuffer(this.gbuffer);

		this.environmentShader.setFloat("intensity", this.environment.intensity);
		this.environmentShader.setTextureCube("irradianceMap", this.environment.irradianceId);
		this.environmentShader.setTextureCube("indirectMap", this.environment.indirectId);
		this.environmentShader.setTextureCube("skyMap", this.environment.skyId);
		this.environmentShader.setTexture("integratedDFG", Texture.INTEGRATED_DFG);
		this.environmentShader.drawArrays(0, 6);

		this.hdrFramebuffer.unbind();

		GL.Disable(GL.BLEND);
	}

	/**
	 * Tone maps the HDR framebuffer to the SDR framebuffer.
	 */
	public void tonemap() {
		this.bloom.render(this.hdrFramebuffer, 2.0f, 1.0f);

		GL.Viewport(0, 0, this.imageWidth, this.imageHeight);

		this.sdrFramebuffer.bind();

		this.tonemapShader.use();

		this.tonemapShader.setTexture("hdr", this.hdrTexture);

		this.tonemapShader.drawArrays(0, 6);

		this.sdrFramebuffer.unbind();
	}

	/**
	 * Reads the current image from the SDR texture and returns it as a byte array.
	 * @return The current image as a byte array.
	 */
	public byte[] imageBytes() {
		byte[] bytes = this.sdrTexture.readBytes();
		GL.assertNoError();

		return bytes;
	}

	/**
	 * Get the id of the pixel at the given coordinates.
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 * @return The id of the pixel.
	 */
	public int getPixelId(int x, int y) {
		x = (int) (x * this.supersampling);
		y = (int) (y * this.supersampling);
		y = this.supersampledHeight() - y - 1;

		return this.gbuffer.getPixelId(x, y);
	}

	public void delete() {
		this.geometryShader.delete();
		this.shadowShader.delete();
		this.lightingShader.delete();
		this.environmentShader.delete();
		this.tonemapShader.delete();

		this.gbuffer.delete();

		this.hdrTexture.delete();
		this.hdrFramebuffer.delete();

		this.sdrTexture.delete();
		this.sdrFramebuffer.delete();

		this.environment.delete();

		for (Light light : this.lights) {
			light.delete();
		}
	}
}
