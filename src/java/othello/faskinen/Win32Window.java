package othello.faskinen;

import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.MemoryAddress;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.MemorySession;
import java.lang.invoke.MethodType;

import othello.faskinen.win32.Test;
import othello.faskinen.win32.Win32;
import othello.faskinen.win32.structs.WNDCLASSEXW;

public class Win32Window extends Window {
	MemoryAddress ghWnd;

	public Win32Window() {
		MemoryAddress instance = Win32.GetExecutableHandle();
		MemorySegment windowClass = MemorySession.global().allocate(WNDCLASSEXW.sizeOf());
		MemorySegment winCallback = Lib.getJavaFuncPointer(
                Test.class,
                "MainWndProc",
                MethodType.methodType(long.class, MemoryAddress.class, int.class, int.class, long.class),
                FunctionDescriptor.of(Lib.C_INT64_T, Lib.C_POINTER_T, Lib.C_UINT32_T, Lib.C_UINT32_T, Lib.C_INT64_T)
        );
		MemorySegment windowName = Lib.javaToWStr("OpenGL Window");

		WNDCLASSEXW.set_cbSize(windowClass, (int) WNDCLASSEXW.sizeOf());
		WNDCLASSEXW.set_style(windowClass, 0);
		WNDCLASSEXW.set_lpfnWndProc(windowClass, winCallback.address());
		WNDCLASSEXW.set_cbClsExtra(windowClass, 0);
		WNDCLASSEXW.set_cbWndExtra(windowClass, 0);
		WNDCLASSEXW.set_hInstance(windowClass, instance);
		WNDCLASSEXW.set_hIcon(windowClass, Win32.LoadIconW(instance, Lib.NULLPTR));
		WNDCLASSEXW.set_hCursor(windowClass, Win32.LoadCursorA(Lib.NULLPTR, MemoryAddress.ofLong(Win32.IDC_ARROW)));
		WNDCLASSEXW.set_hbrBackground(windowClass, MemoryAddress.ofLong(Win32.COLOR_WINDOW + 1));
		WNDCLASSEXW.set_lpszMenuName(windowClass, windowName.address());
		WNDCLASSEXW.set_lpszClassName(windowClass, windowName.address());
        WNDCLASSEXW.set_hIcon(windowClass, Lib.NULLPTR);

		if (Win32.RegisterClassExW(windowClass.address()) == 0) {
			throw new RuntimeException("failed to register window class");
		}

		this.ghWnd = Win32.CreateWindowExW(
			0,
			windowName.address(),
			windowName.address(),
            Win32.WS_OVERLAPPEDWINDOW | Win32.WS_CLIPSIBLINGS | Win32.WS_CLIPCHILDREN,
			Win32.CW_USEDEFAULT,
			Win32.CW_USEDEFAULT,
			0,
			0,
			Lib.NULLPTR,
			Lib.NULLPTR,
			instance,
			Lib.NULLPTR
		);

		if (this.ghWnd == Lib.NULLPTR) {
			int error = Win32.GetLastError();
			int hresult = Win32.HRESULT_FROM_WIN32(error);
			throw new RuntimeException("failed to create window with error code " + error + " and trans " + hresult);
		}
	}

	public void destroy() {
		Win32.DestroyWindow(this.ghWnd);
	}
}
