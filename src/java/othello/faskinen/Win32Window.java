package othello.faskinen;

import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.MemoryAddress;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.MemorySession;
import java.lang.invoke.MethodType;

import othello.faskinen.win32.Test;
import othello.faskinen.win32.Win32;
import othello.faskinen.win32.structs.MSG;
import othello.faskinen.win32.structs.WNDCLASSEXW;

public class Win32Window extends Window {
	MemoryAddress ghWnd;
	MemorySegment msg = MemorySession.global().allocate(MSG.sizeOf());

	public Win32Window(String name, int width, int height) {
		MemoryAddress hInstance = Win32.GetExecutableHandle();

		MemorySegment wndClass = MemorySession.global().allocate(WNDCLASSEXW.sizeOf());

		MemorySegment winCallback = Lib.getJavaFuncPointer(
				Test.class,
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
}
