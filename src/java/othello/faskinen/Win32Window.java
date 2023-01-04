package othello.faskinen;

import java.lang.foreign.*;
import java.lang.invoke.MethodType;

import othello.faskinen.win32.Win32;
import othello.faskinen.win32.structs.MSG;
import othello.faskinen.win32.structs.PIXELFORMATDESCRIPTOR;
import othello.faskinen.win32.structs.WNDCLASSEXW;

public class Win32Window extends Window {
	MemoryAddress ghWnd;
	MemoryAddress ghDC; // HDC
	MemoryAddress ghRC; // HGLRC
	MemorySegment msg = MemorySession.global().allocate(MSG.sizeOf());
	boolean shouldClose;

	public Win32Window(String name, int width, int height) {
		MemoryAddress hInstance = Win32.GetExecutableHandle();

		MemorySegment wndClass = MemorySession.global().allocate(WNDCLASSEXW.sizeOf());

		MemorySegment winCallback = Lib.getJavaFuncPointer(
				this.getClass(),
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

		this.shouldClose = false;
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

	}

	public void destroy() {
		Win32.DestroyWindow(this.ghWnd);
	}

	// TODO
	public static boolean bSetupPixelFormat(MemoryAddress address)
	{
		SegmentAllocator alloc = MemorySession.openImplicit();
		MemorySegment pfd = alloc.allocate(PIXELFORMATDESCRIPTOR.size());
		MemoryAddress ppfd = pfd.address();

		PIXELFORMATDESCRIPTOR.set_nSize(pfd, (short) PIXELFORMATDESCRIPTOR.size());
		PIXELFORMATDESCRIPTOR.set_nVersion(pfd, (short) 1);
		PIXELFORMATDESCRIPTOR.set_dwFlags(pfd,
				0x00000004 | 0x00000020 | 0x00000001
		);
//		PIXELFORMATDESCRIPTOR.set_();
	}


	// LONG WINAPI MainWndProc (HWND, UINT, WPARAM, LPARAM);
	// WPARAM is UINT_PTR is uint32_t
	// LPARAM is LONG_PTR is int64_t
	// i want to die
	public long MainWndProc(MemoryAddress hWnd, int uMsg, int wParam, long lParam)
	{
		long lRet = 1;

		switch (uMsg)
		{
			case Win32.WM_CREATE:
				this.ghDC = Win32.GetDC(hWnd);
				if (!bSetupPixelFormat(ghDC))
		            Win32.PostQuitMessage(0);
//		        this.ghRC = Win32.wglCreateContext(ghDC);
//		        Win32.wglMakeCurrent(ghDC, ghRC);
//		        Win32.GetClientRect(hWnd, &rect);
//		        Win32.initializeGL(rect.right, rect.bottom);
				break;

//			case Win32.WM_PAINT:
//				BeginPaint(hWnd, &ps);
//		        EndPaint(hWnd, &ps);
//				break;
//
//			case Win32.WM_SIZE:
//				GetClientRect(hWnd, &rect);
//		        resize(rect.right, rect.bottom);
//				break;

			case Win32.WM_CLOSE:
	//			if (ghRC)
	//	            wglDeleteContext(ghRC);
	//	        if (ghDC)
	//	            ReleaseDC(hWnd, ghDC);
	//	        ghRC = 0;
	//	        ghDC = 0;
//
				Win32.DestroyWindow(hWnd);
				break;

			case Win32.WM_DESTROY:
//			if (ghRC)
//	            wglDeleteContext(ghRC);
//	        if (ghDC)
//	            ReleaseDC(hWnd, ghDC);
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
