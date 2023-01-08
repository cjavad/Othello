package othello.faskinen.x11;

import java.lang.foreign.MemoryAddress;
import java.lang.invoke.MethodHandle;

import othello.faskinen.Lib;
import othello.faskinen.Platform;

/**
 * A wrapper around the X11 library.
 */
public class X11 {
	static {
		if (Platform.isLinux()) {
			System.loadLibrary("X11");
		}
	}

	// x11 masks
	public static final long 
		ExposureMask = 1 << 15,
		KeyPressMask = 1 << 0;

	public static final int
		GLX_RENDER_TYPE = 0x8011,
		GLX_RGBA = 4,
		GLX_RGBA_BIT = 1 << 0,
		GLX_RGBA_TYPE = 0x8014,
		GLX_DRAWABLE_TYPE = 0x8010,
		GLX_WINDOW_BIT = 1 << 0,
		GLX_DOUBLEBUFFER = 5,
		GLX_RED_SIZE = 8,
		GLX_GREEN_SIZE = 9,
		GLX_BLUE_SIZE = 10,
		GLX_ALPHA_SIZE = 11,
		GLX_DEPTH_SIZE = 12,
		GLX_CONTEXT_MAJOR_VERSION_ARB = 0x2091,
		GLX_CONTEXT_MINOR_VERSION_ARB = 0x2092,
		False = 0,
		True = 1,
		None = 0;

	private static MethodHandle HANDLE_XOpenDisplay = Lib.loadFuncHandle(
		"XOpenDisplay", 
		Lib.C_POINTER_T, 
		Lib.C_POINTER_T
	);
	public static MemoryAddress XOpenDisplay(MemoryAddress display_name) {
		try {
			return (MemoryAddress) HANDLE_XOpenDisplay.invoke(display_name);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}

		return null;
	}

	private static MethodHandle HANDLE_XCloseDisplay = Lib.loadFuncHandle(
		"XCloseDisplay", 
		Lib.C_INT32_T,
		Lib.C_POINTER_T
	);
	public static int XCloseDisplay(MemoryAddress display) {
		try {
			return (int) HANDLE_XCloseDisplay.invoke(display);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}

		return -1;
	}

	private static MethodHandle HANDLE_XDefaultScreen = Lib.loadFuncHandle(
		"XDefaultScreen", 
		Lib.C_INT32_T,
		Lib.C_POINTER_T
	);
	public static int XDefaultScreen(MemoryAddress display) {
		try {
			return (int) HANDLE_XDefaultScreen.invoke(display);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}

		return -1;
	}

	private static MethodHandle HANDLE_XRootWindow = Lib.loadFuncHandle(
		"XRootWindow", 
		Lib.C_POINTER_T,
		Lib.C_POINTER_T,
		Lib.C_INT32_T
	);
	public static MemoryAddress XRootWindow(MemoryAddress display, int screen_number) {
		try {
			return (MemoryAddress) HANDLE_XRootWindow.invoke(display, screen_number);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}

		return null;
	}

	private static MethodHandle HANDLE_XCreateSimpleWindow = Lib.loadFuncHandle(
		"XCreateSimpleWindow", 
		Lib.C_UINT64_T,
		Lib.C_POINTER_T,
		Lib.C_POINTER_T,
		Lib.C_INT32_T,
		Lib.C_INT32_T,
		Lib.C_UINT32_T,
		Lib.C_UINT32_T,
		Lib.C_UINT32_T,
		Lib.C_UINT32_T,
		Lib.C_UINT32_T
	);
	public static long XCreateSimpleWindow(
		MemoryAddress display, 
		MemoryAddress parent, 
		int x, 
		int y, 
		int width, 
		int height, 
		int border_width, 
		int border, 
		int background
	) {
		try {
			return (long) HANDLE_XCreateSimpleWindow.invoke(
				display, 
				parent, 
				x, 
				y, 
				width, 
				height, 
				border_width,
				border, 
				background
			);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
			return 0;
		}
	}

