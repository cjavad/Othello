package othello.faskinen;

import java.lang.foreign.MemoryAddress;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.MemorySession;
import java.lang.foreign.ValueLayout;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import othello.faskinen.opengl.GL;
import othello.utils.ResourceLoader;

/**
 * A wrapper around an OpenGL shader.
 *
 * Shaders may ONLY be created after a valid OpenGL context has been created.
 */
public class Shader {
	public int programId;
	private int nextUnit = 0;

	private static Pattern INCLUDE_PATTERN = Pattern.compile("#include \"(.*)\"");
	private static String VERSION = "#version 450 core";

	private static String readShaderFile(String shaderName) {
		try {
			return ResourceLoader.getShaderResource(shaderName);
		} catch (Exception e) {
			throw new RuntimeException("Failed to read shader file: " + shaderName, e);
		}
	}

	private static String preprocessShader(String source, HashSet<String> included) {
		Matcher matcher = INCLUDE_PATTERN.matcher(source);
		StringBuffer sb = new StringBuffer();

		while (matcher.find()) {
			String includeShader = matcher.group(1);

			if (included.contains(includeShader)) {
				matcher.appendReplacement(sb, "");

				continue;
			}
			included.add(includeShader);

			String includeSource = readShaderFile(includeShader);
			includeSource = preprocessShader(includeSource, included);
			matcher.appendReplacement(sb, includeSource);
		}

		matcher.appendTail(sb);

		return sb.toString();
	}

	/**
	 * Creates a new shader.
	 * @param vertexPath The path to the vertex shader.
	 * @param fragmentPath The path to the fragment shader.
	 * @return The created shader.
	 *
	 * The shader will be preprocessed to include other shaders.
	 * If shaders have circular dependencies, the stack will overflow
	 */
	public Shader(String vertexPath, String fragmentPath) {
		String vertexSource = readShaderFile(vertexPath);
		String fragmentSource = readShaderFile(fragmentPath);

		vertexSource = preprocessShader(vertexSource, new HashSet<>());
		fragmentSource = preprocessShader(fragmentSource, new HashSet<>());

		vertexSource = VERSION + "\n" + vertexSource;
		fragmentSource = VERSION + "\n" + fragmentSource;

		MemorySegment vertexSources = MemorySession.openImplicit().allocate(8);
		MemorySegment fragmentSources = MemorySession.openImplicit().allocate(8);
		vertexSources.set(ValueLayout.ADDRESS, 0, Lib.javaToStr(vertexSource).address());
		fragmentSources.set(ValueLayout.ADDRESS, 0, Lib.javaToStr(fragmentSource).address());

		int vertexShader = GL.CreateShader(GL.VERTEX_SHADER);
		int fragmentShader = GL.CreateShader(GL.FRAGMENT_SHADER);

		GL.ShaderSource(vertexShader, 1, vertexSources.address(), Lib.NULLPTR);
		GL.ShaderSource(fragmentShader, 1, fragmentSources.address(), Lib.NULLPTR);

		GL.CompileShader(vertexShader);
		GL.CompileShader(fragmentShader);

		MemorySegment vertexStatus = MemorySession.openImplicit().allocate(4);
		GL.GetShaderiv(vertexShader, GL.COMPILE_STATUS, vertexStatus.address());

		if (vertexStatus.get(ValueLayout.JAVA_INT, 0) != GL.TRUE) {
			MemorySegment log = MemorySession.openImplicit().allocate(1024);
			GL.GetShaderInfoLog(vertexShader, 1024, Lib.NULLPTR, log.address());

			System.out.println(Lib.strToJava(log));
			throw new RuntimeException("Failed to compile vertex shader");
		}

		MemorySegment fragmentStatus = MemorySession.openImplicit().allocate(4);
		GL.GetShaderiv(fragmentShader, GL.COMPILE_STATUS, fragmentStatus.address());

		if (fragmentStatus.get(ValueLayout.JAVA_INT, 0) != GL.TRUE) {
			MemorySegment log = MemorySession.openImplicit().allocate(1024);
			GL.GetShaderInfoLog(fragmentShader, 1024, Lib.NULLPTR, log.address());

			System.out.println(Lib.strToJava(log));
			throw new RuntimeException("Failed to compile fragment shader");
		}

		this.programId = GL.CreateProgram();
		GL.AttachShader(this.programId, vertexShader);
		GL.AttachShader(this.programId, fragmentShader);

		GL.LinkProgram(this.programId);

		MemorySegment linkStatus = MemorySession.openImplicit().allocate(4);
		GL.GetProgramiv(this.programId, GL.LINK_STATUS, linkStatus.address());

		if (linkStatus.get(ValueLayout.JAVA_INT, 0) != GL.TRUE) {
			MemorySegment log = MemorySession.openImplicit().allocate(1024);
			MemorySegment length = MemorySession.openImplicit().allocate(4);
			GL.GetProgramInfoLog(this.programId, 1024, length.address(), log.address());

			System.out.println(Lib.strToJava(log));
			throw new RuntimeException("Failed to link shader");
		}

		GL.DeleteShader(vertexShader);
		GL.DeleteShader(fragmentShader);
		GL.assertNoError();
	}

