package othello.faskinen;

import java.lang.foreign.*;
import java.lang.invoke.MethodType;
import java.util.HashMap;

import othello.faskinen.win32.ARB;
import othello.faskinen.win32.Win32;
import othello.faskinen.win32.structs.*;

public class Win32Window extends Window {

	private static final HashMap<MemoryAddress, Win32Window> windows = new HashMap<MemoryAddress, Win32Window>();
	private static Win32Window latest;

	MemoryAddress ghWnd;
	MemoryAddress ghDC; // HDC
	MemoryAddress ghRC; // HGLRC
	MemorySegment msg = MemorySession.global().allocate(MSG.sizeOf());
	boolean shouldClose;
	public int width, height;

	public Win32Window(String name, int width, int height) {
		Win32Window.latest = this;

		MemoryAddress hInstance = Win32.GetExecutableHandle();

		MemorySegment wndClass = MemorySession.global().allocate(WNDCLASSEXW.sizeOf());

		MemorySegment winCallback = Lib.getJavaFuncPointer(
				Win32Window.class,
				"MainWndProc",
				MethodType.methodType(long.class, MemoryAddress.class, int.class, int.class, long.class),
				FunctionDescriptor.of(Lib.C_INT64_T, Lib.C_POINTER_T, Lib.C_UINT32_T, Lib.C_UINT32_T, Lib.C_INT64_T)
		);
		MemorySegment szClassName = Lib.javaToWStr("OpenGL Window");

		WNDCLASSEXW.set_cbSize(wndClass, (int) WNDCLASSEXW.sizeOf());
		WNDCLASSEXW.set_style(wndClass, 0);
		WNDCLASSEXW.set_lpfnWndProc(wndClass, winCallback.address());
		WNDCLASSEXW.set_cbClsExtra(wndClass, 0);
		WNDCLASSEXW.set_cbWndExtra(wndClass, 0);
		WNDCLASSEXW.set_hInstance(wndClass, hInstance);
		WNDCLASSEXW.set_hIcon(wndClass, Win32.LoadIconW(hInstance, szClassName.address()));
		WNDCLASSEXW.set_hCursor(wndClass, Win32.LoadCursorA(Lib.NULLPTR, MemoryAddress.ofLong(Win32.IDC_ARROW)));
		WNDCLASSEXW.set_hbrBackground(wndClass, MemoryAddress.ofLong(Win32.COLOR_WINDOW + 1));
		WNDCLASSEXW.set_lpszMenuName(wndClass, szClassName.address());
		WNDCLASSEXW.set_lpszClassName(wndClass, szClassName.address());
		WNDCLASSEXW.set_hIcon(wndClass, Lib.NULLPTR);

		if (Win32.RegisterClassExW(wndClass.address()) == 0)
		{
			System.out.println("failed to register window class");
			System.exit(1);
		}

		MemorySegment winName = Lib.javaToWStr("Hello OpenGL");
		this.ghWnd = Win32.CreateWindowExW(0,
				szClassName.address(),
				winName.address(),
				Win32.WS_OVERLAPPEDWINDOW | Win32.WS_CLIPSIBLINGS | Win32.WS_CLIPCHILDREN,
				Win32.CW_USEDEFAULT,
				Win32.CW_USEDEFAULT,
				width,
				height,
				Lib.NULLPTR,
				Lib.NULLPTR,
				hInstance,
				Lib.NULLPTR);

		if (this.ghWnd.toRawLongValue() == 0)
		{
			System.out.println("failed to create window");
			int error = Win32.GetLastError();
			System.out.println("error code = " + error + ", trans = " + Win32.HRESULT_FROM_WIN32(error));
			System.exit(1);
		}

		Win32Window.windows.put(this.ghWnd, this);
	}

	@Override
	public void show() {
		Win32.ShowWindow(this.ghWnd, 5);
	}

	@Override
	public void hide() {
		Win32.ShowWindow(this.ghWnd, 0);
	}

	@Override
	public void pollEvents() {
		while (Win32.PeekMessageW(msg.address(), Lib.NULLPTR, 0, 0, Win32.PM_NOREMOVE))
		{
			if (Win32.GetMessageW(msg.address(), Lib.NULLPTR, 0, 0))
			{
				Win32.TranslateMessage(msg.address());
				Win32.DispatchMessageW(msg.address());
			}
			else
			{
				this.shouldClose = true;
				throw new RuntimeException("fug");
			}
		}
	}

	@Override
	public void swapBuffers() {
		Win32.UpdateWindow(this.ghWnd);
	}

	@Override
	public void makeContextCurrent() {
		Win32.wglMakeCurrent(this.ghDC, this.ghRC);
	}

	public void destroy() {
		Win32.DestroyWindow(this.ghWnd);
	}

