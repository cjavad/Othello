package othello.faskinen.opengl;

import java.lang.foreign.MemoryAddress;
import java.lang.foreign.MemoryLayout;
import java.lang.invoke.MethodHandle;

import othello.faskinen.Lib;
import othello.faskinen.Platform;
import othello.faskinen.win32.Win32;

/**
 * A wrapper around OpenGL.
 */
public class GL {
    static {
		System.out.println("OpenGL Loaded");

        switch (Platform.get()) {
            case Windows:
                System.loadLibrary("opengl32");
                break;
            case Linux:
                System.loadLibrary("GL");
                break;
            default:
                throw new RuntimeException("Unsupported platform, not a real gamer");
        }
    }

	/*
		typedef void APIENTRY funcname(GLenum source, GLenum type, GLuint id,
	   GLenum severity, GLsizei length, const GLchar* message, const void* userParam);
	 */
	public static void debugCallback(
			int source, int type, int id, int severity, int length, MemoryAddress message, MemoryAddress userParam
	) {
		
	}

	protected static MethodHandle loadFuncGL(String name, MemoryLayout ret, MemoryLayout... params) {
		if (Platform.get() == Platform.Windows) {
			MemoryAddress addr = Win32.wglGetProcAddress(Lib.javaToStr(name).address()).address();
			if (addr.toRawLongValue() != 0) {
				return Lib.loadFuncFromAddr(addr, ret, params);
			}
		}
		return Lib.loadFuncHandle(name, ret, params);
	}		

	public static void assertNoError() {
		switch (GL.GetError()) {
			case GL.NO_ERROR:
				return;
			case GL.INVALID_ENUM:
				throw new RuntimeException("GL_INVALID_ENUM");
			case GL.INVALID_VALUE:
				throw new RuntimeException("GL_INVALID_VALUE");
			case GL.INVALID_OPERATION:
				throw new RuntimeException("GL_INVALID_OPERATION");
			case GL.INVALID_FRAMEBUFFER_OPERATION:
				throw new RuntimeException("GL_INVALID_FRAMEBUFFER_OPERATION");
			case GL.OUT_OF_MEMORY:
				throw new RuntimeException("GL_OUT_OF_MEMORY");
			case GL.STACK_UNDERFLOW:
				throw new RuntimeException("GL_STACK_UNDERFLOW");
			case GL.STACK_OVERFLOW:
				throw new RuntimeException("GL_STACK_OVERFLOW");
			case GL.CONTEXT_LOST:
				throw new RuntimeException("GL_CONTEXT_LOST");
			default:
				throw new RuntimeException("Unknown GL error");
		}
	}

