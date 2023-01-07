package othello.faskinen;

import othello.faskinen.opengl.GL;

public class Faskinen {
	public Window window;
	public Shader geometryShader;
	public Shader lightingShader;
	public Shader environmentShader;
	public Shader tonemapShader;

	public int imageWidth = 1920;
	public int imageHeight = 1080;

	public GBuffer gbuffer;

	public Texture hdrTexture;
	public Framebuffer hdrFramebuffer;

	public Texture sdrTexture;
	public Framebuffer sdrFramebuffer;

	public Light[] lights = new Light[] {
		new Light(new Vec3(-1.0f, -1.0f, 1.0f), new Vec3(1.0f, 1.0f, 1.0f)),
	};
	public Environment environment = new Environment();

	public Model testModel;
	public Camera camera = new Camera();

	public Faskinen() {
		this.window = Window.create("context", this.imageWidth, this.imageHeight);
		this.window.makeContextCurrent();

		this.geometryShader = new Shader("geometry.vert", "geometry.frag");
		this.lightingShader = new Shader("quad.vert", "lighting.frag");
		this.environmentShader = new Shader("quad.vert", "environment.frag");
		this.tonemapShader = new Shader("quad.vert", "tonemap.frag");

		this.testModel = Model.read("models/model.bin");

		this.gbuffer = new GBuffer(this.imageWidth, this.imageHeight);

		this.sdrTexture = Texture.bgra8(this.imageWidth, this.imageHeight);
		this.sdrFramebuffer = new Framebuffer(this.imageWidth, this.imageHeight, new Texture[] { this.sdrTexture });

		this.hdrTexture = Texture.rgba8(this.imageWidth, this.imageHeight);
		this.hdrFramebuffer = new Framebuffer(this.imageWidth, this.imageHeight, new Texture[] { this.hdrTexture });
	}

	public void clear() {	
		this.gbuffer.clear();

		this.sdrFramebuffer.clear(0.0f, 0.0f, 0.0f, 1.0f);
		this.hdrFramebuffer.clear(0.0f, 0.0f, 0.0f, 0.0f);
	}

	public void renderModel(Model model) {
		float aspect = (float) this.imageWidth / (float) this.imageHeight;

		GL.Enable(GL.DEPTH_TEST);

		this.gbuffer.framebuffer.bind();

		for (Primitive primitive : model.primitives) {
			this.geometryShader.use();

			this.geometryShader.setVec3("baseColor", primitive.material.baseColor);

			this.geometryShader.setMat4("model", Mat4.identity());
			this.geometryShader.setMat4("viewProj", this.camera.viewProj(aspect));

			primitive.mesh.bind();
			this.geometryShader.drawElements(primitive.mesh.indexCount, Lib.NULLPTR);
			primitive.mesh.unbind();
		}

		this.gbuffer.framebuffer.unbind();

		GL.Disable(GL.DEPTH_TEST);

		GL.assertNoError();
	}

	public void light() {
		GL.Enable(GL.BLEND);
		GL.BlendFunc(GL.ONE, GL.ONE);

		this.hdrFramebuffer.bind();	

		for (int i = 0; i < this.lights.length; i++) {
			Light light = this.lights[i];

			this.lightingShader.use();

			this.lightingShader.setVec3("light_direction", light.direction.normalize());
			this.lightingShader.setVec3("light_color", light.color);

			this.lightingShader.setTexture("g_position", this.gbuffer.positionTexture);
			this.lightingShader.setTexture("g_normal", this.gbuffer.normalTexture);
			this.lightingShader.setTexture("g_baseColor", this.gbuffer.baseColorTexture);
			this.lightingShader.setTexture("g_depth", this.gbuffer.depthTexture);

			this.lightingShader.drawArrays(0, 6);
		}

		this.environmentShader.use();

		this.environmentShader.setVec3("ambient_color", this.environment.ambientColor);

		this.environmentShader.setTexture("g_position", this.gbuffer.positionTexture);
		this.environmentShader.setTexture("g_normal", this.gbuffer.normalTexture);
		this.environmentShader.setTexture("g_baseColor", this.gbuffer.baseColorTexture);
		this.environmentShader.setTexture("g_depth", this.gbuffer.depthTexture);

		this.environmentShader.drawArrays(0, 6);

		this.hdrFramebuffer.unbind();

		GL.Disable(GL.BLEND);
	}

	public void tonemap() {
		this.sdrFramebuffer.bind();

		this.tonemapShader.use();

		this.tonemapShader.setTexture("hdr", this.hdrTexture);
		this.tonemapShader.drawArrays(0, 6);

		this.sdrFramebuffer.unbind();
	}

	public byte[] imageBytes() {
		byte[] bytes = this.sdrTexture.read();
		GL.assertNoError();

		return bytes;
	}
}