	/**
	 * Creates a new shader.
	 * @param vertexPath The path to the vertex shader.
	 * @return The created shader.
	 *
	 * The shader will be preprocessed to include other shaders.
	 * If shaders have circular dependencies, the stack will overflow
	 */
	public Shader(String vertexPath) {
		String vertexSource = readShaderFile(vertexPath);

		vertexSource = preprocessShader(vertexSource, new HashSet<>());
		vertexSource = VERSION + "\n" + vertexSource;

		MemorySegment vertexSources = MemorySession.openImplicit().allocate(8);
		vertexSources.set(ValueLayout.ADDRESS, 0, Lib.javaToStr(vertexSource).address());

		int vertexShader = GL.CreateShader(GL.VERTEX_SHADER);

		GL.ShaderSource(vertexShader, 1, vertexSources.address(), Lib.NULLPTR);
		GL.CompileShader(vertexShader);

		MemorySegment vertexStatus = MemorySession.openImplicit().allocate(4);
		GL.GetShaderiv(vertexShader, GL.COMPILE_STATUS, vertexStatus.address());

		if (vertexStatus.get(ValueLayout.JAVA_INT, 0) != GL.TRUE) {
			MemorySegment log = MemorySession.openImplicit().allocate(1024);
			GL.GetShaderInfoLog(vertexShader, 1024, Lib.NULLPTR, log.address());

			System.out.println(Lib.strToJava(log));
			throw new RuntimeException("Failed to compile vertex shader");
		}

		this.programId = GL.CreateProgram();
		GL.AttachShader(this.programId, vertexShader);

		GL.LinkProgram(this.programId);

		MemorySegment linkStatus = MemorySession.openImplicit().allocate(4);
		GL.GetProgramiv(this.programId, GL.LINK_STATUS, linkStatus.address());

		if (linkStatus.get(ValueLayout.JAVA_INT, 0) != GL.TRUE) {
			MemorySegment log = MemorySession.openImplicit().allocate(1024);
			MemorySegment length = MemorySession.openImplicit().allocate(4);
			GL.GetProgramInfoLog(this.programId, 1024, length.address(), log.address());

			System.out.println(Lib.strToJava(log));
			throw new RuntimeException("Failed to link shader");
		}

		GL.DeleteShader(vertexShader);
		GL.assertNoError();
	}

	/**
	 * Uses this shader.
	 */
	public void use() {
		GL.UseProgram(this.programId);
		this.nextUnit = 0;
	}

	/**
	 * Gets the location of a vertex attribute.
	 */
	public int getVertexAttributeLocation(String name) {
		int location = GL.GetAttribLocation(this.programId, Lib.javaToStr(name).address());

		if (location == -1) {
			throw new RuntimeException("Failed to find vertex attribute: " + name);
		}

		return location;
	}

