package othello.faskinen;

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

	private static Pattern INCLUDE_PATTERN = Pattern.compile("#include \"(.*)\"");

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

	public Shader(String source_path) {
		Path path = Path.of(source_path);
		String source = readShaderFile(path);
		source = preprocessShader(source, path, new HashSet<>());

		MemorySegment sources = MemorySession.openImplicit().allocate(8);
		sources.set(ValueLayout.ADDRESS, 0, Lib.javaToStr(source).address());

		int shader = GL.CreateShader(GL.COMPUTE_SHADER);
		GL.ShaderSource(shader, 1, sources.address(), Lib.NULLPTR);
		GL.CompileShader(shader);

		MemorySegment status = MemorySession.openImplicit().allocate(4);
		GL.GetShaderiv(shader, GL.COMPILE_STATUS, status.address());

		if (status.get(ValueLayout.JAVA_INT, 0) != GL.TRUE) {
			MemorySegment log = MemorySession.openImplicit().allocate(1024);
			GL.GetShaderInfoLog(shader, 1024, Lib.NULLPTR, log.address());

			System.out.println(Lib.strToJava(log));
			throw new RuntimeException("Failed to compile shader");
		}

		this.programId = GL.CreateProgram();
		GL.AttachShader(this.programId, shader);

		GL.LinkProgram(this.programId);
		GL.GetProgramiv(this.programId, GL.LINK_STATUS, status.address());

		if (status.get(ValueLayout.JAVA_INT, 0) != GL.TRUE) {
			MemorySegment log = MemorySession.openImplicit().allocate(1024);
			MemorySegment length = MemorySession.openImplicit().allocate(4);
			GL.GetProgramInfoLog(this.programId, 1024, length.address(), log.address());

			System.out.println(Lib.strToJava(log));
			throw new RuntimeException("Failed to link shader");
		}

		GL.DeleteShader(shader);
	}

	public void use() {
		GL.UseProgram(this.programId);
	}

	public int getUniformLocation(String name) {
		return GL.GetUniformLocation(this.programId, Lib.javaToStr(name).address());
	}

	public void setInt(String name, int value) {
		GL.Uniform1i(this.getUniformLocation(name), value);
	}

	public void setFloat(String name, float value) {
		GL.Uniform1f(this.getUniformLocation(name), value);
	}

	public void setVec3(String name, Vec3 value) {
		GL.Uniform3f(this.getUniformLocation(name), value.x, value.y, value.z);
	}

	public void setVec4(String name, Vec4 value) {
		GL.Uniform4f(this.getUniformLocation(name), value.x, value.y, value.z, value.w);
	}

	public void setMat4(String name, Mat4 value) {
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

		GL.UniformMatrix4fv(this.getUniformLocation(name), 1, 0, mat.address());
	}

	public void setBuffer(String name, Buffer buffer, int binding) {
		int index = GL.GetProgramResourceIndex(this.programId, GL.SHADER_STORAGE_BLOCK, Lib.javaToStr(name).address());
		GL.ShaderStorageBlockBinding(this.programId, index, binding);
		GL.BindBufferBase(GL.SHADER_STORAGE_BUFFER, binding, buffer.bufferId);
	}

	public void free() {
		GL.DeleteProgram(this.programId);
	}
}
