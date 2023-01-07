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

public class Shader {
	public int programId;
	private int nextUnit = 0;

	private static Pattern INCLUDE_PATTERN = Pattern.compile("#include \"(.*)\"");
	private static String VERSION = "#version 450 core";
	private static Path SHADER_PATH = Path.of("shaders");

	private static String readShaderFile(Path path) {
		try {
			return Files.readString(path);
		} catch (Exception e) {
			throw new RuntimeException("Failed to read shader file: " + path, e);
		}
	}

	private static String preprocessShader(String source, Path path, HashSet<Path> included) {
		Matcher matcher = INCLUDE_PATTERN.matcher(source);
		StringBuffer sb = new StringBuffer();

		while (matcher.find()) {
			Path includePath = path.resolveSibling(matcher.group(1));

			if (included.contains(includePath)) {
				matcher.appendReplacement(sb, "");

				continue;
			}
			included.add(includePath);

			String includeSource = readShaderFile(includePath);
			includeSource = preprocessShader(includeSource, includePath, included);
			matcher.appendReplacement(sb, includeSource);
		}

		matcher.appendTail(sb);

		return sb.toString();
	}

	public Shader(String vertexPath, String fragmentPath) {
		String vertexSource = readShaderFile(SHADER_PATH.resolve(vertexPath));
		String fragmentSource = readShaderFile(SHADER_PATH.resolve(fragmentPath));

		vertexSource = preprocessShader(vertexSource, SHADER_PATH.resolve(vertexPath), new HashSet<>());
		fragmentSource = preprocessShader(fragmentSource, SHADER_PATH.resolve(fragmentPath), new HashSet<>());

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

	public void use() {
		GL.UseProgram(this.programId);
		this.nextUnit = 0;
	}

	public int getVertexAttributeLocation(String name) {
		int location = GL.GetAttribLocation(this.programId, Lib.javaToStr(name).address());

		if (location == -1) {
			throw new RuntimeException("Failed to find vertex attribute: " + name);
		}

		return location;
	}

	public void bindVertexBuffer(String name, Buffer buffer, int offset, int stride) {
		int location = this.getVertexAttributeLocation(name);	

		GL.BindVertexBuffer(location, buffer.bufferId, offset, stride);
		GL.assertNoError();
	}

	public void bindIndexBuffer(Buffer buffer) {
		GL.BindBuffer(GL.ELEMENT_ARRAY_BUFFER, buffer.bufferId);
	}
	
	public void drawElements(int count, MemoryAddress offset) {	
		GL.DrawElements(GL.TRIANGLES, count, GL.UNSIGNED_INT, offset);
	}

	public void drawArrays(int index, int count) {
		GL.DrawArrays(GL.TRIANGLES, index, count);
	}

	public int getUniformLocation(String name) {
		return GL.GetUniformLocation(this.programId, Lib.javaToStr(name).address());
	}

	public void setInt(String name, int value) {
		int location = this.getUniformLocation(name);
		if (location == -1) return;
		GL.Uniform1i(location, value);
	}

	public void setFloat(String name, float value) {
		int location = this.getUniformLocation(name);
		if (location == -1) return;
		GL.Uniform1f(location, value);
	}

	public void setVec3(String name, Vec3 value) {
		int location = this.getUniformLocation(name);
		if (location == -1) return;
		GL.Uniform3f(location, value.x, value.y, value.z);
	}

	public void setVec4(String name, Vec4 value) {
		int location = this.getUniformLocation(name);
		if (location == -1) return;
		GL.Uniform4f(location, value.x, value.y, value.z, value.w);
	}

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

	public void setTexture(String name, Texture texture) {
		int location = this.getUniformLocation(name);
		if (location == -1) return;

		GL.ActiveTexture(GL.TEXTURE0 + this.nextUnit);
		GL.BindTexture(GL.TEXTURE_2D, texture.textureId);

		GL.Uniform1i(location, this.nextUnit);
		this.nextUnit++;
	}

	public void setBuffer(String name, Buffer buffer, int binding) {
		int index = GL.GetProgramResourceIndex(this.programId, GL.SHADER_STORAGE_BLOCK, Lib.javaToStr(name).address());
		if (index == -1) return;
		GL.ShaderStorageBlockBinding(this.programId, index, binding);
		GL.BindBufferBase(GL.SHADER_STORAGE_BUFFER, binding, buffer.bufferId);
	}	

	public void free() {
		GL.DeleteProgram(this.programId);
	}
}