	/**
	 * Binds a vertex buffer to a vertex attribute.
	 * @param name The name of the vertex attribute.
	 * @param buffer The buffer to bind.
	 * @param offset The offset of the attribute in the buffer.
	 * @param stride The stride of the attribute in the buffer.
	 */
	public void bindVertexBuffer(String name, Buffer buffer, int offset, int stride) {
		int location = this.getVertexAttributeLocation(name);	

		GL.BindVertexBuffer(location, buffer.bufferId, offset, stride);
		GL.assertNoError();
	}

	/**
	 * Binds an index buffer.
	 * @param buffer The buffer to bind.
	 */
	public void bindIndexBuffer(Buffer buffer) {
		GL.BindBuffer(GL.ELEMENT_ARRAY_BUFFER, buffer.bufferId);
	}
	
	/**
	 * Draws the shader using glDrawElements.
	 * @param count The number of indices to draw.
	 * @param offset The offset of the indices to draw.
	 */
	public void drawElements(int count, long offset) {	
		GL.DrawElements(GL.TRIANGLES, count, GL.UNSIGNED_INT, offset);
	}

	/**
	 * Draws the shader using glDrawArrays.
	 * @param index The index of the first vertex to draw.
	 * @param count The number of vertices to draw.
	 */
	public void drawArrays(int index, int count) {
		GL.DrawArrays(GL.TRIANGLES, index, count);
	}

	/**
	 * Gets the location of a uniform.
	 * @param name The name of the uniform.
	 * @return The location of the uniform.
	 */
	public int getUniformLocation(String name) {
		return GL.GetUniformLocation(this.programId, Lib.javaToStr(name).address());
	}

	/**
	 * Sets an integer uniform.
	 * @param name The name of the uniform.
	 * @param value The value to set.
	 */
	public void setInt(String name, int value) {
		int location = this.getUniformLocation(name);
		if (location == -1) return;
		GL.Uniform1i(location, value);
	}

	/**
	 * Sets a unsigned integer uniform.
	 * @param name The name of the uniform.
	 * @param value The value to set.
	 */
	public void setUint(String name, int value) {
		int location = this.getUniformLocation(name);
		if (location == -1) return;
		GL.Uniform1ui(location, value);
	}

	/**
	 * Sets a float uniform.
	 * @param name The name of the uniform.
	 * @param value The value to set.
	 */
	public void setFloat(String name, float value) {
		int location = this.getUniformLocation(name);
		if (location == -1) return;
		GL.Uniform1f(location, value);
	}

	/**
	 * Sets a 3D vector uniform.
	 * @param name The name of the uniform.
	 * @param value The value to set.
	 */
	public void setVec3(String name, Vec3 value) {
		int location = this.getUniformLocation(name);
		if (location == -1) return;
		GL.Uniform3f(location, value.x, value.y, value.z);
	}

	/**
	 * Sets a 4D vector uniform.
	 * @param name The name of the uniform.
	 * @param value The value to set.
	 */
	public void setVec4(String name, Vec4 value) {
		int location = this.getUniformLocation(name);
		if (location == -1) return;
		GL.Uniform4f(location, value.x, value.y, value.z, value.w);
	}

	/**
	 * Sets a 4x4 matrix uniform.
	 * @param name The name of the uniform.
	 * @param value The value to set.
	 */
	public void setMat4(String name, Mat4 value) {
		int location = this.getUniformLocation(name);
		if (location == -1) return;

		MemorySegment mat = MemorySession.openImplicit().allocate(64);
		mat.set(ValueLayout.JAVA_FLOAT, 0, value.x.x);
		mat.set(ValueLayout.JAVA_FLOAT, 4, value.x.y);
		mat.set(ValueLayout.JAVA_FLOAT, 8, value.x.z);
		mat.set(ValueLayout.JAVA_FLOAT, 12, value.x.w);
		mat.set(ValueLayout.JAVA_FLOAT, 16, value.y.x);
		mat.set(ValueLayout.JAVA_FLOAT, 20, value.y.y);
		mat.set(ValueLayout.JAVA_FLOAT, 24, value.y.z);
		mat.set(ValueLayout.JAVA_FLOAT, 28, value.y.w);
		mat.set(ValueLayout.JAVA_FLOAT, 32, value.z.x);
		mat.set(ValueLayout.JAVA_FLOAT, 36, value.z.y);
		mat.set(ValueLayout.JAVA_FLOAT, 40, value.z.z);
		mat.set(ValueLayout.JAVA_FLOAT, 44, value.z.w);
		mat.set(ValueLayout.JAVA_FLOAT, 48, value.w.x);
		mat.set(ValueLayout.JAVA_FLOAT, 52, value.w.y);
		mat.set(ValueLayout.JAVA_FLOAT, 56, value.w.z);
		mat.set(ValueLayout.JAVA_FLOAT, 60, value.w.w);

		GL.UniformMatrix4fv(location, 1, 0, mat.address());
	}