	// TODO
	public static boolean bSetupPixelFormat(MemoryAddress hdc)
	{
		SegmentAllocator alloc = MemorySession.openConfined();
		MemorySegment pfd = alloc.allocate(PIXELFORMATDESCRIPTOR.size());
		MemoryAddress ppfd = pfd.address();
		int pixelformat;

		PIXELFORMATDESCRIPTOR.set_nSize(pfd, (short) PIXELFORMATDESCRIPTOR.size());
		PIXELFORMATDESCRIPTOR.set_nVersion(pfd, (short) 1);
		PIXELFORMATDESCRIPTOR.set_dwFlags(pfd,
				0x00000004 | 0x00000020 | 0x00000001
		);
		PIXELFORMATDESCRIPTOR.set_iPixelType(pfd, (byte) 0); //
		PIXELFORMATDESCRIPTOR.set_cColorBits(pfd, (byte) 32);
		{
			PIXELFORMATDESCRIPTOR.set_cRedBits(pfd, (byte) 8);
			PIXELFORMATDESCRIPTOR.set_cRedShift(pfd, (byte) 24);
			PIXELFORMATDESCRIPTOR.set_cGreenBits(pfd, (byte) 8);
			PIXELFORMATDESCRIPTOR.set_cGreenShift(pfd, (byte) 16);
			PIXELFORMATDESCRIPTOR.set_cBlueBits(pfd, (byte) 8);
			PIXELFORMATDESCRIPTOR.set_cBlueShift(pfd, (byte) 8);
			PIXELFORMATDESCRIPTOR.set_cAlphaBits(pfd, (byte) 8);
			PIXELFORMATDESCRIPTOR.set_cAlphaShift(pfd, (byte) 0);
		}
		PIXELFORMATDESCRIPTOR.set_cDepthBits(pfd, (byte) 24);
		PIXELFORMATDESCRIPTOR.set_cStencilBits(pfd, (byte) 8);
		PIXELFORMATDESCRIPTOR.set_cAccumBits(pfd, (byte) 0);


		pixelformat = Win32.ChoosePixelFormat(hdc, ppfd);

		if (pixelformat == 0) {
			return false;
		}

		if (Win32.SetPixelFormat(hdc, pixelformat, ppfd) == false) {
			return false;
		}

		return true;
	}


	// LONG WINAPI MainWndProc (HWND, UINT, WPARAM, LPARAM);
	// WPARAM is UINT_PTR is uint32_t
	// LPARAM is LONG_PTR is int64_t
	// i want to die
	public static long MainWndProc(MemoryAddress hWnd, int uMsg, int wParam, long lParam)
	{
		long lRet = 1;

		Win32Window window = windows.getOrDefault(hWnd, Win32Window.latest);

		MemorySession local = MemorySession.openImplicit();
		MemorySegment ps = local.allocate(PAINTSTRUCT.sizeOf());
		MemorySegment rect = local.allocate(RECT.size());

		switch (uMsg)
		{
			case Win32.WM_CREATE:
				window.ghDC = Win32.GetDC(hWnd);
				if (!bSetupPixelFormat(window.ghDC))
		            Win32.PostQuitMessage(0);
		        window.ghRC = Win32.wglCreateContext(window.ghDC);
				window.makeContextCurrent();

				MemorySegment attribs = local.allocate(Lib.C_INT32_T.byteSize() * 5);
				attribs.setAtIndex(ValueLayout.JAVA_INT, 0, 0x821B); // version major
				attribs.setAtIndex(ValueLayout.JAVA_INT, 1, 4);
				attribs.setAtIndex(ValueLayout.JAVA_INT, 2, 0x821C); // version minor
				attribs.setAtIndex(ValueLayout.JAVA_INT, 3, 5);
				attribs.setAtIndex(ValueLayout.JAVA_INT, 4, 0);

				window.ghRC = ARB.wglCreateContextAttribsARB(window.ghDC, MemoryAddress.NULL, attribs.address());
//		        Win32.wglMakeCurrent(window.ghDC, window.ghRC);
//		        Win32.GetClientRect(hWnd, &rect);
//		        Win32.initializeGL(rect.right, rect.bottom);
				break;

			case Win32.WM_PAINT:
				Win32.BeginPaint(hWnd, ps.address());
		        Win32.EndPaint(hWnd, ps.address());
				break;
//
			case Win32.WM_SIZE:
				Win32.GetClientRect(hWnd, rect.address());
		        window.width = (int) RECT.get_right(rect);
				window.height = (int) RECT.get_bottom(rect);
				break;

			case Win32.WM_CLOSE:
				if (window.ghRC.toRawLongValue() != 0)
		            Win32.wglDeleteContext(window.ghRC);
		        if (window.ghDC.toRawLongValue() != 0)
		            Win32.ReleaseDC(hWnd, window.ghDC);
//		        ghRC = 0;
//		        ghDC = 0;

				Win32.DestroyWindow(hWnd);
				break;

			case Win32.WM_DESTROY:
			if (window.ghRC.toRawLongValue() != 0)
	            Win32.wglDeleteContext(window.ghRC);
	        if (window.ghDC.toRawLongValue() != 0)
	            Win32.ReleaseDC(hWnd, window.ghDC);
//
				Win32.PostQuitMessage(0);
				break;

			case Win32.WM_KEYDOWN:
				switch (wParam) {
					default:
						break;
				}
				break;

			default:
				lRet = Win32.DefWindowProcW(hWnd, uMsg, wParam, lParam);
				break;
		}

		return lRet;
	}
}
