package othello.faskinen.x11;

import java.lang.foreign.MemoryAddress;
import java.lang.invoke.MethodHandle;

import othello.faskinen.Lib;

public class X11 {
	static {
		System.loadLibrary("X11");
	}

	// x11 masks
	public static final long 
		ExposureMask = 1 << 15,
		KeyPressMask = 1 << 0;

	public static final int
		GLX_RGBA = 4,
		GLX_DOUBLEBUFFER = 5,
		GLX_DEPTH_SIZE = 12,
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

	private static MethodHandle HANDLE_glXMakeCurrent = Lib.loadFuncHandle(
		"glXMakeCurrent", 
		Lib.C_INT32_T,
		Lib.C_POINTER_T,
		Lib.C_UINT64_T,
		Lib.C_POINTER_T
	);
	public static int glXMakeCurrent(MemoryAddress display, long window, MemoryAddress ctx) {
		try {
			return (int) HANDLE_glXMakeCurrent.invoke(display, window, ctx);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);

			return -1;
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