	/**
	 * Sets the uniforms for a camera.
	 * @param camera The camera to set the uniforms for.
	 * @param aspect The aspect ratio of the camera.
	 *
	 * The following uniforms are set:
	 * - cameraPosition (vec3)
	 * - viewProj (mat4)
	 * - invViewProj (mat4)
	 * - view (mat4)
	 * - invView (mat4)
	 */
	public void setCamera(Camera camera, float aspect) {
		this.setVec3("cameraPosition", camera.position);

		Mat4 view = camera.view();
		Mat4 viewProj = camera.viewProj(aspect);

		this.setMat4("viewProj", viewProj);
		this.setMat4("invViewProj", viewProj.inverse());

		this.setMat4("view", view);
		this.setMat4("invView", view.inverse());	
	}

	/**
	 * Sets a texture uniform.
	 * @param name The name of the uniform.
	 * @param texture The texture to set.
	 * @param target The texture target.
	 *
	 * The unit is automatically determined.
	 */
	public void setTexture(String name, int texture, int target) {
		int location = this.getUniformLocation(name);
		if (location == -1) return;

		GL.ActiveTexture(GL.TEXTURE0 + this.nextUnit);
		GL.BindTexture(target, texture);

		GL.Uniform1i(location, this.nextUnit);
		this.nextUnit++;
	}

	/**
	 * Sets a 2d texture uniform.
	 * @param name The name of the uniform.
	 * @param texture The texture to set.
	 *
	 * The unit is automatically determined.
	 */
	public void setTexture(String name, Texture texture) {
		this.setTexture(name, texture.textureId, GL.TEXTURE_2D);
	}

	/**
	 * Sets a cubemap texture uniform.
	 * @param name The name of the uniform.
	 * @param texture The texture to set.
	 *
	 * The unit is automatically determined.
	 */
	public void setTextureCube(String name, int texture) {
		this.setTexture(name, texture, GL.TEXTURE_CUBE_MAP);
	}

	/**
	 * Sets a buffer object uniform.
	 * @param name The name of the uniform.
	 * @param buffer The buffer to set.
	 * @param binding The binding point.
	 */
	public void setBuffer(String name, Buffer buffer, int binding) {
		int index = GL.GetProgramResourceIndex(this.programId, GL.SHADER_STORAGE_BLOCK, Lib.javaToStr(name).address());
		if (index == -1) return;
		GL.ShaderStorageBlockBinding(this.programId, index, binding);
		GL.BindBufferBase(GL.SHADER_STORAGE_BUFFER, binding, buffer.bufferId);
	}	

	/**
	 * Sets uniforms for a GBuffer.
	 * @param gbuffer The GBuffer to set the uniforms for.
	 *
	 * The following uniforms are set:
	 * - g_position (sampler2D)
	 * - g_normal (sampler2D)
	 * - g_baseColor (sampler2D)
	 * - g_material (sampler2D)
	 * - g_depth (sampler2D)
	 */
	public void setGBuffer(GBuffer gbuffer) {
		this.setTexture("g_position", gbuffer.position);
		this.setTexture("g_normal", gbuffer.normal);
		this.setTexture("g_baseColor", gbuffer.baseColor);
		this.setTexture("g_material", gbuffer.material);
		this.setTexture("g_depth", gbuffer.depth);
	}

	/**
	 * Deletes the shader program.
	 *
	 * Using the shader program after this will result in undefined behavior.
	 */
	public void delete() {
		GL.DeleteProgram(this.programId);
	}
}
