package othello.faskinen.opengl;

import java.lang.foreign.MemoryAddress;
import java.lang.invoke.MethodHandle;

import othello.faskinen.Lib;
import othello.faskinen.Platform;

public class GL {
    static {
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

	// GL constants
	public static final int
		COLOR_BUFFER_BIT = 0x00004000,
		DEPTH_BUFFER_BIT = 0x00000100,
		STENCIL_BUFFER_BIT = 0x00000400,
		FALSE = 0,
		TRUE = 1,
		POINTS = 0x0000,
		LINES = 0x0001,
		LINE_LOOP = 0x0002,
		LINE_STRIP = 0x0003,
		TRIANGLES = 0x0004,
		TRIANGLE_STRIP = 0x0005,
		TRIANGLE_FAN = 0x0006,
		ZERO = 0,
		ONE = 1,
		SRC_COLOR = 0x0300,
		ONE_MINUS_SRC_COLOR = 0x0301,
		SRC_ALPHA = 0x0302,
		ONE_MINUS_SRC_ALPHA = 0x0303,
		DST_ALPHA = 0x0304,
		ONE_MINUS_DST_ALPHA = 0x0305,
		DST_COLOR = 0x0306,
		ONE_MINUS_DST_COLOR = 0x0307,
		SRC_ALPHA_SATURATE = 0x0308,
		FUNC_ADD = 0x8006,
		BLEND_EQUATION = 0x8009,
		BLEND_EQUATION_RGB = 0x8009,
		BLEND_EQUATION_ALPHA = 0x883D,
		FUNC_SUBTRACT = 0x800A,
		FUNC_REVERSE_SUBTRACT = 0x800B,
		BLEND_DST_RGB = 0x80C8,
		BLEND_SRC_RGB = 0x80C9,
		BLEND_DST_ALPHA = 0x80CA,
		BLEND_SRC_ALPHA = 0x80CB,
		CONSTANT_COLOR = 0x8001,
		ONE_MINUS_CONSTANT_COLOR = 0x8002,
		CONSTANT_ALPHA = 0x8003,
		ONE_MINUS_CONSTANT_ALPHA = 0x8004,
		BLEND_COLOR = 0x8005,
		ARRAY_BUFFER = 0x8892,
		ELEMENT_ARRAY_BUFFER = 0x8893,
		ARRAY_BUFFER_BINDING = 0x8894,
		ELEMENT_ARRAY_BUFFER_BINDING = 0x8895,
		STREAM_DRAW = 0x088E4,
		STATIC_DRAW = 0x088E4,
		DYNAMIC_DRAW = 0x088E4,
		STATIC_READ = 0x088E5,
		DYNAMIC_READ = 0x088E5,
		STATIC_COPY = 0x088E6,
		DYNAMIC_COPY = 0x088E6,
		BUFFER_SIZE = 0x8764,
		BUFFER_USAGE = 0x8765,
		CURRENT_VERTEX_ATTRIB = 0x8626,
		FRONT = 0x0404,
		BACK = 0x0405,
		FRONT_AND_BACK = 0x0408,
		TEXTURE_2D = 0x0DE1,
		CULL_FACE = 0x0B44,
		BLEND = 0x0BE2,
		DITHER = 0x0BD0,
		STENCIL_TEST = 0x0B90,
		DEPTH_TEST = 0x0B71,
		SCISSOR_TEST = 0x0C11,
		POLYGON_OFFSET_FILL = 0x8037,
		SAMPLE_ALPHA_TO_COVERAGE = 0x809E,
		SAMPLE_COVERAGE = 0x80A0,
		NO_ERROR = 0,
		INVALID_ENUM = 0x0500,
		INVALID_VALUE = 0x0501,
		INVALID_OPERATION = 0x0502,
		OUT_OF_MEMORY = 0x0505,
		CW = 0x0900,
		CCW = 0x0901,
		LINE_WIDTH = 0x0B21,
		ALIASED_POINT_SIZE_RANGE = 0x846D,
		ALIASED_LINE_WIDTH_RANGE = 0x846E,
		CULL_FACE_MODE = 0x0B45,
		FRONT_FACE = 0x0B46,
		DEPTH_RANGE = 0x0B70,
		DEPTH_WRITEMASK = 0x0B72,
		DEPTH_CLEAR_VALUE = 0x0B73,
		DEPTH_FUNC = 0x0B74,
		STENCIL_CLEAR_VALUE = 0x0B91,
		STENCIL_FUNC = 0x0B92,
		STENCIL_FAIL = 0x0B94,
		STENCIL_PASS_DEPTH_FAIL = 0x0B95,
		STENCIL_PASS_DEPTH_PASS = 0x0B96,
		STENCIL_REF = 0x0B97,
		STENCIL_VALUE_MASK = 0x0B93,
		STENCIL_WRITEMASK = 0x0B98,
		STENCIL_BACK_FUNC = 0x8800,
		STENCIL_BACK_FAIL = 0x8801,
		STENCIL_BACK_PASS_DEPTH_FAIL = 0x8802,
		STENCIL_BACK_PASS_DEPTH_PASS = 0x8803,
		STENCIL_BACK_REF = 0x8CA3,
		STENCIL_BACK_VALUE_MASK = 0x8CA4,
		STENCIL_BACK_WRITEMASK = 0x8CA5,
		VIEWPORT = 0x0BA2,
		SCISSOR_BOX = 0x0C10,
		COLOR_CLEAR_VALUE = 0x0C22,
		COLOR_WRITEMASK = 0x0C23,
		UNPACK_ALIGNMENT = 0x0CF5,
		PACK_ALIGNMENT = 0x0D05,
		MAX_TEXTURE_SIZE = 0x0D33,
		MAX_VIEWPORT_DIMS = 0x0D3A,
		SUBPIXEL_BITS = 0x0D50,
		RED_BITS = 0x0D52,
		GREEN_BITS = 0x0D53,
		BLUE_BITS = 0x0D54,
		ALPHA_BITS = 0x0D55,
		DEPTH_BITS = 0x0D56,
		STENCIL_BITS = 0x0D57,
		POLYGON_OFFSET_UNITS = 0x2A00,
		POLYGON_OFFSET_FACTOR = 0x8038,
		TEXTURE_BINDING_2D = 0x8069,
		SAMPLE_BUFFERS = 0x80A8,
		SAMPLES = 0x80A9,
		SAMPLE_COVERAGE_VALUE = 0x80AA,
		SAMPLE_COVERAGE_INVERT = 0x80AB,
		NUM_COMPRESSED_TEXTURE_FORMATS = 0x86A2,
		COMPRESSED_TEXTURE_FORMATS = 0x86A3,
		DONT_CARE = 0x1100,
		FASTEST = 0x1101,
		NICEST = 0x1102,
		GENERATE_MIPMAP_HINT = 0x8192,
		BYTE = 0x1400,
		UNSIGNED_BYTE = 0x1401,
		SHORT = 0x1402,
		UNSIGNED_SHORT = 0x1403,
		INT = 0x1404,
		UNSIGNED_INT = 0x1405,
		FLOAT = 0x1406,
		FIXED = 0x140C,
		DEPTH_COMPONENT = 0x1902,
		ALPHA = 0x1906,
		RGB = 0x1907,
		RGBA = 0x1908,
		LUMINANCE = 0x1909,
		LUMINANCE_ALPHA = 0x190A,
		UNSIGNED_SHORT_4_4_4_4 = 0x8033,
		UNSIGNED_SHORT_5_5_5_1 = 0x8034,
		UNSIGNED_SHORT_5_6_5 = 0x8363,
		FRAGMENT_SHADER = 0x8B30,
		VERTEX_SHADER = 0x8B31,
		COMPUTE_SHADER = 0x91B9,
		MAX_VERTEX_ATTRIBS = 0x8869,
		MAX_VERTEX_UNIFORM_VECTORS = 0x8DFB,
		MAX_VARYING_VECTORS = 0x8DFC,
		MAX_COMBINED_TEXTURE_IMAGE_UNITS = 0x8B4D,
		MAX_VERTEX_TEXTURE_IMAGE_UNITS = 0x8B4C,
		MAX_TEXTURE_IMAGE_UNITS = 0x8872,
		MAX_FRAGMENT_UNIFORM_VECTORS = 0x8DFD,
		SHADER_TYPE = 0x8B4F,
		DELETE_STATUS = 0x8B80,
		LINK_STATUS = 0x8B82,
		VALIDATE_STATUS = 0x8B83,
		ATTACHED_SHADERS = 0x8B85,
		ACTIVE_UNIFORMS = 0x8B86,
		ACTIVE_UNIFORM_MAX_LENGTH = 0x8B87,
		ACTIVE_ATTRIBUTES = 0x8B89,
		ACTIVE_ATTRIBUTE_MAX_LENGTH = 0x8B8A,
		SHADING_LANGUAGE_VERSION = 0x8B8C,
		CURRENT_PROGRAM = 0x8B8D,
		NEVER = 0x0200,
		LESS = 0x0201,
		EQUAL = 0x0202,
		LEQUAL = 0x0203,
		GREATER = 0x0204,
		NOTEQUAL = 0x0205,
		GEQUAL = 0x0206,
		ALWAYS = 0x0207,
		KEEP = 0x1E00,
		REPLACE = 0x1E01,
		INCR = 0x1E02,
		DECR = 0x1E03,
		INVERT = 0x150A,
		INCR_WRAP = 0x8507,
		DECR_WRAP = 0x8508,
		VENDOR = 0x1F00,
		RENDERER = 0x1F01,
		VERSION = 0x1F02,
		EXTENSIONS = 0x1F03,
		NEAREST = 0x2600,
		LINEAR = 0x2601,
		NEAREST_MIPMAP_NEAREST = 0x2700,
		LINEAR_MIPMAP_NEAREST = 0x2701,
		NEAREST_MIPMAP_LINEAR = 0x2702,
		LINEAR_MIPMAP_LINEAR = 0x2703,
		TEXTURE_MAG_FILTER = 0x2800,
		TEXTURE_MIN_FILTER = 0x2801,
		TEXTURE_WRAP_S = 0x2802,
		TEXTURE_WRAP_T = 0x2803,
		TEXTURE = 0x1702,
		TEXTURE_CUBE_MAP = 0x8513,
		TEXTURE_BINDING_CUBE_MAP = 0x8514,
		TEXTURE_CUBE_MAP_POSITIVE_X = 0x8515,
		TEXTURE_CUBE_MAP_NEGATIVE_X = 0x8516,
		TEXTURE_CUBE_MAP_POSITIVE_Y = 0x8517,
		TEXTURE_CUBE_MAP_NEGATIVE_Y = 0x8518,
		TEXTURE_CUBE_MAP_POSITIVE_Z = 0x8519,
		TEXTURE_CUBE_MAP_NEGATIVE_Z = 0x851A,
		MAX_CUBE_MAP_TEXTURE_SIZE = 0x851C,
		TEXTURE0 = 0x84C0,
		COMPILE_STATUS = 0x8B81,
		SHADER_STORAGE_BUFFER = 0x90D2,
		SHADER_STORAGE_BLOCK = 0x92E6;

	private static MethodHandle HANDLE_glClearColor = Lib.loadFuncHandle(
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

	private static MethodHandle HANDLE_glClear = Lib.loadFuncHandle(
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

	private static MethodHandle HANDLE_glViewport = Lib.loadFuncHandle(
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

	private static MethodHandle HANDLE_glCreateShader = Lib.loadFuncHandle(
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

	private static MethodHandle HANDLE_glShaderSource = Lib.loadFuncHandle(
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

	private static MethodHandle HANDLE_glCompileShader = Lib.loadFuncHandle(
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

	private static MethodHandle HANDLE_glGetShaderiv = Lib.loadFuncHandle(
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

	private static MethodHandle HANDLE_glGetShaderInfoLog = Lib.loadFuncHandle(
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

	private static MethodHandle HANDLE_glCreateProgram = Lib.loadFuncHandle(
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

	private static MethodHandle HANDLE_glAttachShader = Lib.loadFuncHandle(
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

	private static MethodHandle HANDLE_glLinkProgram = Lib.loadFuncHandle(
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

	private static MethodHandle HANDLE_glGetProgramiv = Lib.loadFuncHandle(
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

	private static MethodHandle HANDLE_glGetProgramInfoLog = Lib.loadFuncHandle(
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

	private static MethodHandle HANDLE_glUseProgram = Lib.loadFuncHandle(
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

	private static MethodHandle HANDLE_glGetUniformLocation = Lib.loadFuncHandle(
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

	private static MethodHandle HANDLE_glGetProgramResourceIndex = Lib.loadFuncHandle(
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

	private static MethodHandle HANDLE_glShaderStorageBlockBinding = Lib.loadFuncHandle(
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

	private static MethodHandle HANDLE_glUniform1i = Lib.loadFuncHandle(
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

	private static MethodHandle HANDLE_glUniform1f = Lib.loadFuncHandle(
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

	private static MethodHandle HANDLE_glUniform2f = Lib.loadFuncHandle(
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

	private static MethodHandle HANDLE_glUniform3f = Lib.loadFuncHandle(
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

	private static MethodHandle HANDLE_glUniform4f = Lib.loadFuncHandle(
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

	private static MethodHandle HANDLE_glUniformMatrix4fv = Lib.loadFuncHandle(
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

	private static MethodHandle HANDLE_glGenBuffers = Lib.loadFuncHandle(
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

	private static MethodHandle HANDLE_glBindBuffer = Lib.loadFuncHandle(
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

	private static MethodHandle HANDLE_glBufferData = Lib.loadFuncHandle(
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

	private static MethodHandle HANDLE_glMapBuffer = Lib.loadFuncHandle(
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

	private static MethodHandle HANDLE_glMapBufferRange = Lib.loadFuncHandle(
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

	private static MethodHandle HANDLE_glUnmapBuffer = Lib.loadFuncHandle(
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

	private static MethodHandle HANDLE_glGetBufferSubData = Lib.loadFuncHandle(
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

	private static MethodHandle HANDLE_glBindBufferBase = Lib.loadFuncHandle(
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

	private static MethodHandle HANDLE_glDispatchCompute = Lib.loadFuncHandle(
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

	private static MethodHandle HANDLE_glDeleteShader = Lib.loadFuncHandle(
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

	private static MethodHandle HANDLE_glDeleteProgram = Lib.loadFuncHandle(
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

	private static MethodHandle HANDLE_glDeleteBuffers = Lib.loadFuncHandle(
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
}