	private static MethodHandle HANDLE_glClearColor = loadFuncGL(
		"glClearColor", 
		null,
		Lib.C_FLOAT32_T,
		Lib.C_FLOAT32_T,
		Lib.C_FLOAT32_T,
		Lib.C_FLOAT32_T
	);
	public static void ClearColor(float r, float g, float b, float a) {
		try {
			HANDLE_glClearColor.invoke(r, g, b, a);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static MethodHandle HANDLE_glClear = loadFuncGL(
		"glClear", 
		null,
		Lib.C_INT32_T
	);
	public static void Clear(int mask) {
		try {
			HANDLE_glClear.invoke(mask);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static MethodHandle HANDLE_glViewport = loadFuncGL(
		"glViewport", 
		null,
		Lib.C_INT32_T,
		Lib.C_INT32_T,
		Lib.C_INT32_T,
		Lib.C_INT32_T
	);
	public static void Viewport(int x, int y, int width, int height) {
		try {
			HANDLE_glViewport.invoke(x, y, width, height);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static MethodHandle HANDLE_glEnable = loadFuncGL(
		"glEnable", 
		null,
		Lib.C_INT32_T
	);
	public static void Enable(int cap) {
		try {
			HANDLE_glEnable.invoke(cap);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static MethodHandle HANDLE_glDisable = loadFuncGL(
		"glDisable", 
		null,
		Lib.C_INT32_T
	);
	public static void Disable(int cap) {
		try {
			HANDLE_glDisable.invoke(cap);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static MethodHandle HANDLE_glPixelStorei = loadFuncGL(
		"glPixelStorei", 
		null,
		Lib.C_INT32_T,
		Lib.C_INT32_T
	);
	public static void PixelStorei(int pname, int param) {
		try {
			HANDLE_glPixelStorei.invoke(pname, param);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static MethodHandle HANDLE_glPixelStoref = loadFuncGL(
		"glPixelStoref", 
		null,
		Lib.C_INT32_T,
		Lib.C_FLOAT32_T
	);
	public static void PixelStoref(int pname, float param) {
		try {
			HANDLE_glPixelStoref.invoke(pname, param);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}
	}


	private static MethodHandle HANDLE_glBlendFunc = loadFuncGL(
		"glBlendFunc", 
		null,
		Lib.C_INT32_T,
		Lib.C_INT32_T
	);
	public static void BlendFunc(int sfactor, int dfactor) {
		try {
			HANDLE_glBlendFunc.invoke(sfactor, dfactor);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static MethodHandle HANDLE_glGetError = loadFuncGL(
		"glGetError", 
		Lib.C_INT32_T
	);
	public static int GetError() {
		try {
			return (int) HANDLE_glGetError.invoke();
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
			return 0;
		}
	}

	private static MethodHandle HANDLE_glCreateShader = loadFuncGL(
		"glCreateShader", 
		Lib.C_UINT32_T,
		Lib.C_INT32_T
	);
	public static int CreateShader(int shaderType) {
		try {
			return (int) HANDLE_glCreateShader.invoke(shaderType);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}

		return -1;
	}

	private static MethodHandle HANDLE_glShaderSource = loadFuncGL(
		"glShaderSource", 
		null,
		Lib.C_UINT32_T,
		Lib.C_INT32_T,
		Lib.C_POINTER_T,
		Lib.C_POINTER_T
	);
	public static void ShaderSource(int shader, int count, MemoryAddress string, MemoryAddress length) {
		try {
			HANDLE_glShaderSource.invoke(shader, count, string, length);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static MethodHandle HANDLE_glCompileShader = loadFuncGL(
		"glCompileShader", 
		null,
		Lib.C_UINT32_T
	);
	public static void CompileShader(int shader) {
		try {
			HANDLE_glCompileShader.invoke(shader);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static MethodHandle HANDLE_glGetShaderiv = loadFuncGL(
		"glGetShaderiv", 
		null,
		Lib.C_UINT32_T,
		Lib.C_INT32_T,
		Lib.C_POINTER_T
	);
	public static void GetShaderiv(int shader, int pname, MemoryAddress params) {
		try {
			HANDLE_glGetShaderiv.invoke(shader, pname, params);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static MethodHandle HANDLE_glGetShaderInfoLog = loadFuncGL(
		"glGetShaderInfoLog", 
		null,
		Lib.C_UINT32_T,
		Lib.C_INT32_T,
		Lib.C_POINTER_T,
		Lib.C_POINTER_T
	);
	public static void GetShaderInfoLog(int shader, int bufSize, MemoryAddress length, MemoryAddress infoLog) {
		try {
			HANDLE_glGetShaderInfoLog.invoke(shader, bufSize, length, infoLog);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static MethodHandle HANDLE_glCreateProgram = loadFuncGL(
		"glCreateProgram", 
		Lib.C_UINT32_T
	);
	public static int CreateProgram() {
		try {
			return (int) HANDLE_glCreateProgram.invoke();
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}

		return -1;
	}

	private static MethodHandle HANDLE_glAttachShader = loadFuncGL(
		"glAttachShader", 
		null,
		Lib.C_UINT32_T,
		Lib.C_UINT32_T
	);
	public static void AttachShader(int program, int shader) {
		try {
			HANDLE_glAttachShader.invoke(program, shader);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static MethodHandle HANDLE_glLinkProgram = loadFuncGL(
		"glLinkProgram", 
		null,
		Lib.C_UINT32_T
	);
	public static void LinkProgram(int program) {
		try {
			HANDLE_glLinkProgram.invoke(program);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static MethodHandle HANDLE_glGetProgramiv = loadFuncGL(
		"glGetProgramiv", 
		null,
		Lib.C_UINT32_T,
		Lib.C_INT32_T,
		Lib.C_POINTER_T
	);
	public static void GetProgramiv(int program, int pname, MemoryAddress params) {
		try {
			HANDLE_glGetProgramiv.invoke(program, pname, params);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static MethodHandle HANDLE_glGetProgramInfoLog = loadFuncGL(
		"glGetProgramInfoLog", 
		null,
		Lib.C_UINT32_T,
		Lib.C_INT32_T,
		Lib.C_POINTER_T,
		Lib.C_POINTER_T
	);
	public static void GetProgramInfoLog(int program, int bufSize, MemoryAddress length, MemoryAddress infoLog) {
		try {
			HANDLE_glGetProgramInfoLog.invoke(program, bufSize, length, infoLog);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static MethodHandle HANDLE_glUseProgram = loadFuncGL(
		"glUseProgram", 
		null,
		Lib.C_UINT32_T
	);
	public static void UseProgram(int program) {
		try {
			HANDLE_glUseProgram.invoke(program);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static MethodHandle HANDLE_glGetUniformLocation = loadFuncGL(
		"glGetUniformLocation", 
		Lib.C_INT32_T,
		Lib.C_UINT32_T,
		Lib.C_POINTER_T
	);
	public static int GetUniformLocation(int program, MemoryAddress name) {
		try {
			return (int) HANDLE_glGetUniformLocation.invoke(program, name);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}

		return -1;
	}

	private static MethodHandle HANDLE_glGetProgramResourceIndex = loadFuncGL(
		"glGetProgramResourceIndex", 
		Lib.C_UINT32_T,
		Lib.C_UINT32_T,
		Lib.C_INT32_T,
		Lib.C_POINTER_T
	);
	public static int GetProgramResourceIndex(int program, int programInterface, MemoryAddress name) {
		try {
			return (int) HANDLE_glGetProgramResourceIndex.invoke(program, programInterface, name);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}

		return -1;
	}

	private static MethodHandle HANDLE_glShaderStorageBlockBinding = loadFuncGL(
		"glShaderStorageBlockBinding", 
		null,
		Lib.C_UINT32_T,
		Lib.C_UINT32_T,
		Lib.C_UINT32_T
	);
	public static void ShaderStorageBlockBinding(int program, int storageBlockIndex, int storageBlockBinding) {
		try {
			HANDLE_glShaderStorageBlockBinding.invoke(program, storageBlockIndex, storageBlockBinding);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static MethodHandle HANDLE_glGetVertexAttributeLocation = loadFuncGL(
		"glGetAttribLocation", 
		Lib.C_INT32_T,
		Lib.C_UINT32_T,
		Lib.C_POINTER_T
	);
	public static int GetAttribLocation(int program, MemoryAddress name) {
		try {
			return (int) HANDLE_glGetVertexAttributeLocation.invoke(program, name);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}

		return -1;
	}

	private static MethodHandle HANDLE_glBindVertexBuffer = loadFuncGL(
		"glBindVertexBuffer", 
		null,
		Lib.C_UINT32_T,
		Lib.C_UINT32_T,
		Lib.C_UINT64_T,
		Lib.C_INT32_T
	);
	public static void BindVertexBuffer(int bindingindex, int buffer, long offset, int stride) {
		try {
			HANDLE_glBindVertexBuffer.invoke(bindingindex, buffer, offset, stride);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static MethodHandle HANDLE_glUniform1i = loadFuncGL(
		"glUniform1i", 
		null,
		Lib.C_INT32_T,
		Lib.C_INT32_T
	);
	public static void Uniform1i(int location, int v0) {
		try {
			HANDLE_glUniform1i.invoke(location, v0);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static MethodHandle HANDLE_glUniform1ui = loadFuncGL(
		"glUniform1ui", 
		null,
		Lib.C_INT32_T,
		Lib.C_UINT32_T
	);
	public static void Uniform1ui(int location, int v0) {
		try {
			HANDLE_glUniform1ui.invoke(location, v0);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static MethodHandle HANDLE_glUniform1f = loadFuncGL(
		"glUniform1f", 
		null,
		Lib.C_INT32_T,
		Lib.C_FLOAT32_T
	);
	public static void Uniform1f(int location, float v0) {
		try {
			HANDLE_glUniform1f.invoke(location, v0);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static MethodHandle HANDLE_glUniform2f = loadFuncGL(
		"glUniform2f", 
		null,
		Lib.C_INT32_T,
		Lib.C_FLOAT32_T,
		Lib.C_FLOAT32_T
	);
	public static void Uniform2f(int location, float v0, float v1) {
		try {
			HANDLE_glUniform2f.invoke(location, v0, v1);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static MethodHandle HANDLE_glUniform3f = loadFuncGL(
		"glUniform3f", 
		null,
		Lib.C_INT32_T,
		Lib.C_FLOAT32_T,
		Lib.C_FLOAT32_T,
		Lib.C_FLOAT32_T
	);
	public static void Uniform3f(int location, float v0, float v1, float v2) {
		try {
			HANDLE_glUniform3f.invoke(location, v0, v1, v2);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static MethodHandle HANDLE_glUniform4f = loadFuncGL(
		"glUniform4f", 
		null,
		Lib.C_INT32_T,
		Lib.C_FLOAT32_T,
		Lib.C_FLOAT32_T,
		Lib.C_FLOAT32_T,
		Lib.C_FLOAT32_T
	);
	public static void Uniform4f(int location, float v0, float v1, float v2, float v3) {
		try {
			HANDLE_glUniform4f.invoke(location, v0, v1, v2, v3);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static MethodHandle HANDLE_glUniformMatrix4fv = loadFuncGL(
		"glUniformMatrix4fv", 
		null,
		Lib.C_INT32_T,
		Lib.C_INT32_T,
		Lib.C_INT32_T,
		Lib.C_POINTER_T
	);
	public static void UniformMatrix4fv(int location, int count, int transpose, MemoryAddress value) {
		try {
			HANDLE_glUniformMatrix4fv.invoke(location, count, transpose, value);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static MethodHandle HANDLE_glGenBuffers = loadFuncGL(
		"glGenBuffers", 
		null,
		Lib.C_INT32_T,
		Lib.C_POINTER_T
	);
	public static void GenBuffers(int n, MemoryAddress buffers) {
		try {
			HANDLE_glGenBuffers.invoke(n, buffers);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static MethodHandle HANDLE_glBindBuffer = loadFuncGL(
		"glBindBuffer", 
		null,
		Lib.C_UINT32_T,
		Lib.C_UINT32_T
	);
	public static void BindBuffer(int target, int buffer) {
		try {
			HANDLE_glBindBuffer.invoke(target, buffer);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static MethodHandle HANDLE_glBufferData = loadFuncGL(
		"glBufferData", 
		null,
		Lib.C_UINT32_T,
		Lib.C_INT64_T,
		Lib.C_POINTER_T,
		Lib.C_UINT32_T
	);
	public static void BufferData(int target, long size, MemoryAddress data, int usage) {
		try {
			HANDLE_glBufferData.invoke(target, size, data, usage);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static MethodHandle HANDLE_glMapBuffer = loadFuncGL(
		"glMapBuffer", 
		null,
		Lib.C_POINTER_T,
		Lib.C_UINT32_T,
		Lib.C_UINT32_T
	);
	public static MemoryAddress MapBuffer(int target, int access) {
		try {
			return (MemoryAddress) HANDLE_glMapBuffer.invoke(target, access);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}
		return null;
	}

	private static MethodHandle HANDLE_glMapBufferRange = loadFuncGL(
		"glMapBufferRange", 
		null,
		Lib.C_POINTER_T,
		Lib.C_UINT32_T,
		Lib.C_INT64_T,
		Lib.C_INT64_T
	);
	public static MemoryAddress MapBufferRange(int target, long offset, long length, int access) {
		try {
			return (MemoryAddress) HANDLE_glMapBufferRange.invoke(target, offset, length, access);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}
		return null;
	}

	private static MethodHandle HANDLE_glUnmapBuffer = loadFuncGL(
		"glUnmapBuffer", 
		null,
		Lib.C_INT32_T,
		Lib.C_UINT32_T
	);
	public static boolean UnmapBuffer(int target) {
		try {
			return (boolean) HANDLE_glUnmapBuffer.invoke(target);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}
		return false;
	}

	private static MethodHandle HANDLE_glGetBufferSubData = loadFuncGL(
		"glGetBufferSubData", 
		null,
		Lib.C_UINT32_T,
		Lib.C_INT64_T,
		Lib.C_INT64_T,
		Lib.C_POINTER_T
	);
	public static void GetBufferSubData(int target, long offset, long size, MemoryAddress data) {
		try {
			HANDLE_glGetBufferSubData.invoke(target, offset, size, data);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static MethodHandle HANDLE_glBindBufferBase = loadFuncGL(
		"glBindBufferBase", 
		null,
		Lib.C_UINT32_T,
		Lib.C_UINT32_T,
		Lib.C_UINT32_T
	);
	public static void BindBufferBase(int target, int index, int buffer) {
		try {
			HANDLE_glBindBufferBase.invoke(target, index, buffer);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static MethodHandle HANDLE_glGenVertexArrays = loadFuncGL(
		"glGenVertexArrays", 
		null,
		Lib.C_INT32_T,
		Lib.C_POINTER_T
	);
	public static void GenVertexArrays(int n, MemoryAddress arrays) {
		try {
			HANDLE_glGenVertexArrays.invoke(n, arrays);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static MethodHandle HANDLE_glBindVertexArray = loadFuncGL(
		"glBindVertexArray", 
		null,
		Lib.C_UINT32_T
	);
	public static void BindVertexArray(int array) {
		try {
			HANDLE_glBindVertexArray.invoke(array);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static MethodHandle HANDLE_glVertexAttribPointer = loadFuncGL(
		"glVertexAttribPointer", 
		null,
		Lib.C_UINT32_T,
		Lib.C_INT32_T,
		Lib.C_UINT32_T,
		Lib.C_INT32_T,
		Lib.C_INT64_T,
		Lib.C_INT64_T
	);
	public static void VertexAttribPointer(int index, int size, int type, int normalized, long stride, long pointer) {
		try {
			HANDLE_glVertexAttribPointer.invoke(index, size, type, normalized, stride, pointer);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static MethodHandle HANDLE_glEnableVertexAttribArray = loadFuncGL(
		"glEnableVertexAttribArray", 
		null,
		Lib.C_UINT32_T
	);
	public static void EnableVertexAttribArray(int index) {
		try {
			HANDLE_glEnableVertexAttribArray.invoke(index);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static MethodHandle HANDLE_glGenTextures = loadFuncGL(
		"glGenTextures", 
		null,
		Lib.C_INT32_T,
		Lib.C_POINTER_T
	);
	public static void GenTextures(int n, MemoryAddress textures) {
		try {
			HANDLE_glGenTextures.invoke(n, textures);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static MethodHandle HANDLE_glActiveTexture = loadFuncGL(
		"glActiveTexture", 
		null,
		Lib.C_UINT32_T
	);
	public static void ActiveTexture(int texture) {
		try {
			HANDLE_glActiveTexture.invoke(texture);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static MethodHandle HANDLE_glBindTexture = loadFuncGL(
		"glBindTexture", 
		null,
		Lib.C_UINT32_T,
		Lib.C_UINT32_T
	);
	public static void BindTexture(int target, int texture) {
		try {
			HANDLE_glBindTexture.invoke(target, texture);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static MethodHandle HANDLE_glTexImage2D = loadFuncGL(
		"glTexImage2D", 
		null,
		Lib.C_UINT32_T,
		Lib.C_INT32_T,
		Lib.C_INT32_T,
		Lib.C_INT32_T,
		Lib.C_INT32_T,
		Lib.C_INT32_T,
		Lib.C_UINT32_T,
		Lib.C_UINT32_T,
		Lib.C_POINTER_T
	);
	public static void TexImage2D(
		int target, 
		int level, 
		int internalformat, 
		int width, 
		int height, 
		int border, 
		int format, 
		int type, 
		MemoryAddress pixels
	) {
		try {
			HANDLE_glTexImage2D.invoke(target, level, internalformat, width, height, border, format, type, pixels);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static MethodHandle HANDLE_glTexParameteri = loadFuncGL(
		"glTexParameteri", 
		null,
		Lib.C_UINT32_T,
		Lib.C_UINT32_T,
		Lib.C_INT32_T
	);
	public static void TexParameteri(int target, int pname, int param) {
		try {
			HANDLE_glTexParameteri.invoke(target, pname, param);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static MethodHandle HANDLE_glTexParameterf = loadFuncGL(
		"glTexParameterf", 
		null,
		Lib.C_UINT32_T,
		Lib.C_UINT32_T,
		Lib.C_FLOAT32_T
	);
	public static void TexParameterf(int target, int pname, float param) {
		try {
			HANDLE_glTexParameterf.invoke(target, pname, param);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static MethodHandle HANDLE_glGenerateMipmap = loadFuncGL(
		"glGenerateMipmap", 
		null,
		Lib.C_UINT32_T
	);
	public static void GenerateMipmap(int target) {
		try {
			HANDLE_glGenerateMipmap.invoke(target);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static MethodHandle HANDLE_glClearTexImage = loadFuncGL(
		"glClearTexImage", 
		null,
		Lib.C_UINT32_T,
		Lib.C_INT32_T,
		Lib.C_UINT32_T,
		Lib.C_UINT32_T,
		Lib.C_POINTER_T
	);
	public static void ClearTexImage(int texture, int level, int format, int type, MemoryAddress data) {
		try {
			HANDLE_glClearTexImage.invoke(texture, level, format, type, data);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static MethodHandle HANDLE_glGetTexImage = loadFuncGL(
		"glGetTexImage", 
		null,
		Lib.C_UINT32_T,
		Lib.C_INT32_T,
		Lib.C_UINT32_T,
		Lib.C_UINT32_T,
		Lib.C_POINTER_T
	);
	public static void GetTexImage(int target, int level, int format, int type, MemoryAddress pixels) {
		try {
			HANDLE_glGetTexImage.invoke(target, level, format, type, pixels);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static MethodHandle HANDLE_glGetTextureSubImage = loadFuncGL(
		"glGetTextureSubImage", 
		null,
		Lib.C_UINT32_T,
		Lib.C_INT32_T,
		Lib.C_INT32_T,
		Lib.C_INT32_T,
		Lib.C_INT32_T,
		Lib.C_INT32_T,
		Lib.C_INT32_T,
		Lib.C_INT32_T,
		Lib.C_UINT32_T,
		Lib.C_UINT32_T,
		Lib.C_INT32_T,
		Lib.C_POINTER_T
	);
	public static void GetTextureSubImage(
		int texture, 
		int level, 
		int xoffset, 
		int yoffset, 
		int zoffset, 
		int width, 
		int height, 
		int depth, 
		int format, 
		int type, 
		int bufSize, 
		MemoryAddress pixels
	) {
		try {
			HANDLE_glGetTextureSubImage.invoke(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, bufSize, pixels);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static MethodHandle HANDLE_glGenFramebuffers = loadFuncGL(
		"glGenFramebuffers", 
		null,
		Lib.C_INT32_T,
		Lib.C_POINTER_T
	);
	public static void GenFramebuffers(int n, MemoryAddress framebuffers) {
		try {
			HANDLE_glGenFramebuffers.invoke(n, framebuffers);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static MethodHandle HANDLE_glBindFramebuffer = loadFuncGL(
		"glBindFramebuffer", 
		null,
		Lib.C_UINT32_T,
		Lib.C_UINT32_T
	);
	public static void BindFramebuffer(int target, int framebuffer) {
		try {
			HANDLE_glBindFramebuffer.invoke(target, framebuffer);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static MethodHandle HANDLE_glFramebufferTexture2D = loadFuncGL(
		"glFramebufferTexture2D", 
		null,
		Lib.C_UINT32_T,
		Lib.C_UINT32_T,
		Lib.C_UINT32_T,
		Lib.C_UINT32_T,
		Lib.C_INT32_T
	);
	public static void FramebufferTexture2D(int target, int attachment, int textarget, int texture, int level) {
		try {
			HANDLE_glFramebufferTexture2D.invoke(target, attachment, textarget, texture, level);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static MethodHandle HANDLE_glCheckFramebufferStatus = loadFuncGL(
		"glCheckFramebufferStatus", 
		Lib.C_UINT32_T,
		Lib.C_UINT32_T
	);
	public static int CheckFramebufferStatus(int target) {
		try {
			return (int) HANDLE_glCheckFramebufferStatus.invoke(target);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
			return 0;
		}
	}

	private static MethodHandle HANDLE_glPolygonMode = loadFuncGL(
		"glPolygonMode", 
		null,
		Lib.C_UINT32_T,
		Lib.C_UINT32_T
	);
	public static void PolygonMode(int face, int mode) {
		try {
			HANDLE_glPolygonMode.invoke(face, mode);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static MethodHandle HANDLE_glDrawBuffers = loadFuncGL(
		"glDrawBuffers", 
		null,
		Lib.C_INT32_T,
		Lib.C_POINTER_T
	);
	public static void DrawBuffers(int n, MemoryAddress bufs) {
		try {
			HANDLE_glDrawBuffers.invoke(n, bufs);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static MethodHandle HANDLE_glDrawBuffer = loadFuncGL(
		"glDrawBuffer", 
		null,
		Lib.C_UINT32_T
	);
	public static void DrawBuffer(int buf) {
		try {
			HANDLE_glDrawBuffer.invoke(buf);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static MethodHandle HANDLE_glReadBuffer = loadFuncGL(
		"glReadBuffer", 
		null,
		Lib.C_UINT32_T
	);
	public static void ReadBuffer(int mode) {
		try {
			HANDLE_glReadBuffer.invoke(mode);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static MethodHandle HANDLE_glDrawElements = loadFuncGL(
		"glDrawElements", 
		Lib.C_INT32_T,
		Lib.C_UINT32_T,
		Lib.C_INT32_T,
		Lib.C_UINT32_T,
		Lib.C_UINT64_T
	);
	public static void DrawElements(int mode, int count, int type, long indices) {
		try {
			HANDLE_glDrawElements.invoke(mode, count, type, indices);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static MethodHandle HANDLE_glDrawArrays = loadFuncGL(
		"glDrawArrays", 
		null,
		Lib.C_UINT32_T,
		Lib.C_INT32_T,
		Lib.C_INT32_T
	);
	public static void DrawArrays(int mode, int first, int count) {
		try {
			HANDLE_glDrawArrays.invoke(mode, first, count);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static MethodHandle HANDLE_glDispatchCompute = loadFuncGL(
		"glDispatchCompute", 
		null,
		Lib.C_UINT32_T,
		Lib.C_UINT32_T,
		Lib.C_UINT32_T
	);
	public static void DispatchCompute(int num_groups_x, int num_groups_y, int num_groups_z) {
		try {
			HANDLE_glDispatchCompute.invoke(num_groups_x, num_groups_y, num_groups_z);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static MethodHandle HANDLE_glFinish = loadFuncGL(
		"glFinish", 
		null
	);
	public static void Finish() {
		try {
			HANDLE_glFinish.invoke();
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static MethodHandle HANDLE_glDeleteShader = loadFuncGL(
		"glDeleteShader", 
		null,
		Lib.C_UINT32_T
	);
	public static void DeleteShader(int shader) {
		try {
			HANDLE_glDeleteShader.invoke(shader);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static MethodHandle HANDLE_glDeleteProgram = loadFuncGL(
		"glDeleteProgram", 
		null,
		Lib.C_UINT32_T
	);
	public static void DeleteProgram(int program) {
		try {
			HANDLE_glDeleteProgram.invoke(program);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static MethodHandle HANDLE_glDeleteBuffers = loadFuncGL(
		"glDeleteBuffers", 
		null,
		Lib.C_INT32_T,
		Lib.C_POINTER_T
	);
	public static void DeleteBuffers(int n, MemoryAddress buffers) {
		try {
			HANDLE_glDeleteBuffers.invoke(n, buffers);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static MethodHandle HANDLE_glDeleteVertexArrays = loadFuncGL(
		"glDeleteVertexArrays", 
		null,
		Lib.C_INT32_T,
		Lib.C_POINTER_T
	);
	public static void DeleteVertexArrays(int n, MemoryAddress vertexArrays) {
		try {
			HANDLE_glDeleteVertexArrays.invoke(n, vertexArrays);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static MethodHandle HANDLE_glDeleteTextures = loadFuncGL(
		"glDeleteTextures", 
		null,
		Lib.C_INT32_T,
		Lib.C_POINTER_T
	);
	public static void DeleteTextures(int n, MemoryAddress textures) {
		try {
			HANDLE_glDeleteTextures.invoke(n, textures);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static MethodHandle HANDLE_glDeleteFramebuffers = loadFuncGL(
		"glDeleteFramebuffers", 
		null,
		Lib.C_INT32_T,
		Lib.C_POINTER_T
	);
	public static void DeleteFramebuffers(int n, MemoryAddress framebuffers) {
		try {
			HANDLE_glDeleteFramebuffers.invoke(n, framebuffers);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public static final int
		ACTIVE_ATOMIC_COUNTER_BUFFERS = 0x92D9,
		ACTIVE_ATTRIBUTES = 0x8B89,
		ACTIVE_ATTRIBUTE_MAX_LENGTH = 0x8B8A,
		ACTIVE_PROGRAM = 0x8259,
		ACTIVE_RESOURCES = 0x92F5,
		ACTIVE_SUBROUTINES = 0x8DE5,
		ACTIVE_SUBROUTINE_MAX_LENGTH = 0x8E48,
		ACTIVE_SUBROUTINE_UNIFORMS = 0x8DE6,
		ACTIVE_SUBROUTINE_UNIFORM_LOCATIONS = 0x8E47,
		ACTIVE_SUBROUTINE_UNIFORM_MAX_LENGTH = 0x8E49,
		ACTIVE_TEXTURE = 0x84E0,
		ACTIVE_UNIFORMS = 0x8B86,
		ACTIVE_UNIFORM_BLOCKS = 0x8A36,
		ACTIVE_UNIFORM_BLOCK_MAX_NAME_LENGTH = 0x8A35,
		ACTIVE_UNIFORM_MAX_LENGTH = 0x8B87,
		ACTIVE_VARIABLES = 0x9305,
		ALIASED_LINE_WIDTH_RANGE = 0x846E,
		ALL_BARRIER_BITS = 0xFFFFFFFF,
		ALL_SHADER_BITS = 0xFFFFFFFF,
		ALPHA = 0x1906,
		ALREADY_SIGNALED = 0x911A,
		ALWAYS = 0x0207,
		AND = 0x1501,
		AND_INVERTED = 0x1504,
		AND_REVERSE = 0x1502,
		ANY_SAMPLES_PASSED = 0x8C2F,
		ANY_SAMPLES_PASSED_CONSERVATIVE = 0x8D6A,
		ARRAY_BUFFER = 0x8892,
		ARRAY_BUFFER_BINDING = 0x8894,
		ARRAY_SIZE = 0x92FB,
		ARRAY_STRIDE = 0x92FE,
		ATOMIC_COUNTER_BARRIER_BIT = 0x00001000,
		ATOMIC_COUNTER_BUFFER = 0x92C0,
		ATOMIC_COUNTER_BUFFER_ACTIVE_ATOMIC_COUNTERS = 0x92C5,
		ATOMIC_COUNTER_BUFFER_ACTIVE_ATOMIC_COUNTER_INDICES = 0x92C6,
		ATOMIC_COUNTER_BUFFER_BINDING = 0x92C1,
		ATOMIC_COUNTER_BUFFER_DATA_SIZE = 0x92C4,
		ATOMIC_COUNTER_BUFFER_INDEX = 0x9301,
		ATOMIC_COUNTER_BUFFER_REFERENCED_BY_COMPUTE_SHADER = 0x90ED,
		ATOMIC_COUNTER_BUFFER_REFERENCED_BY_FRAGMENT_SHADER = 0x92CB,
		ATOMIC_COUNTER_BUFFER_REFERENCED_BY_GEOMETRY_SHADER = 0x92CA,
		ATOMIC_COUNTER_BUFFER_REFERENCED_BY_TESS_CONTROL_SHADER = 0x92C8,
		ATOMIC_COUNTER_BUFFER_REFERENCED_BY_TESS_EVALUATION_SHADER = 0x92C9,
		ATOMIC_COUNTER_BUFFER_REFERENCED_BY_VERTEX_SHADER = 0x92C7,
		ATOMIC_COUNTER_BUFFER_SIZE = 0x92C3,
		ATOMIC_COUNTER_BUFFER_START = 0x92C2,
		ATTACHED_SHADERS = 0x8B85,
		AUTO_GENERATE_MIPMAP = 0x8295,
		BACK = 0x0405,
		BACK_LEFT = 0x0402,
		BACK_RIGHT = 0x0403,
		BGR = 0x80E0,
		BGRA = 0x80E1,
		BGRA_INTEGER = 0x8D9B,
		BGR_INTEGER = 0x8D9A,
		BLEND = 0x0BE2,
		BLEND_COLOR = 0x8005,
		BLEND_DST = 0x0BE0,
		BLEND_DST_ALPHA = 0x80CA,
		BLEND_DST_RGB = 0x80C8,
		BLEND_EQUATION = 0x8009,
		BLEND_EQUATION_ALPHA = 0x883D,
		BLEND_EQUATION_RGB = 0x8009,
		BLEND_SRC = 0x0BE1,
		BLEND_SRC_ALPHA = 0x80CB,
		BLEND_SRC_RGB = 0x80C9,
		BLOCK_INDEX = 0x92FD,
		BLUE = 0x1905,
		BLUE_INTEGER = 0x8D96,
		BOOL = 0x8B56,
		BOOL_VEC2 = 0x8B57,
		BOOL_VEC3 = 0x8B58,
		BOOL_VEC4 = 0x8B59,
		BUFFER = 0x82E0,
		BUFFER_ACCESS = 0x88BB,
		BUFFER_ACCESS_FLAGS = 0x911F,
		BUFFER_BINDING = 0x9302,
		BUFFER_DATA_SIZE = 0x9303,
		BUFFER_IMMUTABLE_STORAGE = 0x821F,
		BUFFER_MAPPED = 0x88BC,
		BUFFER_MAP_LENGTH = 0x9120,
		BUFFER_MAP_OFFSET = 0x9121,
		BUFFER_MAP_POINTER = 0x88BD,
		BUFFER_SIZE = 0x8764,
		BUFFER_STORAGE_FLAGS = 0x8220,
		BUFFER_UPDATE_BARRIER_BIT = 0x00000200,
		BUFFER_USAGE = 0x8765,
		BUFFER_VARIABLE = 0x92E5,
		BYTE = 0x1400,
		CAVEAT_SUPPORT = 0x82B8,
		CCW = 0x0901,
		CLAMP_READ_COLOR = 0x891C,
		CLAMP_TO_BORDER = 0x812D,
		CLAMP_TO_EDGE = 0x812F,
		CLEAR = 0x1500,
		CLEAR_BUFFER = 0x82B4,
		CLEAR_TEXTURE = 0x9365,
		CLIENT_MAPPED_BUFFER_BARRIER_BIT = 0x00004000,
		CLIENT_STORAGE_BIT = 0x0200,
		CLIPPING_INPUT_PRIMITIVES = 0x82F6,
		CLIPPING_OUTPUT_PRIMITIVES = 0x82F7,
		CLIP_DEPTH_MODE = 0x935D,
		CLIP_DISTANCE0 = 0x3000,
		CLIP_DISTANCE1 = 0x3001,
		CLIP_DISTANCE2 = 0x3002,
		CLIP_DISTANCE3 = 0x3003,
		CLIP_DISTANCE4 = 0x3004,
		CLIP_DISTANCE5 = 0x3005,
		CLIP_DISTANCE6 = 0x3006,
		CLIP_DISTANCE7 = 0x3007,
		CLIP_ORIGIN = 0x935C,
		COLOR = 0x1800,
		COLOR_ATTACHMENT0 = 0x8CE0,
		COLOR_ATTACHMENT1 = 0x8CE1,
		COLOR_ATTACHMENT10 = 0x8CEA,
		COLOR_ATTACHMENT11 = 0x8CEB,
		COLOR_ATTACHMENT12 = 0x8CEC,
		COLOR_ATTACHMENT13 = 0x8CED,
		COLOR_ATTACHMENT14 = 0x8CEE,
		COLOR_ATTACHMENT15 = 0x8CEF,
		COLOR_ATTACHMENT16 = 0x8CF0,
		COLOR_ATTACHMENT17 = 0x8CF1,
		COLOR_ATTACHMENT18 = 0x8CF2,
		COLOR_ATTACHMENT19 = 0x8CF3,
		COLOR_ATTACHMENT2 = 0x8CE2,
		COLOR_ATTACHMENT20 = 0x8CF4,
		COLOR_ATTACHMENT21 = 0x8CF5,
		COLOR_ATTACHMENT22 = 0x8CF6,
		COLOR_ATTACHMENT23 = 0x8CF7,
		COLOR_ATTACHMENT24 = 0x8CF8,
		COLOR_ATTACHMENT25 = 0x8CF9,
		COLOR_ATTACHMENT26 = 0x8CFA,
		COLOR_ATTACHMENT27 = 0x8CFB,
		COLOR_ATTACHMENT28 = 0x8CFC,
		COLOR_ATTACHMENT29 = 0x8CFD,
		COLOR_ATTACHMENT3 = 0x8CE3,
		COLOR_ATTACHMENT30 = 0x8CFE,
		COLOR_ATTACHMENT31 = 0x8CFF,
		COLOR_ATTACHMENT4 = 0x8CE4,
		COLOR_ATTACHMENT5 = 0x8CE5,
		COLOR_ATTACHMENT6 = 0x8CE6,
		COLOR_ATTACHMENT7 = 0x8CE7,
		COLOR_ATTACHMENT8 = 0x8CE8,
		COLOR_ATTACHMENT9 = 0x8CE9,
		COLOR_BUFFER_BIT = 0x00004000,
		COLOR_CLEAR_VALUE = 0x0C22,
		COLOR_COMPONENTS = 0x8283,
		COLOR_ENCODING = 0x8296,
		COLOR_LOGIC_OP = 0x0BF2,
		COLOR_RENDERABLE = 0x8286,
		COLOR_WRITEMASK = 0x0C23,
		COMMAND_BARRIER_BIT = 0x00000040,
		COMPARE_REF_TO_TEXTURE = 0x884E,
		COMPATIBLE_SUBROUTINES = 0x8E4B,
		COMPILE_STATUS = 0x8B81,
		COMPRESSED_R11_EAC = 0x9270,
		COMPRESSED_RED = 0x8225,
		COMPRESSED_RED_RGTC1 = 0x8DBB,
		COMPRESSED_RG = 0x8226,
		COMPRESSED_RG11_EAC = 0x9272,
		COMPRESSED_RGB = 0x84ED,
		COMPRESSED_RGB8_ETC2 = 0x9274,
		COMPRESSED_RGB8_PUNCHTHROUGH_ALPHA1_ETC2 = 0x9276,
		COMPRESSED_RGBA = 0x84EE,
		COMPRESSED_RGBA8_ETC2_EAC = 0x9278,
		COMPRESSED_RGBA_BPTC_UNORM = 0x8E8C,
		COMPRESSED_RGB_BPTC_SIGNED_FLOAT = 0x8E8E,
		COMPRESSED_RGB_BPTC_UNSIGNED_FLOAT = 0x8E8F,
		COMPRESSED_RG_RGTC2 = 0x8DBD,
		COMPRESSED_SIGNED_R11_EAC = 0x9271,
		COMPRESSED_SIGNED_RED_RGTC1 = 0x8DBC,
		COMPRESSED_SIGNED_RG11_EAC = 0x9273,
		COMPRESSED_SIGNED_RG_RGTC2 = 0x8DBE,
		COMPRESSED_SRGB = 0x8C48,
		COMPRESSED_SRGB8_ALPHA8_ETC2_EAC = 0x9279,
		COMPRESSED_SRGB8_ETC2 = 0x9275,
		COMPRESSED_SRGB8_PUNCHTHROUGH_ALPHA1_ETC2 = 0x9277,
		COMPRESSED_SRGB_ALPHA = 0x8C49,
		COMPRESSED_SRGB_ALPHA_BPTC_UNORM = 0x8E8D,
		COMPRESSED_RGB_S3TC_DXT1_EXT = 0x83F0,
		COMPRESSED_RGBA_S3TC_DXT1_EXT = 0x83F1,
		COMPRESSED_RGBA_S3TC_DXT3_EXT = 0x83F2,
		COMPRESSED_RGBA_S3TC_DXT5_EXT = 0x83F3,
		COMPRESSED_SRGB_S3TC_DXT1_EXT = 0x8C4C,
		COMPRESSED_SRGB_ALPHA_S3TC_DXT1_EXT = 0x8C4D,
		COMPRESSED_SRGB_ALPHA_S3TC_DXT3_EXT = 0x8C4E,
		COMPRESSED_SRGB_ALPHA_S3TC_DXT5_EXT = 0x8C4F,
		COMPRESSED_RGBA_ASTC_4x4_KHR = 0x93B0,
		COMPRESSED_RGBA_ASTC_5x4_KHR = 0x93B1,
		COMPRESSED_RGBA_ASTC_5x5_KHR = 0x93B2,
		COMPRESSED_RGBA_ASTC_6x5_KHR = 0x93B3,
		COMPRESSED_RGBA_ASTC_6x6_KHR = 0x93B4,
		COMPRESSED_RGBA_ASTC_8x5_KHR = 0x93B5,
		COMPRESSED_RGBA_ASTC_8x6_KHR = 0x93B6,
		COMPRESSED_RGBA_ASTC_8x8_KHR = 0x93B7,
		COMPRESSED_RGBA_ASTC_10x5_KHR = 0x93B8,
		COMPRESSED_RGBA_ASTC_10x6_KHR = 0x93B9,
		COMPRESSED_RGBA_ASTC_10x8_KHR = 0x93BA,
		COMPRESSED_RGBA_ASTC_10x10_KHR = 0x93BB,
		COMPRESSED_RGBA_ASTC_12x10_KHR = 0x93BC,
		COMPRESSED_RGBA_ASTC_12x12_KHR = 0x93BD,
		COMPRESSED_SRGB8_ALPHA8_ASTC_4x4_KHR = 0x93D0,
		COMPRESSED_SRGB8_ALPHA8_ASTC_5x4_KHR = 0x93D1,
		COMPRESSED_SRGB8_ALPHA8_ASTC_5x5_KHR = 0x93D2,
		COMPRESSED_SRGB8_ALPHA8_ASTC_6x5_KHR = 0x93D3,
		COMPRESSED_SRGB8_ALPHA8_ASTC_6x6_KHR = 0x93D4,
		COMPRESSED_SRGB8_ALPHA8_ASTC_8x5_KHR = 0x93D5,
		COMPRESSED_SRGB8_ALPHA8_ASTC_8x6_KHR = 0x93D6,
		COMPRESSED_SRGB8_ALPHA8_ASTC_8x8_KHR = 0x93D7,
		COMPRESSED_SRGB8_ALPHA8_ASTC_10x5_KHR = 0x93D8,
		COMPRESSED_SRGB8_ALPHA8_ASTC_10x6_KHR = 0x93D9,
		COMPRESSED_SRGB8_ALPHA8_ASTC_10x8_KHR = 0x93DA,
		COMPRESSED_SRGB8_ALPHA8_ASTC_10x10_KHR = 0x93DB,
		COMPRESSED_SRGB8_ALPHA8_ASTC_12x10_KHR = 0x93DC,
		COMPRESSED_SRGB8_ALPHA8_ASTC_12x12_KHR = 0x93DD,
		COMPRESSED_TEXTURE_FORMATS = 0x86A3,
		COMPUTE_SHADER = 0x91B9,
		COMPUTE_SHADER_BIT = 0x00000020,
		COMPUTE_SHADER_INVOCATIONS = 0x82F5,
		COMPUTE_SUBROUTINE = 0x92ED,
		COMPUTE_SUBROUTINE_UNIFORM = 0x92F3,
		COMPUTE_TEXTURE = 0x82A0,
		COMPUTE_WORK_GROUP_SIZE = 0x8267,
		CONDITION_SATISFIED = 0x911C,
		CONSTANT_ALPHA = 0x8003,
		CONSTANT_COLOR = 0x8001,
		CONTEXT_COMPATIBILITY_PROFILE_BIT = 0x00000002,
		CONTEXT_CORE_PROFILE_BIT = 0x00000001,
		CONTEXT_FLAGS = 0x821E,
		CONTEXT_FLAG_DEBUG_BIT = 0x00000002,
		CONTEXT_FLAG_FORWARD_COMPATIBLE_BIT = 0x00000001,
		CONTEXT_FLAG_NO_ERROR_BIT = 0x00000008,
		CONTEXT_FLAG_ROBUST_ACCESS_BIT = 0x00000004,
		CONTEXT_LOST = 0x0507,
		CONTEXT_PROFILE_MASK = 0x9126,
		CONTEXT_RELEASE_BEHAVIOR = 0x82FB,
		CONTEXT_RELEASE_BEHAVIOR_FLUSH = 0x82FC,
		COPY = 0x1503,
		COPY_INVERTED = 0x150C,
		COPY_READ_BUFFER = 0x8F36,
		COPY_READ_BUFFER_BINDING = 0x8F36,
		COPY_WRITE_BUFFER = 0x8F37,
		COPY_WRITE_BUFFER_BINDING = 0x8F37,
		CULL_FACE = 0x0B44,
		CULL_FACE_MODE = 0x0B45,
		CURRENT_PROGRAM = 0x8B8D,
		CURRENT_QUERY = 0x8865,
		CURRENT_VERTEX_ATTRIB = 0x8626,
		CW = 0x0900,
		DEBUG_CALLBACK_FUNCTION = 0x8244,
		DEBUG_CALLBACK_USER_PARAM = 0x8245,
		DEBUG_GROUP_STACK_DEPTH = 0x826D,
		DEBUG_LOGGED_MESSAGES = 0x9145,
		DEBUG_NEXT_LOGGED_MESSAGE_LENGTH = 0x8243,
		DEBUG_OUTPUT = 0x92E0,
		DEBUG_OUTPUT_SYNCHRONOUS = 0x8242,
		DEBUG_SEVERITY_HIGH = 0x9146,
		DEBUG_SEVERITY_LOW = 0x9148,
		DEBUG_SEVERITY_MEDIUM = 0x9147,
		DEBUG_SEVERITY_NOTIFICATION = 0x826B,
		DEBUG_SOURCE_API = 0x8246,
		DEBUG_SOURCE_APPLICATION = 0x824A,
		DEBUG_SOURCE_OTHER = 0x824B,
		DEBUG_SOURCE_SHADER_COMPILER = 0x8248,
		DEBUG_SOURCE_THIRD_PARTY = 0x8249,
		DEBUG_SOURCE_WINDOW_SYSTEM = 0x8247,
		DEBUG_TYPE_DEPRECATED_BEHAVIOR = 0x824D,
		DEBUG_TYPE_ERROR = 0x824C,
		DEBUG_TYPE_MARKER = 0x8268,
		DEBUG_TYPE_OTHER = 0x8251,
		DEBUG_TYPE_PERFORMANCE = 0x8250,
		DEBUG_TYPE_POP_GROUP = 0x826A,
		DEBUG_TYPE_PORTABILITY = 0x824F,
		DEBUG_TYPE_PUSH_GROUP = 0x8269,
		DEBUG_TYPE_UNDEFINED_BEHAVIOR = 0x824E,
		DECR = 0x1E03,
		DECR_WRAP = 0x8508,
		DELETE_STATUS = 0x8B80,
		DEPTH = 0x1801,
		DEPTH24_STENCIL8 = 0x88F0,
		DEPTH32F_STENCIL8 = 0x8CAD,
		DEPTH_ATTACHMENT = 0x8D00,
		DEPTH_BUFFER_BIT = 0x00000100,
		DEPTH_CLAMP = 0x864F,
		DEPTH_CLEAR_VALUE = 0x0B73,
		DEPTH_COMPONENT = 0x1902,
		DEPTH_COMPONENT16 = 0x81A5,
		DEPTH_COMPONENT24 = 0x81A6,
		DEPTH_COMPONENT32 = 0x81A7,
		DEPTH_COMPONENT32F = 0x8CAC,
		DEPTH_COMPONENTS = 0x8284,
		DEPTH_FUNC = 0x0B74,
		DEPTH_RANGE = 0x0B70,
		DEPTH_RENDERABLE = 0x8287,
		DEPTH_STENCIL = 0x84F9,
		DEPTH_STENCIL_ATTACHMENT = 0x821A,
		DEPTH_STENCIL_TEXTURE_MODE = 0x90EA,
		DEPTH_TEST = 0x0B71,
		DEPTH_WRITEMASK = 0x0B72,
		DISPATCH_INDIRECT_BUFFER = 0x90EE,
		DISPATCH_INDIRECT_BUFFER_BINDING = 0x90EF,
		DISPLAY_LIST = 0x82E7,
		DITHER = 0x0BD0,
		DONT_CARE = 0x1100,
		DOUBLE = 0x140A,
		DOUBLEBUFFER = 0x0C32,
		DOUBLE_MAT2 = 0x8F46,
		DOUBLE_MAT2x3 = 0x8F49,
		DOUBLE_MAT2x4 = 0x8F4A,
		DOUBLE_MAT3 = 0x8F47,
		DOUBLE_MAT3x2 = 0x8F4B,
		DOUBLE_MAT3x4 = 0x8F4C,
		DOUBLE_MAT4 = 0x8F48,
		DOUBLE_MAT4x2 = 0x8F4D,
		DOUBLE_MAT4x3 = 0x8F4E,
		DOUBLE_VEC2 = 0x8FFC,
		DOUBLE_VEC3 = 0x8FFD,
		DOUBLE_VEC4 = 0x8FFE,
		DRAW_BUFFER = 0x0C01,
		DRAW_BUFFER0 = 0x8825,
		DRAW_BUFFER1 = 0x8826,
		DRAW_BUFFER10 = 0x882F,
		DRAW_BUFFER11 = 0x8830,
		DRAW_BUFFER12 = 0x8831,
		DRAW_BUFFER13 = 0x8832,
		DRAW_BUFFER14 = 0x8833,
		DRAW_BUFFER15 = 0x8834,
		DRAW_BUFFER2 = 0x8827,
		DRAW_BUFFER3 = 0x8828,
		DRAW_BUFFER4 = 0x8829,
		DRAW_BUFFER5 = 0x882A,
		DRAW_BUFFER6 = 0x882B,
		DRAW_BUFFER7 = 0x882C,
		DRAW_BUFFER8 = 0x882D,
		DRAW_BUFFER9 = 0x882E,
		DRAW_FRAMEBUFFER = 0x8CA9,
		DRAW_FRAMEBUFFER_BINDING = 0x8CA6,
		DRAW_INDIRECT_BUFFER = 0x8F3F,
		DRAW_INDIRECT_BUFFER_BINDING = 0x8F43,
		DST_ALPHA = 0x0304,
		DST_COLOR = 0x0306,
		DYNAMIC_COPY = 0x88EA,
		DYNAMIC_DRAW = 0x88E8,
		DYNAMIC_READ = 0x88E9,
		DYNAMIC_STORAGE_BIT = 0x0100,
		ELEMENT_ARRAY_BARRIER_BIT = 0x00000002,
		ELEMENT_ARRAY_BUFFER = 0x8893,
		ELEMENT_ARRAY_BUFFER_BINDING = 0x8895,
		EQUAL = 0x0202,
		EQUIV = 0x1509,
		EXTENSIONS = 0x1F03,
		FALSE = 0,
		FASTEST = 0x1101,
		FILL = 0x1B02,
		FILTER = 0x829A,
		FIRST_VERTEX_CONVENTION = 0x8E4D,
		FIXED = 0x140C,
		FIXED_ONLY = 0x891D,
		FLOAT = 0x1406,
		FLOAT_32_UNSIGNED_INT_24_8_REV = 0x8DAD,
		FLOAT_MAT2 = 0x8B5A,
		FLOAT_MAT2x3 = 0x8B65,
		FLOAT_MAT2x4 = 0x8B66,
		FLOAT_MAT3 = 0x8B5B,
		FLOAT_MAT3x2 = 0x8B67,
		FLOAT_MAT3x4 = 0x8B68,
		FLOAT_MAT4 = 0x8B5C,
		FLOAT_MAT4x2 = 0x8B69,
		FLOAT_MAT4x3 = 0x8B6A,
		FLOAT_VEC2 = 0x8B50,
		FLOAT_VEC3 = 0x8B51,
		FLOAT_VEC4 = 0x8B52,
		FRACTIONAL_EVEN = 0x8E7C,
		FRACTIONAL_ODD = 0x8E7B,
		FRAGMENT_INTERPOLATION_OFFSET_BITS = 0x8E5D,
		FRAGMENT_SHADER = 0x8B30,
		FRAGMENT_SHADER_BIT = 0x00000002,
		FRAGMENT_SHADER_DERIVATIVE_HINT = 0x8B8B,
		FRAGMENT_SHADER_INVOCATIONS = 0x82F4,
		FRAGMENT_SUBROUTINE = 0x92EC,
		FRAGMENT_SUBROUTINE_UNIFORM = 0x92F2,
		FRAGMENT_TEXTURE = 0x829F,
		FRAMEBUFFER = 0x8D40,
		FRAMEBUFFER_ATTACHMENT_ALPHA_SIZE = 0x8215,
		FRAMEBUFFER_ATTACHMENT_BLUE_SIZE = 0x8214,
		FRAMEBUFFER_ATTACHMENT_COLOR_ENCODING = 0x8210,
		FRAMEBUFFER_ATTACHMENT_COMPONENT_TYPE = 0x8211,
		FRAMEBUFFER_ATTACHMENT_DEPTH_SIZE = 0x8216,
		FRAMEBUFFER_ATTACHMENT_GREEN_SIZE = 0x8213,
		FRAMEBUFFER_ATTACHMENT_LAYERED = 0x8DA7,
		FRAMEBUFFER_ATTACHMENT_OBJECT_NAME = 0x8CD1,
		FRAMEBUFFER_ATTACHMENT_OBJECT_TYPE = 0x8CD0,
		FRAMEBUFFER_ATTACHMENT_RED_SIZE = 0x8212,
		FRAMEBUFFER_ATTACHMENT_STENCIL_SIZE = 0x8217,
		FRAMEBUFFER_ATTACHMENT_TEXTURE_CUBE_MAP_FACE = 0x8CD3,
		FRAMEBUFFER_ATTACHMENT_TEXTURE_LAYER = 0x8CD4,
		FRAMEBUFFER_ATTACHMENT_TEXTURE_LEVEL = 0x8CD2,
		FRAMEBUFFER_BARRIER_BIT = 0x00000400,
		FRAMEBUFFER_BINDING = 0x8CA6,
		FRAMEBUFFER_BLEND = 0x828B,
		FRAMEBUFFER_COMPLETE = 0x8CD5,
		FRAMEBUFFER_DEFAULT = 0x8218,
		FRAMEBUFFER_DEFAULT_FIXED_SAMPLE_LOCATIONS = 0x9314,
		FRAMEBUFFER_DEFAULT_HEIGHT = 0x9311,
		FRAMEBUFFER_DEFAULT_LAYERS = 0x9312,
		FRAMEBUFFER_DEFAULT_SAMPLES = 0x9313,
		FRAMEBUFFER_DEFAULT_WIDTH = 0x9310,
		FRAMEBUFFER_INCOMPLETE_ATTACHMENT = 0x8CD6,
		FRAMEBUFFER_INCOMPLETE_DIMENSIONS = 0x8CD9,
		FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER = 0x8CDB,
		FRAMEBUFFER_INCOMPLETE_LAYER_TARGETS = 0x8DA8,
		FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT = 0x8CD7,
		FRAMEBUFFER_INCOMPLETE_MULTISAMPLE = 0x8D56,
		FRAMEBUFFER_INCOMPLETE_READ_BUFFER = 0x8CDC,
		FRAMEBUFFER_RENDERABLE = 0x8289,
		FRAMEBUFFER_RENDERABLE_LAYERED = 0x828A,
		FRAMEBUFFER_SRGB = 0x8DB9,
		FRAMEBUFFER_UNDEFINED = 0x8219,
		FRAMEBUFFER_UNSUPPORTED = 0x8CDD,
		FRONT = 0x0404,
		FRONT_AND_BACK = 0x0408,
		FRONT_FACE = 0x0B46,
		FRONT_LEFT = 0x0400,
		FRONT_RIGHT = 0x0401,
		FULL_SUPPORT = 0x82B7,
		FUNC_ADD = 0x8006,
		FUNC_REVERSE_SUBTRACT = 0x800B,
		FUNC_SUBTRACT = 0x800A,
		GEOMETRY_INPUT_TYPE = 0x8917,
		GEOMETRY_OUTPUT_TYPE = 0x8918,
		GEOMETRY_SHADER = 0x8DD9,
		GEOMETRY_SHADER_BIT = 0x00000004,
		GEOMETRY_SHADER_INVOCATIONS = 0x887F,
		GEOMETRY_SHADER_PRIMITIVES_EMITTED = 0x82F3,
		GEOMETRY_SUBROUTINE = 0x92EB,
		GEOMETRY_SUBROUTINE_UNIFORM = 0x92F1,
		GEOMETRY_TEXTURE = 0x829E,
		GEOMETRY_VERTICES_OUT = 0x8916,
		GEQUAL = 0x0206,
		GET_TEXTURE_IMAGE_FORMAT = 0x8291,
		GET_TEXTURE_IMAGE_TYPE = 0x8292,
		GREATER = 0x0204,
		GREEN = 0x1904,
		GREEN_INTEGER = 0x8D95,
		GUILTY_CONTEXT_RESET = 0x8253,
		HALF_FLOAT = 0x140B,
		HIGH_FLOAT = 0x8DF2,
		HIGH_INT = 0x8DF5,
		IMAGE_1D = 0x904C,
		IMAGE_1D_ARRAY = 0x9052,
		IMAGE_2D = 0x904D,
		IMAGE_2D_ARRAY = 0x9053,
		IMAGE_2D_MULTISAMPLE = 0x9055,
		IMAGE_2D_MULTISAMPLE_ARRAY = 0x9056,
		IMAGE_2D_RECT = 0x904F,
		IMAGE_3D = 0x904E,
		IMAGE_BINDING_ACCESS = 0x8F3E,
		IMAGE_BINDING_FORMAT = 0x906E,
		IMAGE_BINDING_LAYER = 0x8F3D,
		IMAGE_BINDING_LAYERED = 0x8F3C,
		IMAGE_BINDING_LEVEL = 0x8F3B,
		IMAGE_BINDING_NAME = 0x8F3A,
		IMAGE_BUFFER = 0x9051,
		IMAGE_CLASS_10_10_10_2 = 0x82C3,
		IMAGE_CLASS_11_11_10 = 0x82C2,
		IMAGE_CLASS_1_X_16 = 0x82BE,
		IMAGE_CLASS_1_X_32 = 0x82BB,
		IMAGE_CLASS_1_X_8 = 0x82C1,
		IMAGE_CLASS_2_X_16 = 0x82BD,
		IMAGE_CLASS_2_X_32 = 0x82BA,
		IMAGE_CLASS_2_X_8 = 0x82C0,
		IMAGE_CLASS_4_X_16 = 0x82BC,
		IMAGE_CLASS_4_X_32 = 0x82B9,
		IMAGE_CLASS_4_X_8 = 0x82BF,
		IMAGE_COMPATIBILITY_CLASS = 0x82A8,
		IMAGE_CUBE = 0x9050,
		IMAGE_CUBE_MAP_ARRAY = 0x9054,
		IMAGE_FORMAT_COMPATIBILITY_BY_CLASS = 0x90C9,
		IMAGE_FORMAT_COMPATIBILITY_BY_SIZE = 0x90C8,
		IMAGE_FORMAT_COMPATIBILITY_TYPE = 0x90C7,
		IMAGE_PIXEL_FORMAT = 0x82A9,
		IMAGE_PIXEL_TYPE = 0x82AA,
		IMAGE_TEXEL_SIZE = 0x82A7,
		IMPLEMENTATION_COLOR_READ_FORMAT = 0x8B9B,
		IMPLEMENTATION_COLOR_READ_TYPE = 0x8B9A,
		INCR = 0x1E02,
		INCR_WRAP = 0x8507,
		INDEX = 0x8222,
		INFO_LOG_LENGTH = 0x8B84,
		INNOCENT_CONTEXT_RESET = 0x8254,
		INT = 0x1404,
		INTERLEAVED_ATTRIBS = 0x8C8C,
		INTERNALFORMAT_ALPHA_SIZE = 0x8274,
		INTERNALFORMAT_ALPHA_TYPE = 0x827B,
		INTERNALFORMAT_BLUE_SIZE = 0x8273,
		INTERNALFORMAT_BLUE_TYPE = 0x827A,
		INTERNALFORMAT_DEPTH_SIZE = 0x8275,
		INTERNALFORMAT_DEPTH_TYPE = 0x827C,
		INTERNALFORMAT_GREEN_SIZE = 0x8272,
		INTERNALFORMAT_GREEN_TYPE = 0x8279,
		INTERNALFORMAT_PREFERRED = 0x8270,
		INTERNALFORMAT_RED_SIZE = 0x8271,
		INTERNALFORMAT_RED_TYPE = 0x8278,
		INTERNALFORMAT_SHARED_SIZE = 0x8277,
		INTERNALFORMAT_STENCIL_SIZE = 0x8276,
		INTERNALFORMAT_STENCIL_TYPE = 0x827D,
		INTERNALFORMAT_SUPPORTED = 0x826F,
		INT_2_10_10_10_REV = 0x8D9F,
		INT_IMAGE_1D = 0x9057,
		INT_IMAGE_1D_ARRAY = 0x905D,
		INT_IMAGE_2D = 0x9058,
		INT_IMAGE_2D_ARRAY = 0x905E,
		INT_IMAGE_2D_MULTISAMPLE = 0x9060,
		INT_IMAGE_2D_MULTISAMPLE_ARRAY = 0x9061,
		INT_IMAGE_2D_RECT = 0x905A,
		INT_IMAGE_3D = 0x9059,
		INT_IMAGE_BUFFER = 0x905C,
		INT_IMAGE_CUBE = 0x905B,
		INT_IMAGE_CUBE_MAP_ARRAY = 0x905F,
		INT_SAMPLER_1D = 0x8DC9,
		INT_SAMPLER_1D_ARRAY = 0x8DCE,
		INT_SAMPLER_2D = 0x8DCA,
		INT_SAMPLER_2D_ARRAY = 0x8DCF,
		INT_SAMPLER_2D_MULTISAMPLE = 0x9109,
		INT_SAMPLER_2D_MULTISAMPLE_ARRAY = 0x910C,
		INT_SAMPLER_2D_RECT = 0x8DCD,
		INT_SAMPLER_3D = 0x8DCB,
		INT_SAMPLER_BUFFER = 0x8DD0,
		INT_SAMPLER_CUBE = 0x8DCC,
		INT_SAMPLER_CUBE_MAP_ARRAY = 0x900E,
		INT_VEC2 = 0x8B53,
		INT_VEC3 = 0x8B54,
		INT_VEC4 = 0x8B55,
		INVALID_ENUM = 0x0500,
		INVALID_FRAMEBUFFER_OPERATION = 0x0506,
		INVALID_INDEX = 0xFFFFFFFF,
		INVALID_OPERATION = 0x0502,
		INVALID_VALUE = 0x0501,
		INVERT = 0x150A,
		ISOLINES = 0x8E7A,
		IS_PER_PATCH = 0x92E7,
		IS_ROW_MAJOR = 0x9300,
		KEEP = 0x1E00,
		LAST_VERTEX_CONVENTION = 0x8E4E,
		LAYER_PROVOKING_VERTEX = 0x825E,
		LEFT = 0x0406,
		LEQUAL = 0x0203,
		LESS = 0x0201,
		LINE = 0x1B01,
		LINEAR = 0x2601,
		LINEAR_MIPMAP_LINEAR = 0x2703,
		LINEAR_MIPMAP_NEAREST = 0x2701,
		LINES = 0x0001,
		LINES_ADJACENCY = 0x000A,
		LINE_LOOP = 0x0002,
		LINE_SMOOTH = 0x0B20,
		LINE_SMOOTH_HINT = 0x0C52,
		LINE_STRIP = 0x0003,
		LINE_STRIP_ADJACENCY = 0x000B,
		LINE_WIDTH = 0x0B21,
		LINE_WIDTH_GRANULARITY = 0x0B23,
		LINE_WIDTH_RANGE = 0x0B22,
		LINK_STATUS = 0x8B82,
		LOCATION = 0x930E,
		LOCATION_COMPONENT = 0x934A,
		LOCATION_INDEX = 0x930F,
		LOGIC_OP_MODE = 0x0BF0,
		LOSE_CONTEXT_ON_RESET = 0x8252,
		LOWER_LEFT = 0x8CA1,
		LOW_FLOAT = 0x8DF0,
		LOW_INT = 0x8DF3,
		LUMINANCE = 0x1909,
		LUMINANCE_ALPHA = 0x190A,
		MAJOR_VERSION = 0x821B,
		MANUAL_GENERATE_MIPMAP = 0x8294,
		MAP_COHERENT_BIT = 0x0080,
		MAP_FLUSH_EXPLICIT_BIT = 0x0010,
		MAP_INVALIDATE_BUFFER_BIT = 0x0008,
		MAP_INVALIDATE_RANGE_BIT = 0x0004,
		MAP_PERSISTENT_BIT = 0x0040,
		MAP_READ_BIT = 0x0001,
		MAP_UNSYNCHRONIZED_BIT = 0x0020,
		MAP_WRITE_BIT = 0x0002,
		MATRIX_STRIDE = 0x92FF,
		MAX = 0x8008,
		MAX_3D_TEXTURE_SIZE = 0x8073,
		MAX_ARRAY_TEXTURE_LAYERS = 0x88FF,
		MAX_ATOMIC_COUNTER_BUFFER_BINDINGS = 0x92DC,
		MAX_ATOMIC_COUNTER_BUFFER_SIZE = 0x92D8,
		MAX_CLIP_DISTANCES = 0x0D32,
		MAX_COLOR_ATTACHMENTS = 0x8CDF,
		MAX_COLOR_TEXTURE_SAMPLES = 0x910E,
		MAX_COMBINED_ATOMIC_COUNTERS = 0x92D7,
		MAX_COMBINED_ATOMIC_COUNTER_BUFFERS = 0x92D1,
		MAX_COMBINED_CLIP_AND_CULL_DISTANCES = 0x82FA,
		MAX_COMBINED_COMPUTE_UNIFORM_COMPONENTS = 0x8266,
		MAX_COMBINED_DIMENSIONS = 0x8282,
		MAX_COMBINED_FRAGMENT_UNIFORM_COMPONENTS = 0x8A33,
		MAX_COMBINED_GEOMETRY_UNIFORM_COMPONENTS = 0x8A32,
		MAX_COMBINED_IMAGE_UNIFORMS = 0x90CF,
		MAX_COMBINED_IMAGE_UNITS_AND_FRAGMENT_OUTPUTS = 0x8F39,
		MAX_COMBINED_SHADER_OUTPUT_RESOURCES = 0x8F39,
		MAX_COMBINED_SHADER_STORAGE_BLOCKS = 0x90DC,
		MAX_COMBINED_TESS_CONTROL_UNIFORM_COMPONENTS = 0x8E1E,
		MAX_COMBINED_TESS_EVALUATION_UNIFORM_COMPONENTS = 0x8E1F,
		MAX_COMBINED_TEXTURE_IMAGE_UNITS = 0x8B4D,
		MAX_COMBINED_UNIFORM_BLOCKS = 0x8A2E,
		MAX_COMBINED_VERTEX_UNIFORM_COMPONENTS = 0x8A31,
		MAX_COMPUTE_ATOMIC_COUNTERS = 0x8265,
		MAX_COMPUTE_ATOMIC_COUNTER_BUFFERS = 0x8264,
		MAX_COMPUTE_IMAGE_UNIFORMS = 0x91BD,
		MAX_COMPUTE_SHADER_STORAGE_BLOCKS = 0x90DB,
		MAX_COMPUTE_SHARED_MEMORY_SIZE = 0x8262,
		MAX_COMPUTE_TEXTURE_IMAGE_UNITS = 0x91BC,
		MAX_COMPUTE_UNIFORM_BLOCKS = 0x91BB,
		MAX_COMPUTE_UNIFORM_COMPONENTS = 0x8263,
		MAX_COMPUTE_WORK_GROUP_COUNT = 0x91BE,
		MAX_COMPUTE_WORK_GROUP_INVOCATIONS = 0x90EB,
		MAX_COMPUTE_WORK_GROUP_SIZE = 0x91BF,
		MAX_CUBE_MAP_TEXTURE_SIZE = 0x851C,
		MAX_CULL_DISTANCES = 0x82F9,
		MAX_DEBUG_GROUP_STACK_DEPTH = 0x826C,
		MAX_DEBUG_LOGGED_MESSAGES = 0x9144,
		MAX_DEBUG_MESSAGE_LENGTH = 0x9143,
		MAX_DEPTH = 0x8280,
		MAX_DEPTH_TEXTURE_SAMPLES = 0x910F,
		MAX_DRAW_BUFFERS = 0x8824,
		MAX_DUAL_SOURCE_DRAW_BUFFERS = 0x88FC,
		MAX_ELEMENTS_INDICES = 0x80E9,
		MAX_ELEMENTS_VERTICES = 0x80E8,
		MAX_ELEMENT_INDEX = 0x8D6B,
		MAX_FRAGMENT_ATOMIC_COUNTERS = 0x92D6,
		MAX_FRAGMENT_ATOMIC_COUNTER_BUFFERS = 0x92D0,
		MAX_FRAGMENT_IMAGE_UNIFORMS = 0x90CE,
		MAX_FRAGMENT_INPUT_COMPONENTS = 0x9125,
		MAX_FRAGMENT_INTERPOLATION_OFFSET = 0x8E5C,
		MAX_FRAGMENT_SHADER_STORAGE_BLOCKS = 0x90DA,
		MAX_FRAGMENT_UNIFORM_BLOCKS = 0x8A2D,
		MAX_FRAGMENT_UNIFORM_COMPONENTS = 0x8B49,
		MAX_FRAGMENT_UNIFORM_VECTORS = 0x8DFD,
		MAX_FRAMEBUFFER_HEIGHT = 0x9316,
		MAX_FRAMEBUFFER_LAYERS = 0x9317,
		MAX_FRAMEBUFFER_SAMPLES = 0x9318,
		MAX_FRAMEBUFFER_WIDTH = 0x9315,
		MAX_GEOMETRY_ATOMIC_COUNTERS = 0x92D5,
		MAX_GEOMETRY_ATOMIC_COUNTER_BUFFERS = 0x92CF,
		MAX_GEOMETRY_IMAGE_UNIFORMS = 0x90CD,
		MAX_GEOMETRY_INPUT_COMPONENTS = 0x9123,
		MAX_GEOMETRY_OUTPUT_COMPONENTS = 0x9124,
		MAX_GEOMETRY_OUTPUT_VERTICES = 0x8DE0,
		MAX_GEOMETRY_SHADER_INVOCATIONS = 0x8E5A,
		MAX_GEOMETRY_SHADER_STORAGE_BLOCKS = 0x90D7,
		MAX_GEOMETRY_TEXTURE_IMAGE_UNITS = 0x8C29,
		MAX_GEOMETRY_TOTAL_OUTPUT_COMPONENTS = 0x8DE1,
		MAX_GEOMETRY_UNIFORM_BLOCKS = 0x8A2C,
		MAX_GEOMETRY_UNIFORM_COMPONENTS = 0x8DDF,
		MAX_HEIGHT = 0x827F,
		MAX_IMAGE_SAMPLES = 0x906D,
		MAX_IMAGE_UNITS = 0x8F38,
		MAX_INTEGER_SAMPLES = 0x9110,
		MAX_LABEL_LENGTH = 0x82E8,
		MAX_LAYERS = 0x8281,
		MAX_NAME_LENGTH = 0x92F6,
		MAX_NUM_ACTIVE_VARIABLES = 0x92F7,
		MAX_NUM_COMPATIBLE_SUBROUTINES = 0x92F8,
		MAX_PATCH_VERTICES = 0x8E7D,
		MAX_PROGRAM_TEXEL_OFFSET = 0x8905,
		MAX_PROGRAM_TEXTURE_GATHER_OFFSET = 0x8E5F,
		MAX_RECTANGLE_TEXTURE_SIZE = 0x84F8,
		MAX_RENDERBUFFER_SIZE = 0x84E8,
		MAX_SAMPLES = 0x8D57,
		MAX_SAMPLE_MASK_WORDS = 0x8E59,
		MAX_SERVER_WAIT_TIMEOUT = 0x9111,
		MAX_SHADER_STORAGE_BLOCK_SIZE = 0x90DE,
		MAX_SHADER_STORAGE_BUFFER_BINDINGS = 0x90DD,
		MAX_SUBROUTINES = 0x8DE7,
		MAX_SUBROUTINE_UNIFORM_LOCATIONS = 0x8DE8,
		MAX_TESS_CONTROL_ATOMIC_COUNTERS = 0x92D3,
		MAX_TESS_CONTROL_ATOMIC_COUNTER_BUFFERS = 0x92CD,
		MAX_TESS_CONTROL_IMAGE_UNIFORMS = 0x90CB,
		MAX_TESS_CONTROL_INPUT_COMPONENTS = 0x886C,
		MAX_TESS_CONTROL_OUTPUT_COMPONENTS = 0x8E83,
		MAX_TESS_CONTROL_SHADER_STORAGE_BLOCKS = 0x90D8,
		MAX_TESS_CONTROL_TEXTURE_IMAGE_UNITS = 0x8E81,
		MAX_TESS_CONTROL_TOTAL_OUTPUT_COMPONENTS = 0x8E85,
		MAX_TESS_CONTROL_UNIFORM_BLOCKS = 0x8E89,
		MAX_TESS_CONTROL_UNIFORM_COMPONENTS = 0x8E7F,
		MAX_TESS_EVALUATION_ATOMIC_COUNTERS = 0x92D4,
		MAX_TESS_EVALUATION_ATOMIC_COUNTER_BUFFERS = 0x92CE,
		MAX_TESS_EVALUATION_IMAGE_UNIFORMS = 0x90CC,
		MAX_TESS_EVALUATION_INPUT_COMPONENTS = 0x886D,
		MAX_TESS_EVALUATION_OUTPUT_COMPONENTS = 0x8E86,
		MAX_TESS_EVALUATION_SHADER_STORAGE_BLOCKS = 0x90D9,
		MAX_TESS_EVALUATION_TEXTURE_IMAGE_UNITS = 0x8E82,
		MAX_TESS_EVALUATION_UNIFORM_BLOCKS = 0x8E8A,
		MAX_TESS_EVALUATION_UNIFORM_COMPONENTS = 0x8E80,
		MAX_TESS_GEN_LEVEL = 0x8E7E,
		MAX_TESS_PATCH_COMPONENTS = 0x8E84,
		MAX_TEXTURE_BUFFER_SIZE = 0x8C2B,
		MAX_TEXTURE_IMAGE_UNITS = 0x8872,
		MAX_TEXTURE_LOD_BIAS = 0x84FD,
		MAX_TEXTURE_MAX_ANISOTROPY = 0x84FF,
		MAX_TEXTURE_MAX_ANISOTROPY_EXT = 0x84FF,
		MAX_TEXTURE_SIZE = 0x0D33,
		MAX_TRANSFORM_FEEDBACK_BUFFERS = 0x8E70,
		MAX_TRANSFORM_FEEDBACK_INTERLEAVED_COMPONENTS = 0x8C8A,
		MAX_TRANSFORM_FEEDBACK_SEPARATE_ATTRIBS = 0x8C8B,
		MAX_TRANSFORM_FEEDBACK_SEPARATE_COMPONENTS = 0x8C80,
		MAX_UNIFORM_BLOCK_SIZE = 0x8A30,
		MAX_UNIFORM_BUFFER_BINDINGS = 0x8A2F,
		MAX_UNIFORM_LOCATIONS = 0x826E,
		MAX_VARYING_COMPONENTS = 0x8B4B,
		MAX_VARYING_FLOATS = 0x8B4B,
		MAX_VARYING_VECTORS = 0x8DFC,
		MAX_VERTEX_ATOMIC_COUNTERS = 0x92D2,
		MAX_VERTEX_ATOMIC_COUNTER_BUFFERS = 0x92CC,
		MAX_VERTEX_ATTRIBS = 0x8869,
		MAX_VERTEX_ATTRIB_BINDINGS = 0x82DA,
		MAX_VERTEX_ATTRIB_RELATIVE_OFFSET = 0x82D9,
		MAX_VERTEX_ATTRIB_STRIDE = 0x82E5,
		MAX_VERTEX_IMAGE_UNIFORMS = 0x90CA,
		MAX_VERTEX_OUTPUT_COMPONENTS = 0x9122,
		MAX_VERTEX_SHADER_STORAGE_BLOCKS = 0x90D6,
		MAX_VERTEX_STREAMS = 0x8E71,
		MAX_VERTEX_TEXTURE_IMAGE_UNITS = 0x8B4C,
		MAX_VERTEX_UNIFORM_BLOCKS = 0x8A2B,
		MAX_VERTEX_UNIFORM_COMPONENTS = 0x8B4A,
		MAX_VERTEX_UNIFORM_VECTORS = 0x8DFB,
		MAX_VIEWPORTS = 0x825B,
		MAX_VIEWPORT_DIMS = 0x0D3A,
		MAX_WIDTH = 0x827E,
		MEDIUM_FLOAT = 0x8DF1,
		MEDIUM_INT = 0x8DF4,
		MIN = 0x8007,
		MINOR_VERSION = 0x821C,
		MIN_FRAGMENT_INTERPOLATION_OFFSET = 0x8E5B,
		MIN_MAP_BUFFER_ALIGNMENT = 0x90BC,
		MIN_PROGRAM_TEXEL_OFFSET = 0x8904,
		MIN_PROGRAM_TEXTURE_GATHER_OFFSET = 0x8E5E,
		MIN_SAMPLE_SHADING_VALUE = 0x8C37,
		MIPMAP = 0x8293,
		MIRRORED_REPEAT = 0x8370,
		MIRROR_CLAMP_TO_EDGE = 0x8743,
		MULTISAMPLE = 0x809D,
		NAME_LENGTH = 0x92F9,
		NAND = 0x150E,
		NEAREST = 0x2600,
		NEAREST_MIPMAP_LINEAR = 0x2702,
		NEAREST_MIPMAP_NEAREST = 0x2700,
		NEGATIVE_ONE_TO_ONE = 0x935E,
		NEVER = 0x0200,
		NICEST = 0x1102,
		NONE = 0,
		NOOP = 0x1505,
		NOR = 0x1508,
		NOTEQUAL = 0x0205,
		NO_ERROR = 0,
		NO_RESET_NOTIFICATION = 0x8261,
		NUM_ACTIVE_VARIABLES = 0x9304,
		NUM_COMPATIBLE_SUBROUTINES = 0x8E4A,
		NUM_COMPRESSED_TEXTURE_FORMATS = 0x86A2,
		NUM_EXTENSIONS = 0x821D,
		NUM_PROGRAM_BINARY_FORMATS = 0x87FE,
		NUM_SAMPLE_COUNTS = 0x9380,
		NUM_SHADER_BINARY_FORMATS = 0x8DF9,
		NUM_SHADING_LANGUAGE_VERSIONS = 0x82E9,
		NUM_SPIR_V_EXTENSIONS = 0x9554,
		OBJECT_TYPE = 0x9112,
		OFFSET = 0x92FC,
		ONE = 1,
		ONE_MINUS_CONSTANT_ALPHA = 0x8004,
		ONE_MINUS_CONSTANT_COLOR = 0x8002,
		ONE_MINUS_DST_ALPHA = 0x0305,
		ONE_MINUS_DST_COLOR = 0x0307,
		ONE_MINUS_SRC1_ALPHA = 0x88FB,
		ONE_MINUS_SRC1_COLOR = 0x88FA,
		ONE_MINUS_SRC_ALPHA = 0x0303,
		ONE_MINUS_SRC_COLOR = 0x0301,
		OR = 0x1507,
		OR_INVERTED = 0x150D,
		OR_REVERSE = 0x150B,
		OUT_OF_MEMORY = 0x0505,
		PACK_ALIGNMENT = 0x0D05,
		PACK_COMPRESSED_BLOCK_DEPTH = 0x912D,
		PACK_COMPRESSED_BLOCK_HEIGHT = 0x912C,
		PACK_COMPRESSED_BLOCK_SIZE = 0x912E,
		PACK_COMPRESSED_BLOCK_WIDTH = 0x912B,
		PACK_IMAGE_HEIGHT = 0x806C,
		PACK_LSB_FIRST = 0x0D01,
		PACK_ROW_LENGTH = 0x0D02,
		PACK_SKIP_IMAGES = 0x806B,
		PACK_SKIP_PIXELS = 0x0D04,
		PACK_SKIP_ROWS = 0x0D03,
		PACK_SWAP_BYTES = 0x0D00,
		PARAMETER_BUFFER = 0x80EE,
		PARAMETER_BUFFER_BINDING = 0x80EF,
		PATCHES = 0x000E,
		PATCH_DEFAULT_INNER_LEVEL = 0x8E73,
		PATCH_DEFAULT_OUTER_LEVEL = 0x8E74,
		PATCH_VERTICES = 0x8E72,
		PIXEL_BUFFER_BARRIER_BIT = 0x00000080,
		PIXEL_PACK_BUFFER = 0x88EB,
		PIXEL_PACK_BUFFER_BINDING = 0x88ED,
		PIXEL_UNPACK_BUFFER = 0x88EC,
		PIXEL_UNPACK_BUFFER_BINDING = 0x88EF,
		POINT = 0x1B00,
		POINTS = 0x0000,
		POINT_FADE_THRESHOLD_SIZE = 0x8128,
		POINT_SIZE = 0x0B11,
		POINT_SIZE_GRANULARITY = 0x0B13,
		POINT_SIZE_RANGE = 0x0B12,
		POINT_SPRITE_COORD_ORIGIN = 0x8CA0,
		POLYGON_MODE = 0x0B40,
		POLYGON_OFFSET_CLAMP = 0x8E1B,
		POLYGON_OFFSET_FACTOR = 0x8038,
		POLYGON_OFFSET_FILL = 0x8037,
		POLYGON_OFFSET_LINE = 0x2A02,
		POLYGON_OFFSET_POINT = 0x2A01,
		POLYGON_OFFSET_UNITS = 0x2A00,
		POLYGON_SMOOTH = 0x0B41,
		POLYGON_SMOOTH_HINT = 0x0C53,
		PRIMITIVES_GENERATED = 0x8C87,
		PRIMITIVES_SUBMITTED = 0x82EF,
		PRIMITIVE_RESTART = 0x8F9D,
		PRIMITIVE_RESTART_FIXED_INDEX = 0x8D69,
		PRIMITIVE_RESTART_FOR_PATCHES_SUPPORTED = 0x8221,
		PRIMITIVE_RESTART_INDEX = 0x8F9E,
		PROGRAM = 0x82E2,
		PROGRAM_BINARY_FORMATS = 0x87FF,
		PROGRAM_BINARY_LENGTH = 0x8741,
		PROGRAM_BINARY_RETRIEVABLE_HINT = 0x8257,
		PROGRAM_INPUT = 0x92E3,
		PROGRAM_OUTPUT = 0x92E4,
		PROGRAM_PIPELINE = 0x82E4,
		PROGRAM_PIPELINE_BINDING = 0x825A,
		PROGRAM_POINT_SIZE = 0x8642,
		PROGRAM_SEPARABLE = 0x8258,
		PROVOKING_VERTEX = 0x8E4F,
		PROXY_TEXTURE_1D = 0x8063,
		PROXY_TEXTURE_1D_ARRAY = 0x8C19,
		PROXY_TEXTURE_2D = 0x8064,
		PROXY_TEXTURE_2D_ARRAY = 0x8C1B,
		PROXY_TEXTURE_2D_MULTISAMPLE = 0x9101,
		PROXY_TEXTURE_2D_MULTISAMPLE_ARRAY = 0x9103,
		PROXY_TEXTURE_3D = 0x8070,
		PROXY_TEXTURE_CUBE_MAP = 0x851B,
		PROXY_TEXTURE_CUBE_MAP_ARRAY = 0x900B,
		PROXY_TEXTURE_RECTANGLE = 0x84F7,
		QUADS = 0x0007,
		QUADS_FOLLOW_PROVOKING_VERTEX_CONVENTION = 0x8E4C,
		QUERY = 0x82E3,
		QUERY_BUFFER = 0x9192,
		QUERY_BUFFER_BARRIER_BIT = 0x00008000,
		QUERY_BUFFER_BINDING = 0x9193,
		QUERY_BY_REGION_NO_WAIT = 0x8E16,
		QUERY_BY_REGION_NO_WAIT_INVERTED = 0x8E1A,
		QUERY_BY_REGION_WAIT = 0x8E15,
		QUERY_BY_REGION_WAIT_INVERTED = 0x8E19,
		QUERY_COUNTER_BITS = 0x8864,
		QUERY_NO_WAIT = 0x8E14,
		QUERY_NO_WAIT_INVERTED = 0x8E18,
		QUERY_RESULT = 0x8866,
		QUERY_RESULT_AVAILABLE = 0x8867,
		QUERY_RESULT_NO_WAIT = 0x9194,
		QUERY_TARGET = 0x82EA,
		QUERY_WAIT = 0x8E13,
		QUERY_WAIT_INVERTED = 0x8E17,
		R11F_G11F_B10F = 0x8C3A,
		R16 = 0x822A,
		R16F = 0x822D,
		R16I = 0x8233,
		R16UI = 0x8234,
		R16_SNORM = 0x8F98,
		R32F = 0x822E,
		R32I = 0x8235,
		R32UI = 0x8236,
		R3_G3_B2 = 0x2A10,
		R8 = 0x8229,
		R8I = 0x8231,
		R8UI = 0x8232,
		R8_SNORM = 0x8F94,
		RASTERIZER_DISCARD = 0x8C89,
		READ_BUFFER = 0x0C02,
		READ_FRAMEBUFFER = 0x8CA8,
		READ_FRAMEBUFFER_BINDING = 0x8CAA,
		READ_ONLY = 0x88B8,
		READ_PIXELS = 0x828C,
		READ_PIXELS_FORMAT = 0x828D,
		READ_PIXELS_TYPE = 0x828E,
		READ_WRITE = 0x88BA,
		RED = 0x1903,
		RED_INTEGER = 0x8D94,
		REFERENCED_BY_COMPUTE_SHADER = 0x930B,
		REFERENCED_BY_FRAGMENT_SHADER = 0x930A,
		REFERENCED_BY_GEOMETRY_SHADER = 0x9309,
		REFERENCED_BY_TESS_CONTROL_SHADER = 0x9307,
		REFERENCED_BY_TESS_EVALUATION_SHADER = 0x9308,
		REFERENCED_BY_VERTEX_SHADER = 0x9306,
		RENDERBUFFER = 0x8D41,
		RENDERBUFFER_ALPHA_SIZE = 0x8D53,
		RENDERBUFFER_BINDING = 0x8CA7,
		RENDERBUFFER_BLUE_SIZE = 0x8D52,
		RENDERBUFFER_DEPTH_SIZE = 0x8D54,
		RENDERBUFFER_GREEN_SIZE = 0x8D51,
		RENDERBUFFER_HEIGHT = 0x8D43,
		RENDERBUFFER_INTERNAL_FORMAT = 0x8D44,
		RENDERBUFFER_RED_SIZE = 0x8D50,
		RENDERBUFFER_SAMPLES = 0x8CAB,
		RENDERBUFFER_STENCIL_SIZE = 0x8D55,
		RENDERBUFFER_WIDTH = 0x8D42,
		RENDERER = 0x1F01,
		REPEAT = 0x2901,
		REPLACE = 0x1E01,
		RESET_NOTIFICATION_STRATEGY = 0x8256,
		RG = 0x8227,
		RG16 = 0x822C,
		RG16F = 0x822F,
		RG16I = 0x8239,
		RG16UI = 0x823A,
		RG16_SNORM = 0x8F99,
		RG32F = 0x8230,
		RG32I = 0x823B,
		RG32UI = 0x823C,
		RG8 = 0x822B,
		RG8I = 0x8237,
		RG8UI = 0x8238,
		RG8_SNORM = 0x8F95,
		RGB = 0x1907,
		RGB10 = 0x8052,
		RGB10_A2 = 0x8059,
		RGB10_A2UI = 0x906F,
		RGB12 = 0x8053,
		RGB16 = 0x8054,
		RGB16F = 0x881B,
		RGB16I = 0x8D89,
		RGB16UI = 0x8D77,
		RGB16_SNORM = 0x8F9A,
		RGB32F = 0x8815,
		RGB32I = 0x8D83,
		RGB32UI = 0x8D71,
		RGB4 = 0x804F,
		RGB5 = 0x8050,
		RGB565 = 0x8D62,
		RGB5_A1 = 0x8057,
		RGB8 = 0x8051,
		RGB8I = 0x8D8F,
		RGB8UI = 0x8D7D,
		RGB8_SNORM = 0x8F96,
		RGB9_E5 = 0x8C3D,
		RGBA = 0x1908,
		RGBA12 = 0x805A,
		RGBA16 = 0x805B,
		RGBA16F = 0x881A,
		RGBA16I = 0x8D88,
		RGBA16UI = 0x8D76,
		RGBA16_SNORM = 0x8F9B,
		RGBA2 = 0x8055,
		RGBA32F = 0x8814,
		RGBA32I = 0x8D82,
		RGBA32UI = 0x8D70,
		RGBA4 = 0x8056,
		RGBA8 = 0x8058,
		RGBA8I = 0x8D8E,
		RGBA8UI = 0x8D7C,
		RGBA8_SNORM = 0x8F97,
		RGBA_INTEGER = 0x8D99,
		RGB_INTEGER = 0x8D98,
		RG_INTEGER = 0x8228,
		RIGHT = 0x0407,
		SAMPLER = 0x82E6,
		SAMPLER_1D = 0x8B5D,
		SAMPLER_1D_ARRAY = 0x8DC0,
		SAMPLER_1D_ARRAY_SHADOW = 0x8DC3,
		SAMPLER_1D_SHADOW = 0x8B61,
		SAMPLER_2D = 0x8B5E,
		SAMPLER_2D_ARRAY = 0x8DC1,
		SAMPLER_2D_ARRAY_SHADOW = 0x8DC4,
		SAMPLER_2D_MULTISAMPLE = 0x9108,
		SAMPLER_2D_MULTISAMPLE_ARRAY = 0x910B,
		SAMPLER_2D_RECT = 0x8B63,
		SAMPLER_2D_RECT_SHADOW = 0x8B64,
		SAMPLER_2D_SHADOW = 0x8B62,
		SAMPLER_3D = 0x8B5F,
		SAMPLER_BINDING = 0x8919,
		SAMPLER_BUFFER = 0x8DC2,
		SAMPLER_CUBE = 0x8B60,
		SAMPLER_CUBE_MAP_ARRAY = 0x900C,
		SAMPLER_CUBE_MAP_ARRAY_SHADOW = 0x900D,
		SAMPLER_CUBE_SHADOW = 0x8DC5,
		SAMPLES = 0x80A9,
		SAMPLES_PASSED = 0x8914,
		SAMPLE_ALPHA_TO_COVERAGE = 0x809E,
		SAMPLE_ALPHA_TO_ONE = 0x809F,
		SAMPLE_BUFFERS = 0x80A8,
		SAMPLE_COVERAGE = 0x80A0,
		SAMPLE_COVERAGE_INVERT = 0x80AB,
		SAMPLE_COVERAGE_VALUE = 0x80AA,
		SAMPLE_MASK = 0x8E51,
		SAMPLE_MASK_VALUE = 0x8E52,
		SAMPLE_POSITION = 0x8E50,
		SAMPLE_SHADING = 0x8C36,
		SCISSOR_BOX = 0x0C10,
		SCISSOR_TEST = 0x0C11,
		SEPARATE_ATTRIBS = 0x8C8D,
		SET = 0x150F,
		SHADER = 0x82E1,
		SHADER_BINARY_FORMATS = 0x8DF8,
		SHADER_BINARY_FORMAT_SPIR_V = 0x9551,
		SHADER_COMPILER = 0x8DFA,
		SHADER_IMAGE_ACCESS_BARRIER_BIT = 0x00000020,
		SHADER_IMAGE_ATOMIC = 0x82A6,
		SHADER_IMAGE_LOAD = 0x82A4,
		SHADER_IMAGE_STORE = 0x82A5,
		SHADER_SOURCE_LENGTH = 0x8B88,
		SHADER_STORAGE_BARRIER_BIT = 0x00002000,
		SHADER_STORAGE_BLOCK = 0x92E6,
		SHADER_STORAGE_BUFFER = 0x90D2,
		SHADER_STORAGE_BUFFER_BINDING = 0x90D3,
		SHADER_STORAGE_BUFFER_OFFSET_ALIGNMENT = 0x90DF,
		SHADER_STORAGE_BUFFER_SIZE = 0x90D5,
		SHADER_STORAGE_BUFFER_START = 0x90D4,
		SHADER_TYPE = 0x8B4F,
		SHADING_LANGUAGE_VERSION = 0x8B8C,
		SHORT = 0x1402,
		SIGNALED = 0x9119,
		SIGNED_NORMALIZED = 0x8F9C,
		SIMULTANEOUS_TEXTURE_AND_DEPTH_TEST = 0x82AC,
		SIMULTANEOUS_TEXTURE_AND_DEPTH_WRITE = 0x82AE,
		SIMULTANEOUS_TEXTURE_AND_STENCIL_TEST = 0x82AD,
		SIMULTANEOUS_TEXTURE_AND_STENCIL_WRITE = 0x82AF,
		SMOOTH_LINE_WIDTH_GRANULARITY = 0x0B23,
		SMOOTH_LINE_WIDTH_RANGE = 0x0B22,
		SMOOTH_POINT_SIZE_GRANULARITY = 0x0B13,
		SMOOTH_POINT_SIZE_RANGE = 0x0B12,
		SPIR_V_BINARY = 0x9552,
		SPIR_V_EXTENSIONS = 0x9553,
		SRC1_ALPHA = 0x8589,
		SRC1_COLOR = 0x88F9,
		SRC_ALPHA = 0x0302,
		SRC_ALPHA_SATURATE = 0x0308,
		SRC_COLOR = 0x0300,
		SRGB = 0x8C40,
		SRGB8 = 0x8C41,
		SRGB8_ALPHA8 = 0x8C43,
		SRGB_ALPHA = 0x8C42,
		SRGB_READ = 0x8297,
		SRGB_WRITE = 0x8298,
		STACK_OVERFLOW = 0x0503,
		STACK_UNDERFLOW = 0x0504,
		STATIC_COPY = 0x88E6,
		STATIC_DRAW = 0x88E4,
		STATIC_READ = 0x88E5,
		STENCIL = 0x1802,
		STENCIL_ATTACHMENT = 0x8D20,
		STENCIL_BACK_FAIL = 0x8801,
		STENCIL_BACK_FUNC = 0x8800,
		STENCIL_BACK_PASS_DEPTH_FAIL = 0x8802,
		STENCIL_BACK_PASS_DEPTH_PASS = 0x8803,
		STENCIL_BACK_REF = 0x8CA3,
		STENCIL_BACK_VALUE_MASK = 0x8CA4,
		STENCIL_BACK_WRITEMASK = 0x8CA5,
		STENCIL_BUFFER_BIT = 0x00000400,
		STENCIL_CLEAR_VALUE = 0x0B91,
		STENCIL_COMPONENTS = 0x8285,
		STENCIL_FAIL = 0x0B94,
		STENCIL_FUNC = 0x0B92,
		STENCIL_INDEX = 0x1901,
		STENCIL_INDEX1 = 0x8D46,
		STENCIL_INDEX16 = 0x8D49,
		STENCIL_INDEX4 = 0x8D47,
		STENCIL_INDEX8 = 0x8D48,
		STENCIL_PASS_DEPTH_FAIL = 0x0B95,
		STENCIL_PASS_DEPTH_PASS = 0x0B96,
		STENCIL_REF = 0x0B97,
		STENCIL_RENDERABLE = 0x8288,
		STENCIL_TEST = 0x0B90,
		STENCIL_VALUE_MASK = 0x0B93,
		STENCIL_WRITEMASK = 0x0B98,
		STEREO = 0x0C33,
		STREAM_COPY = 0x88E2,
		STREAM_DRAW = 0x88E0,
		STREAM_READ = 0x88E1,
		SUBPIXEL_BITS = 0x0D50,
		SYNC_CONDITION = 0x9113,
		SYNC_FENCE = 0x9116,
		SYNC_FLAGS = 0x9115,
		SYNC_FLUSH_COMMANDS_BIT = 0x00000001,
		SYNC_GPU_COMMANDS_COMPLETE = 0x9117,
		SYNC_STATUS = 0x9114,
		TESS_CONTROL_OUTPUT_VERTICES = 0x8E75,
		TESS_CONTROL_SHADER = 0x8E88,
		TESS_CONTROL_SHADER_BIT = 0x00000008,
		TESS_CONTROL_SHADER_PATCHES = 0x82F1,
		TESS_CONTROL_SUBROUTINE = 0x92E9,
		TESS_CONTROL_SUBROUTINE_UNIFORM = 0x92EF,
		TESS_CONTROL_TEXTURE = 0x829C,
		TESS_EVALUATION_SHADER = 0x8E87,
		TESS_EVALUATION_SHADER_BIT = 0x00000010,
		TESS_EVALUATION_SHADER_INVOCATIONS = 0x82F2,
		TESS_EVALUATION_SUBROUTINE = 0x92EA,
		TESS_EVALUATION_SUBROUTINE_UNIFORM = 0x92F0,
		TESS_EVALUATION_TEXTURE = 0x829D,
		TESS_GEN_MODE = 0x8E76,
		TESS_GEN_POINT_MODE = 0x8E79,
		TESS_GEN_SPACING = 0x8E77,
		TESS_GEN_VERTEX_ORDER = 0x8E78,
		TEXTURE = 0x1702,
		TEXTURE0 = 0x84C0,
		TEXTURE1 = 0x84C1,
		TEXTURE10 = 0x84CA,
		TEXTURE11 = 0x84CB,
		TEXTURE12 = 0x84CC,
		TEXTURE13 = 0x84CD,
		TEXTURE14 = 0x84CE,
		TEXTURE15 = 0x84CF,
		TEXTURE16 = 0x84D0,
		TEXTURE17 = 0x84D1,
		TEXTURE18 = 0x84D2,
		TEXTURE19 = 0x84D3,
		TEXTURE2 = 0x84C2,
		TEXTURE20 = 0x84D4,
		TEXTURE21 = 0x84D5,
		TEXTURE22 = 0x84D6,
		TEXTURE23 = 0x84D7,
		TEXTURE24 = 0x84D8,
		TEXTURE25 = 0x84D9,
		TEXTURE26 = 0x84DA,
		TEXTURE27 = 0x84DB,
		TEXTURE28 = 0x84DC,
		TEXTURE29 = 0x84DD,
		TEXTURE3 = 0x84C3,
		TEXTURE30 = 0x84DE,
		TEXTURE31 = 0x84DF,
		TEXTURE4 = 0x84C4,
		TEXTURE5 = 0x84C5,
		TEXTURE6 = 0x84C6,
		TEXTURE7 = 0x84C7,
		TEXTURE8 = 0x84C8,
		TEXTURE9 = 0x84C9,
		TEXTURE_1D = 0x0DE0,
		TEXTURE_1D_ARRAY = 0x8C18,
		TEXTURE_2D = 0x0DE1,
		TEXTURE_2D_ARRAY = 0x8C1A,
		TEXTURE_2D_MULTISAMPLE = 0x9100,
		TEXTURE_2D_MULTISAMPLE_ARRAY = 0x9102,
		TEXTURE_3D = 0x806F,
		TEXTURE_ALPHA_SIZE = 0x805F,
		TEXTURE_ALPHA_TYPE = 0x8C13,
		TEXTURE_BASE_LEVEL = 0x813C,
		TEXTURE_BINDING_1D = 0x8068,
		TEXTURE_BINDING_1D_ARRAY = 0x8C1C,
		TEXTURE_BINDING_2D = 0x8069,
		TEXTURE_BINDING_2D_ARRAY = 0x8C1D,
		TEXTURE_BINDING_2D_MULTISAMPLE = 0x9104,
		TEXTURE_BINDING_2D_MULTISAMPLE_ARRAY = 0x9105,
		TEXTURE_BINDING_3D = 0x806A,
		TEXTURE_BINDING_BUFFER = 0x8C2C,
		TEXTURE_BINDING_CUBE_MAP = 0x8514,
		TEXTURE_BINDING_CUBE_MAP_ARRAY = 0x900A,
		TEXTURE_BINDING_RECTANGLE = 0x84F6,
		TEXTURE_BLUE_SIZE = 0x805E,
		TEXTURE_BLUE_TYPE = 0x8C12,
		TEXTURE_BORDER_COLOR = 0x1004,
		TEXTURE_BUFFER = 0x8C2A,
		TEXTURE_BUFFER_BINDING = 0x8C2A,
		TEXTURE_BUFFER_DATA_STORE_BINDING = 0x8C2D,
		TEXTURE_BUFFER_OFFSET = 0x919D,
		TEXTURE_BUFFER_OFFSET_ALIGNMENT = 0x919F,
		TEXTURE_BUFFER_SIZE = 0x919E,
		TEXTURE_COMPARE_FUNC = 0x884D,
		TEXTURE_COMPARE_MODE = 0x884C,
		TEXTURE_COMPRESSED = 0x86A1,
		TEXTURE_COMPRESSED_BLOCK_HEIGHT = 0x82B2,
		TEXTURE_COMPRESSED_BLOCK_SIZE = 0x82B3,
		TEXTURE_COMPRESSED_BLOCK_WIDTH = 0x82B1,
		TEXTURE_COMPRESSED_IMAGE_SIZE = 0x86A0,
		TEXTURE_COMPRESSION_HINT = 0x84EF,
		TEXTURE_CUBE_MAP = 0x8513,
		TEXTURE_CUBE_MAP_ARRAY = 0x9009,
		TEXTURE_CUBE_MAP_NEGATIVE_X = 0x8516,
		TEXTURE_CUBE_MAP_NEGATIVE_Y = 0x8518,
		TEXTURE_CUBE_MAP_NEGATIVE_Z = 0x851A,
		TEXTURE_CUBE_MAP_POSITIVE_X = 0x8515,
		TEXTURE_CUBE_MAP_POSITIVE_Y = 0x8517,
		TEXTURE_CUBE_MAP_POSITIVE_Z = 0x8519,
		TEXTURE_CUBE_MAP_SEAMLESS = 0x884F,
		TEXTURE_DEPTH = 0x8071,
		TEXTURE_DEPTH_SIZE = 0x884A,
		TEXTURE_DEPTH_TYPE = 0x8C16,
		TEXTURE_FETCH_BARRIER_BIT = 0x00000008,
		TEXTURE_FIXED_SAMPLE_LOCATIONS = 0x9107,
		TEXTURE_GATHER = 0x82A2,
		TEXTURE_GATHER_SHADOW = 0x82A3,
		TEXTURE_GREEN_SIZE = 0x805D,
		TEXTURE_GREEN_TYPE = 0x8C11,
		TEXTURE_HEIGHT = 0x1001,
		TEXTURE_IMAGE_FORMAT = 0x828F,
		TEXTURE_IMAGE_TYPE = 0x8290,
		TEXTURE_IMMUTABLE_FORMAT = 0x912F,
		TEXTURE_IMMUTABLE_LEVELS = 0x82DF,
		TEXTURE_INTERNAL_FORMAT = 0x1003,
		TEXTURE_LOD_BIAS = 0x8501,
		TEXTURE_MAG_FILTER = 0x2800,
		TEXTURE_MAX_ANISOTROPY = 0x84FE,
		TEXTURE_MAX_ANISOTROPY_EXT = 0x84FE,
		TEXTURE_MAX_LEVEL = 0x813D,
		TEXTURE_MAX_LOD = 0x813B,
		TEXTURE_MIN_FILTER = 0x2801,
		TEXTURE_MIN_LOD = 0x813A,
		TEXTURE_RECTANGLE = 0x84F5,
		TEXTURE_RED_SIZE = 0x805C,
		TEXTURE_RED_TYPE = 0x8C10,
		TEXTURE_SAMPLES = 0x9106,
		TEXTURE_SHADOW = 0x82A1,
		TEXTURE_SHARED_SIZE = 0x8C3F,
		TEXTURE_STENCIL_SIZE = 0x88F1,
		TEXTURE_SWIZZLE_A = 0x8E45,
		TEXTURE_SWIZZLE_B = 0x8E44,
		TEXTURE_SWIZZLE_G = 0x8E43,
		TEXTURE_SWIZZLE_R = 0x8E42,
		TEXTURE_SWIZZLE_RGBA = 0x8E46,
		TEXTURE_TARGET = 0x1006,
		TEXTURE_UPDATE_BARRIER_BIT = 0x00000100,
		TEXTURE_VIEW = 0x82B5,
		TEXTURE_VIEW_MIN_LAYER = 0x82DD,
		TEXTURE_VIEW_MIN_LEVEL = 0x82DB,
		TEXTURE_VIEW_NUM_LAYERS = 0x82DE,
		TEXTURE_VIEW_NUM_LEVELS = 0x82DC,
		TEXTURE_WIDTH = 0x1000,
		TEXTURE_WRAP_R = 0x8072,
		TEXTURE_WRAP_S = 0x2802,
		TEXTURE_WRAP_T = 0x2803,
		TIMEOUT_EXPIRED = 0x911B,
		TIMEOUT_IGNORED = 0xFFFFFFFF,
		TIMESTAMP = 0x8E28,
		TIME_ELAPSED = 0x88BF,
		TOP_LEVEL_ARRAY_SIZE = 0x930C,
		TOP_LEVEL_ARRAY_STRIDE = 0x930D,
		TRANSFORM_FEEDBACK = 0x8E22,
		TRANSFORM_FEEDBACK_ACTIVE = 0x8E24,
		TRANSFORM_FEEDBACK_BARRIER_BIT = 0x00000800,
		TRANSFORM_FEEDBACK_BINDING = 0x8E25,
		TRANSFORM_FEEDBACK_BUFFER = 0x8C8E,
		TRANSFORM_FEEDBACK_BUFFER_ACTIVE = 0x8E24,
		TRANSFORM_FEEDBACK_BUFFER_BINDING = 0x8C8F,
		TRANSFORM_FEEDBACK_BUFFER_INDEX = 0x934B,
		TRANSFORM_FEEDBACK_BUFFER_MODE = 0x8C7F,
		TRANSFORM_FEEDBACK_BUFFER_PAUSED = 0x8E23,
		TRANSFORM_FEEDBACK_BUFFER_SIZE = 0x8C85,
		TRANSFORM_FEEDBACK_BUFFER_START = 0x8C84,
		TRANSFORM_FEEDBACK_BUFFER_STRIDE = 0x934C,
		TRANSFORM_FEEDBACK_OVERFLOW = 0x82EC,
		TRANSFORM_FEEDBACK_PAUSED = 0x8E23,
		TRANSFORM_FEEDBACK_PRIMITIVES_WRITTEN = 0x8C88,
		TRANSFORM_FEEDBACK_STREAM_OVERFLOW = 0x82ED,
		TRANSFORM_FEEDBACK_VARYING = 0x92F4,
		TRANSFORM_FEEDBACK_VARYINGS = 0x8C83,
		TRANSFORM_FEEDBACK_VARYING_MAX_LENGTH = 0x8C76,
		TRIANGLES = 0x0004,
		TRIANGLES_ADJACENCY = 0x000C,
		TRIANGLE_FAN = 0x0006,
		TRIANGLE_STRIP = 0x0005,
		TRIANGLE_STRIP_ADJACENCY = 0x000D,
		TRUE = 1,
		TYPE = 0x92FA,
		UNDEFINED_VERTEX = 0x8260,
		UNIFORM = 0x92E1,
		UNIFORM_ARRAY_STRIDE = 0x8A3C,
		UNIFORM_ATOMIC_COUNTER_BUFFER_INDEX = 0x92DA,
		UNIFORM_BARRIER_BIT = 0x00000004,
		UNIFORM_BLOCK = 0x92E2,
		UNIFORM_BLOCK_ACTIVE_UNIFORMS = 0x8A42,
		UNIFORM_BLOCK_ACTIVE_UNIFORM_INDICES = 0x8A43,
		UNIFORM_BLOCK_BINDING = 0x8A3F,
		UNIFORM_BLOCK_DATA_SIZE = 0x8A40,
		UNIFORM_BLOCK_INDEX = 0x8A3A,
		UNIFORM_BLOCK_NAME_LENGTH = 0x8A41,
		UNIFORM_BLOCK_REFERENCED_BY_COMPUTE_SHADER = 0x90EC,
		UNIFORM_BLOCK_REFERENCED_BY_FRAGMENT_SHADER = 0x8A46,
		UNIFORM_BLOCK_REFERENCED_BY_GEOMETRY_SHADER = 0x8A45,
		UNIFORM_BLOCK_REFERENCED_BY_TESS_CONTROL_SHADER = 0x84F0,
		UNIFORM_BLOCK_REFERENCED_BY_TESS_EVALUATION_SHADER = 0x84F1,
		UNIFORM_BLOCK_REFERENCED_BY_VERTEX_SHADER = 0x8A44,
		UNIFORM_BUFFER = 0x8A11,
		UNIFORM_BUFFER_BINDING = 0x8A28,
		UNIFORM_BUFFER_OFFSET_ALIGNMENT = 0x8A34,
		UNIFORM_BUFFER_SIZE = 0x8A2A,
		UNIFORM_BUFFER_START = 0x8A29,
		UNIFORM_IS_ROW_MAJOR = 0x8A3E,
		UNIFORM_MATRIX_STRIDE = 0x8A3D,
		UNIFORM_NAME_LENGTH = 0x8A39,
		UNIFORM_OFFSET = 0x8A3B,
		UNIFORM_SIZE = 0x8A38,
		UNIFORM_TYPE = 0x8A37,
		UNKNOWN_CONTEXT_RESET = 0x8255,
		UNPACK_ALIGNMENT = 0x0CF5,
		UNPACK_COMPRESSED_BLOCK_DEPTH = 0x9129,
		UNPACK_COMPRESSED_BLOCK_HEIGHT = 0x9128,
		UNPACK_COMPRESSED_BLOCK_SIZE = 0x912A,
		UNPACK_COMPRESSED_BLOCK_WIDTH = 0x9127,
		UNPACK_IMAGE_HEIGHT = 0x806E,
		UNPACK_LSB_FIRST = 0x0CF1,
		UNPACK_ROW_LENGTH = 0x0CF2,
		UNPACK_SKIP_IMAGES = 0x806D,
		UNPACK_SKIP_PIXELS = 0x0CF4,
		UNPACK_SKIP_ROWS = 0x0CF3,
		UNPACK_SWAP_BYTES = 0x0CF0,
		UNSIGNALED = 0x9118,
		UNSIGNED_BYTE = 0x1401,
		UNSIGNED_BYTE_2_3_3_REV = 0x8362,
		UNSIGNED_BYTE_3_3_2 = 0x8032,
		UNSIGNED_INT = 0x1405,
		UNSIGNED_INT_10F_11F_11F_REV = 0x8C3B,
		UNSIGNED_INT_10_10_10_2 = 0x8036,
		UNSIGNED_INT_24_8 = 0x84FA,
		UNSIGNED_INT_2_10_10_10_REV = 0x8368,
		UNSIGNED_INT_5_9_9_9_REV = 0x8C3E,
		UNSIGNED_INT_8_8_8_8 = 0x8035,
		UNSIGNED_INT_8_8_8_8_REV = 0x8367,
		UNSIGNED_INT_ATOMIC_COUNTER = 0x92DB,
		UNSIGNED_INT_IMAGE_1D = 0x9062,
		UNSIGNED_INT_IMAGE_1D_ARRAY = 0x9068,
		UNSIGNED_INT_IMAGE_2D = 0x9063,
		UNSIGNED_INT_IMAGE_2D_ARRAY = 0x9069,
		UNSIGNED_INT_IMAGE_2D_MULTISAMPLE = 0x906B,
		UNSIGNED_INT_IMAGE_2D_MULTISAMPLE_ARRAY = 0x906C,
		UNSIGNED_INT_IMAGE_2D_RECT = 0x9065,
		UNSIGNED_INT_IMAGE_3D = 0x9064,
		UNSIGNED_INT_IMAGE_BUFFER = 0x9067,
		UNSIGNED_INT_IMAGE_CUBE = 0x9066,
		UNSIGNED_INT_IMAGE_CUBE_MAP_ARRAY = 0x906A,
		UNSIGNED_INT_SAMPLER_1D = 0x8DD1,
		UNSIGNED_INT_SAMPLER_1D_ARRAY = 0x8DD6,
		UNSIGNED_INT_SAMPLER_2D = 0x8DD2,
		UNSIGNED_INT_SAMPLER_2D_ARRAY = 0x8DD7,
		UNSIGNED_INT_SAMPLER_2D_MULTISAMPLE = 0x910A,
		UNSIGNED_INT_SAMPLER_2D_MULTISAMPLE_ARRAY = 0x910D,
		UNSIGNED_INT_SAMPLER_2D_RECT = 0x8DD5,
		UNSIGNED_INT_SAMPLER_3D = 0x8DD3,
		UNSIGNED_INT_SAMPLER_BUFFER = 0x8DD8,
		UNSIGNED_INT_SAMPLER_CUBE = 0x8DD4,
		UNSIGNED_INT_SAMPLER_CUBE_MAP_ARRAY = 0x900F,
		UNSIGNED_INT_VEC2 = 0x8DC6,
		UNSIGNED_INT_VEC3 = 0x8DC7,
		UNSIGNED_INT_VEC4 = 0x8DC8,
		UNSIGNED_NORMALIZED = 0x8C17,
		UNSIGNED_SHORT = 0x1403,
		UNSIGNED_SHORT_1_5_5_5_REV = 0x8366,
		UNSIGNED_SHORT_4_4_4_4 = 0x8033,
		UNSIGNED_SHORT_4_4_4_4_REV = 0x8365,
		UNSIGNED_SHORT_5_5_5_1 = 0x8034,
		UNSIGNED_SHORT_5_6_5 = 0x8363,
		UNSIGNED_SHORT_5_6_5_REV = 0x8364,
		UPPER_LEFT = 0x8CA2,
		VALIDATE_STATUS = 0x8B83,
		VENDOR = 0x1F00,
		VERSION = 0x1F02,
		VERTEX_ARRAY = 0x8074,
		VERTEX_ARRAY_BINDING = 0x85B5,
		VERTEX_ATTRIB_ARRAY_BARRIER_BIT = 0x00000001,
		VERTEX_ATTRIB_ARRAY_BUFFER_BINDING = 0x889F,
		VERTEX_ATTRIB_ARRAY_DIVISOR = 0x88FE,
		VERTEX_ATTRIB_ARRAY_ENABLED = 0x8622,
		VERTEX_ATTRIB_ARRAY_INTEGER = 0x88FD,
		VERTEX_ATTRIB_ARRAY_LONG = 0x874E,
		VERTEX_ATTRIB_ARRAY_NORMALIZED = 0x886A,
		VERTEX_ATTRIB_ARRAY_POINTER = 0x8645,
		VERTEX_ATTRIB_ARRAY_SIZE = 0x8623,
		VERTEX_ATTRIB_ARRAY_STRIDE = 0x8624,
		VERTEX_ATTRIB_ARRAY_TYPE = 0x8625,
		VERTEX_ATTRIB_BINDING = 0x82D4,
		VERTEX_ATTRIB_RELATIVE_OFFSET = 0x82D5,
		VERTEX_BINDING_BUFFER = 0x8F4F,
		VERTEX_BINDING_DIVISOR = 0x82D6,
		VERTEX_BINDING_OFFSET = 0x82D7,
		VERTEX_BINDING_STRIDE = 0x82D8,
		VERTEX_PROGRAM_POINT_SIZE = 0x8642,
		VERTEX_SHADER = 0x8B31,
		VERTEX_SHADER_BIT = 0x00000001,
		VERTEX_SHADER_INVOCATIONS = 0x82F0,
		VERTEX_SUBROUTINE = 0x92E8,
		VERTEX_SUBROUTINE_UNIFORM = 0x92EE,
		VERTEX_TEXTURE = 0x829B,
		VERTICES_SUBMITTED = 0x82EE,
		VIEWPORT = 0x0BA2,
		VIEWPORT_BOUNDS_RANGE = 0x825D,
		VIEWPORT_INDEX_PROVOKING_VERTEX = 0x825F,
		VIEWPORT_SUBPIXEL_BITS = 0x825C,
		VIEW_CLASS_128_BITS = 0x82C4,
		VIEW_CLASS_16_BITS = 0x82CA,
		VIEW_CLASS_24_BITS = 0x82C9,
		VIEW_CLASS_32_BITS = 0x82C8,
		VIEW_CLASS_48_BITS = 0x82C7,
		VIEW_CLASS_64_BITS = 0x82C6,
		VIEW_CLASS_8_BITS = 0x82CB,
		VIEW_CLASS_96_BITS = 0x82C5,
		VIEW_CLASS_BPTC_FLOAT = 0x82D3,
		VIEW_CLASS_BPTC_UNORM = 0x82D2,
		VIEW_CLASS_RGTC1_RED = 0x82D0,
		VIEW_CLASS_RGTC2_RG = 0x82D1,
		VIEW_CLASS_S3TC_DXT1_RGB = 0x82CC,
		VIEW_CLASS_S3TC_DXT1_RGBA = 0x82CD,
		VIEW_CLASS_S3TC_DXT3_RGBA = 0x82CE,
		VIEW_CLASS_S3TC_DXT5_RGBA = 0x82CF,
		VIEW_COMPATIBILITY_CLASS = 0x82B6,
		WAIT_FAILED = 0x911D,
		WRITE_ONLY = 0x88B9,
		XOR = 0x1506,
		ZERO = 0,
		ZERO_TO_ONE = 0x935F;
}
