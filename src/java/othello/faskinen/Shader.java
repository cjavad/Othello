package othello.faskinen;

import java.lang.foreign.MemorySegment;
import java.lang.foreign.MemorySession;
import java.lang.foreign.ValueLayout;
import java.nio.file.Files;
import java.nio.file.Path;

import othello.faskinen.opengl.GL;

public class Shader {
	public int programId;

	public Shader(String source_path) {
		Path path = Path.of(source_path);
		String source;

		try { 
			source = Files.readString(path);
		} catch (Exception e) {
			throw new RuntimeException("Failed to read shader source: " + e.getMessage());
		};

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
}
