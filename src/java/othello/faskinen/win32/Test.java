package othello.faskinen.win32;

import othello.faskinen.Lib;
import othello.faskinen.win32.structs.MSG;
import othello.faskinen.win32.structs.WNDCLASSEXW;

import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.MemoryAddress;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.MemorySession;
import java.lang.invoke.MethodType;

public class Test {

    static MemoryAddress ghWnd;
    static MemoryAddress ghDC;
    static MemoryAddress ghRC;

    public static void main(String[] args)
    {
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
        ghWnd = Win32.CreateWindowExW(0,
                szClassName.address(),
                winName.address(),
                Win32.WS_OVERLAPPEDWINDOW | Win32.WS_CLIPSIBLINGS | Win32.WS_CLIPCHILDREN,
                Win32.CW_USEDEFAULT,
                Win32.CW_USEDEFAULT,
                1280,
                720,
                Lib.NULLPTR,
                Lib.NULLPTR,
                hInstance,
                Lib.NULLPTR);

        if (ghWnd.toRawLongValue() == 0)
        {
            System.out.println("failed to create window");
            int error = Win32.GetLastError();
            System.out.println("error code = " + error + ", trans = " + Win32.HRESULT_FROM_WIN32(error));
            System.exit(1);
        }
    }

    // LONG WINAPI MainWndProc (HWND, UINT, WPARAM, LPARAM);
    // WPARAM is UINT_PTR is uint32_t
    // LPARAM is LONG_PTR is int64_t
    // i want to die
    public static long MainWndProc(MemoryAddress hWnd, int uMsg, int wParam, long lParam)
    {
        long lRet = 1;

        switch (uMsg)
        {
//		case Win32.WM_CREATE:
//			ghDC = GetDC(hWnd);
//			if (!bSetupPixelFormat(ghDC))
//	            PostQuitMessage (0);
//	        ghRC = wglCreateContext(ghDC);
//	        wglMakeCurrent(ghDC, ghRC);
//	        GetClientRect(hWnd, &rect);
//	        initializeGL(rect.right, rect.bottom);
//			break;

//		case Win32.WM_PAINT:
//			BeginPaint(hWnd, &ps);
//	        EndPaint(hWnd, &ps);
//			break;

//		case Win32.WM_SIZE:
//			GetClientRect(hWnd, &rect);
//	        resize(rect.right, rect.bottom);
//			break;

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

//		case Win32.WM_KEYDOWN:
//			switch (wParam) {
//				default:
//					break;
//			}
//			break;

            default:
                lRet = Win32.DefWindowProcW(hWnd, uMsg, wParam, lParam);
                break;
        }

        return lRet;
    }


}