	private static MethodHandle HANDLE_XStoreName = Lib.loadFuncHandle(
		"XStoreName", 
		Lib.C_INT32_T,
		Lib.C_POINTER_T,
		Lib.C_UINT64_T,
		Lib.C_POINTER_T
	);
	public static int XStoreName(MemoryAddress display, long window, MemoryAddress window_name) {
		try {
			return (int) HANDLE_XStoreName.invoke(display, window, window_name);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);

			return -1;
		}
	}

	private static MethodHandle HANDLE_XSelectInput = Lib.loadFuncHandle(
		"XSelectInput", 
		Lib.C_INT32_T,
		Lib.C_POINTER_T,
		Lib.C_UINT64_T,
		Lib.C_INT64_T
	);
	public static int XSelectInput(MemoryAddress display, long window, long event_mask) {
		try {
			return (int) HANDLE_XSelectInput.invoke(display, window, event_mask);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);

			return -1;
		}
	}

	private static MethodHandle HANDLE_XMapWindow = Lib.loadFuncHandle(
		"XMapWindow", 
		Lib.C_INT32_T,
		Lib.C_POINTER_T,
		Lib.C_UINT64_T
	);
	public static int XMapWindow(MemoryAddress display, long window) {
		try {
			return (int) HANDLE_XMapWindow.invoke(display, window);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);

			return -1;
		}
	}

	private static MethodHandle HANDLE_XNextEvent = Lib.loadFuncHandle(
		"XNextEvent", 
		Lib.C_INT32_T,
		Lib.C_POINTER_T,
		Lib.C_POINTER_T
	);
	public static int XNextEvent(MemoryAddress display, MemoryAddress event_return) {
		try {
			return (int) HANDLE_XNextEvent.invoke(display, event_return);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);

			return -1;
		}
	}

	private static MethodHandle HANDLE_XFree = Lib.loadFuncHandle(
		"XFree", 
		Lib.C_INT32_T,
		Lib.C_POINTER_T
	);
	public static void XFree(MemoryAddress data) {
		try {
			HANDLE_XFree.invoke(data);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static MethodHandle HANDLE_glXChooseVisual = Lib.loadFuncHandle(
		"glXChooseVisual", 
		Lib.C_POINTER_T,
		Lib.C_POINTER_T,
		Lib.C_INT32_T,
		Lib.C_POINTER_T
	);
	public static MemoryAddress glXChooseVisual(MemoryAddress display, int screen, MemoryAddress attribList) {
		try {
			return (MemoryAddress) HANDLE_glXChooseVisual.invoke(display, screen, attribList);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);

			return null;
		}
	}
	
	private static MethodHandle HANDLE_glXChooseFBConfig = Lib.loadFuncHandle(
		"glXChooseFBConfig", 
		Lib.C_POINTER_T,
		Lib.C_POINTER_T,
		Lib.C_INT32_T,
		Lib.C_POINTER_T,
		Lib.C_POINTER_T
	);
	public static MemoryAddress glXChooseFBConfig(
		MemoryAddress display, 
		int screen, 
		MemoryAddress attribList, 
		MemoryAddress nelements
	) {
		try {
			return (MemoryAddress) HANDLE_glXChooseFBConfig.invoke(display, screen, attribList, nelements);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);

			return null;
		}
	}

	private static MethodHandle HANDLE_glXGetVisualFromFBConfig = Lib.loadFuncHandle(
		"glXGetVisualFromFBConfig", 
		Lib.C_POINTER_T,
		Lib.C_POINTER_T,
		Lib.C_UINT64_T
	);
	public static MemoryAddress glXGetVisualFromFBConfig(MemoryAddress display, long config) {
		try {
			return (MemoryAddress) HANDLE_glXGetVisualFromFBConfig.invoke(display, config);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);

			return null;
		}
	}

	private static MethodHandle HANDLE_XRenderFindVisualFormat = Lib.loadFuncHandle(
		"XRenderFindVisualFormat", 
		Lib.C_POINTER_T,
		Lib.C_POINTER_T,
		Lib.C_UINT64_T
	);
	public static MemoryAddress XRenderFindVisualFormat(MemoryAddress display, long visual) {
		try {
			return (MemoryAddress) HANDLE_XRenderFindVisualFormat.invoke(display, visual);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);

			return null;
		}
	}

	private static MethodHandle HANDLE_glXCreateWindow = Lib.loadFuncHandle(
		"glXCreateWindow", 
		Lib.C_UINT64_T,
		Lib.C_POINTER_T,
		Lib.C_UINT64_T,
		Lib.C_UINT64_T,
		Lib.C_POINTER_T
	);
	public static long glXCreateWindow(MemoryAddress display, long config, long win, MemoryAddress attribList) {
		try {
			return (long) HANDLE_glXCreateWindow.invoke(display, config, win, attribList);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);

			return -1;
		}
	}

	private static MethodHandle HANDLE_glXGetProcAddress = Lib.loadFuncHandle(
		"glXGetProcAddress", 
		Lib.C_POINTER_T,
		Lib.C_POINTER_T
	);
	public static MemoryAddress glXGetProcAddress(MemoryAddress procName) {
		try {
			return (MemoryAddress) HANDLE_glXGetProcAddress.invoke(procName);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);

			return null;
		}
	}

	private static MethodHandle HANDLE_glXCreateContextAttribsARB = Lib.loadFuncHandle(
		"glXCreateContextAttribsARB", 
		Lib.C_POINTER_T,
		Lib.C_POINTER_T,
		Lib.C_INT64_T,
		Lib.C_INT32_T,
		Lib.C_INT32_T,
		Lib.C_POINTER_T
	);
	public static MemoryAddress glXCreateContextAttribsARB(
		MemoryAddress display, 
		long fb_config,
		int direct, 
		int some_bool,
		MemoryAddress attrib_list
	) {
		try {
			return (MemoryAddress) HANDLE_glXCreateContextAttribsARB.invoke(display, fb_config, some_bool, direct, attrib_list);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);

			return null;
		}
	}

	private static MethodHandle HANDLE_glXCreateNewContext = Lib.loadFuncHandle(
		"glXCreateNewContext", 
		Lib.C_POINTER_T,
		Lib.C_POINTER_T,
		Lib.C_UINT64_T,
		Lib.C_INT32_T,
		Lib.C_POINTER_T,
		Lib.C_INT32_T
	);
	public static MemoryAddress glXCreateNewContext(
		MemoryAddress display, 
		long fb_config,
		int render_type, 
		MemoryAddress share_list,
		int direct
	) {
		try {
			return (MemoryAddress) HANDLE_glXCreateNewContext.invoke(display, fb_config, render_type, share_list, direct);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);

			return null;
		}
	}

	private static MethodHandle HANDLE_glXCreateContext = Lib.loadFuncHandle(
		"glXCreateContext", 
		Lib.C_POINTER_T,
		Lib.C_POINTER_T,
		Lib.C_POINTER_T,
		Lib.C_POINTER_T,
		Lib.C_INT32_T
	);
	public static MemoryAddress glXCreateContext(
		MemoryAddress display, 
		MemoryAddress visual, 
		MemoryAddress shareList, 
		int direct
	) {
		try {
			return (MemoryAddress) HANDLE_glXCreateContext.invoke(display, visual, shareList, direct);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);

			return null;
		}
	}

	private static MethodHandle HANDLE_glXMakeContextCurrent = Lib.loadFuncHandle(
		"glXMakeContextCurrent", 
		Lib.C_INT32_T,
		Lib.C_POINTER_T,
		Lib.C_UINT64_T,	
		Lib.C_UINT64_T,
		Lib.C_POINTER_T
	);
	public static int glXMakeContextCurrent(
		MemoryAddress display, 
		long draw,
		long read, 
		MemoryAddress ctx
	) {
		try {
			return (int) HANDLE_glXMakeContextCurrent.invoke(display, draw, read, ctx);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);

			return 0;
		}
	}

	private static MethodHandle HANDLE_glXMakeCurrent = Lib.loadFuncHandle(
		"glXMakeCurrent", 
		Lib.C_INT32_T,
		Lib.C_POINTER_T,
		Lib.C_UINT64_T,
		Lib.C_POINTER_T
	);
	public static int glXMakeCurrent(MemoryAddress display, long drawable, MemoryAddress ctx) {
		try {
			return (int) HANDLE_glXMakeCurrent.invoke(display, drawable, ctx);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);

			return 0;
		}
	}


	private static MethodHandle HANDLE_glXSwapBuffers = Lib.loadFuncHandle(
		"glXSwapBuffers", 
		Lib.C_INT32_T,
		Lib.C_POINTER_T,
		Lib.C_UINT64_T
	);
	public static int glXSwapBuffers(MemoryAddress display, long window) {
		try {
			return (int) HANDLE_glXSwapBuffers.invoke(display, window);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);

			return -1;
		}
	}
}
