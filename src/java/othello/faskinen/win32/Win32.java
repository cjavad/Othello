package othello.faskinen.win32;

import othello.faskinen.Lib;

import java.lang.foreign.MemoryAddress;
import java.lang.foreign.SegmentAllocator;
import java.lang.invoke.MethodHandle;

public class Win32 {

    // https://learn.microsoft.com/en-us/windows/win32/winprog/windows-data-types

    static
    {
        System.loadLibrary("kernel32");
        System.loadLibrary("user32");
    }

    public static final long COLOR_WINDOW = 5;

    public static final long
            IDC_ARROW			= 32512L;

    public static final int
            WS_CAPTION 			= 0x00C00000,
            WS_CLIPCHILDREN 	= 0x02000000,
            WS_CLIPSIBLINGS 	= 0x04000000,
            WS_MAXIMIZEBOX 		= 0x00010000,
            WS_MINIMIZEBOX 		= 0x00020000,
            WS_OVERLAPPED 		= 0x00000000,
            WS_SYSMENU 			= 0x00080000,
            WS_THICKFRAME 		= 0x00040000;

    public static final int
            WS_OVERLAPPEDWINDOW = WS_OVERLAPPED | WS_CAPTION | WS_SYSMENU | WS_THICKFRAME | WS_MINIMIZEBOX | WS_MAXIMIZEBOX;

    public static final int
            CW_USEDEFAULT = 0x80000000;

    public static final int
            PM_NOREMOVE			= 0x0000,
            PM_REMOVE			= 0x0001,
            PM_NOYIELD			= 0x0002;

    public static final int
            WM_NULL				= 0x0000,
            WM_CREATE			= 0x0001,
            WM_DESTROY			= 0x0002,
            WM_MOVE				= 0x0003,
            WM_SIZE				= 0x0005,

    WM_ACTIVATE			= 0x0006,
            WM_INACTIVE			= 0,
            WM_ACTIVE			= 1,
            WM_CLICKACTIVE		= 2,

    WM_SETFOCUS			= 0x0007,
            WM_KILLFOCUS		= 0x0008,
            WM_ENALBE			= 0x000A,
            WM_SETDRAW			= 0x000B,
            WM_SETTEXT			= 0x000C,
            WM_GETTEXT			= 0x000D,
            WM_GETTTEXTLENGTH	= 0x000E,
            WM_PAINT			= 0x000F,
            WM_CLOSE			= 0x0010,

    WM_NCCREATE			= 0x0081,
            WM_NCDESTROY		= 0x0082,
            WM_NCCALCSIZE		= 0x0083,
            WM_NCHITTEST		= 0x0084,
            WM_NCPAINT			= 0x0085,
            WM_NCACTIVATE		= 0x0086,

    WM_NCMOUSEMOVE		= 0x00A0,
            WM_NCLBUTTONDOWN	= 0x00A1,
            WN_NCLBUTTONUP		= 0x00A2,
            WN_NCLBUTTONDBLCLK	= 0x00A3,
            WM_NCRBUTTONDOWN	= 0x00A4,
            WM_NCRBUTTONUP		= 0x00A5,
            WM_NCRBUTTONDBLCLK	= 0x00A6,
            WM_NCMBUTTONDOWN	= 0x00A7,
            WN_NCMBUTTONUP		= 0x00A8,
            WM_NCMBUTTONDBLCLK	= 0x00A9,

    WM_KEYDOWN			= 0x0100,
            WM_KEYUP			= 0x0101,
            WM_CHAR				= 0x0102,
            WM_DEADCHAR			= 0x0103,
            WM_SYSKEYDOWN		= 0x0104,
            WM_SYSKEYUP			= 0x0105,
            WM_SYSCHAR			= 0x0106,
            WM_SYSDEADCHAR		= 0x0107;



	/*
	HWND CreateWindowExW(
		[in]           DWORD     dwExStyle,
		[in, optional] LPCWSTR   lpClassName,
		[in, optional] LPCWSTR   lpWindowName,
		[in]           DWORD     dwStyle,
		[in]           int       X,
		[in]           int       Y,
		[in]           int       nWidth,
		[in]           int       nHeight,
		[in, optional] HWND      hWndParent,
		[in, optional] HMENU     hMenu,
		[in, optional] HINSTANCE hInstance,
		[in, optional] LPVOID    lpParam
	);
	*/

    private static MethodHandle HANDLE_CreateWindowExW = Lib.loadFuncHandle("CreateWindowExW", Lib.C_POINTER_T,
            Lib.C_UINT32_T, Lib.C_POINTER_T, Lib.C_POINTER_T, Lib.C_UINT32_T,
            Lib.C_INT32_T, Lib.C_INT32_T, Lib.C_INT32_T, Lib.C_INT32_T,
            Lib.C_POINTER_T, Lib.C_POINTER_T, Lib.C_POINTER_T, Lib.C_POINTER_T);


    public static MemoryAddress CreateWindowExW(SegmentAllocator alloc,
                                                int i_dwExStyle, String i0_lpClassName, String i0_lpWindowName, int i_dwStyle,
                                                int i_X, int i_Y, int i_nWidth, int i_nHeight,
                                                MemoryAddress i0_hWndParent, MemoryAddress i0_hMenu, MemoryAddress i0_hInstance, MemoryAddress i0_lpParam)
    {
        return CreateWindowExW(i_dwExStyle, Lib.javaToWStr(alloc, i0_lpClassName).address(), Lib.javaToWStr(alloc, i0_lpWindowName).address(), i_dwStyle,
                i_X, i_Y, i_nWidth, i_nHeight, i0_hWndParent, i0_hMenu, i0_hInstance, i0_lpParam);
    }

    public static MemoryAddress CreateWindowExW(
            int i_dwExStyle, MemoryAddress i0_lpClassName, MemoryAddress i0_lpWindowName, int i_dwStyle,
            int i_X, int i_Y, int i_nWidth, int i_nHeight,
            MemoryAddress i0_hWndParent, MemoryAddress i0_hMenu, MemoryAddress i0_hInstance, MemoryAddress i0_lpParam
    )
    {
        try {
            // figure out sending strings
            return (MemoryAddress) HANDLE_CreateWindowExW.invoke(
                    i_dwExStyle,
                    i0_lpClassName,
                    i0_lpWindowName,
                    i_dwStyle,
                    i_X,
                    i_Y,
                    i_nWidth,
                    i_nHeight,
                    i0_hWndParent,
                    i0_hMenu,
                    i0_hInstance,
                    i0_lpParam);
        } catch (Throwable e) {
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }

	/*
	BOOL ShowWindow(
		[in] HWND hWnd,
		[in] int  nCmdShow
	);
	*/

    private static MethodHandle HANDLE_ShowWindow = Lib.loadFuncHandle("ShowWindow", Lib.C_BOOL_T,
            Lib.C_POINTER_T, Lib.C_INT32_T);

    public static boolean ShowWindow(MemoryAddress i_hWnd, int i_nCmdShow)
    {
        try {
            return ((int) HANDLE_ShowWindow.invoke(i_hWnd, i_nCmdShow) != 0);
        } catch (Throwable e) {
            e.printStackTrace();
            System.exit(1);
        }
        return false;
    }

	/*
	HMODULE GetModuleHandleW(
		[in, optional] LPCWSTR lpModuleName
	);
	*/

    private static MethodHandle HANDLE_GetModuleHandleW = Lib.loadFuncHandle("GetModuleHandleW", Lib.C_POINTER_T,
            Lib.C_POINTER_T);

    public static MemoryAddress GetExecutableHandle()
    {
        try {
            return (MemoryAddress) HANDLE_GetModuleHandleW.invoke(Lib.NULLPTR);
        } catch (Throwable e) {
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }

    public static MemoryAddress GetModuleHandleW(SegmentAllocator alloc, String i_lpModuleName)
    {
        return GetModuleHandleW(Lib.javaToWStr(alloc, i_lpModuleName).address());
    }

    public static MemoryAddress GetModuleHandleW(MemoryAddress i_lpModuleName)
    {
        try {
            return (MemoryAddress) HANDLE_GetModuleHandleW.invoke(i_lpModuleName);
        } catch (Throwable e) {
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }

	/*
	HICON LoadIconW(
		[in, optional] HINSTANCE hInstance,
		[in]           LPCWSTR   lpIconName
	);
	*/

    private static MethodHandle HANDLE_LoadIconW = Lib.loadFuncHandle("LoadIconW", Lib.C_POINTER_T,
            Lib.C_POINTER_T, Lib.C_POINTER_T);

    public static MemoryAddress LoadIconW(SegmentAllocator alloc, MemoryAddress i0_hInstance, String i_lpIconName)
    {
        return LoadIconW(i0_hInstance, Lib.javaToWStr(alloc, i_lpIconName).address());
    }

    public static MemoryAddress LoadIconW(MemoryAddress i0_hInstance, MemoryAddress i_lpIconName)
    {
        try {
            return (MemoryAddress) HANDLE_LoadIconW.invoke(i0_hInstance, i_lpIconName);
        } catch (Throwable e) {
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }

	/*
	HCURSOR LoadCursorW(
		[in, optional] HINSTANCE hInstance,
		[in]           LPCWSTR   lpCursorName
	);
	*/

    private static MethodHandle HANDLE_LoadCursorW = Lib.loadFuncHandle("LoadCursorW", Lib.C_POINTER_T,
            Lib.C_POINTER_T, Lib.C_POINTER_T);

    public static MemoryAddress LoadCursorW(SegmentAllocator alloc, MemoryAddress i0_hInstance, String i_lpCursorName)
    {
        return LoadCursorW(i0_hInstance, Lib.javaToWStr(alloc, i_lpCursorName).address());
    }

    public static MemoryAddress LoadCursorW(MemoryAddress i0_hInstance, MemoryAddress i_lpCursorName)
    {
        try {
            return (MemoryAddress) HANDLE_LoadCursorW.invoke(i0_hInstance, i_lpCursorName);
        } catch (Throwable e) {
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }

	/*
	HCURSOR LoadCursorA(
		[in, optional] HINSTANCE hInstance,
		[in]           LPCSTR    lpCursorName
	);
	*/

    private static MethodHandle HANDLE_LoadCursorA = Lib.loadFuncHandle("LoadCursorA", Lib.C_POINTER_T,
            Lib.C_POINTER_T, Lib.C_POINTER_T);

    public static MemoryAddress LoadCursorA(SegmentAllocator alloc, MemoryAddress i0_hInstance, String i_lpCursorName)
    {
        return LoadCursorA(i0_hInstance, Lib.javaToStr(alloc, i_lpCursorName).address());
    }

    public static MemoryAddress LoadCursorA(MemoryAddress i0_hInstance, MemoryAddress i_lpCursorName)
    {
        try {
            return (MemoryAddress) HANDLE_LoadCursorA.invoke(i0_hInstance, i_lpCursorName);
        } catch (Throwable e) {
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }

	/*
	ATOM RegisterClassExW(
		[in] const WNDCLASSEXW *unnamedParam1
	);
	*/

    private static MethodHandle HANDLE_RegisterClassExW = Lib.loadFuncHandle("RegisterClassExW", Lib.C_UINT16_T,
            Lib.C_POINTER_T);

    public static short RegisterClassExW(MemoryAddress i_winClass)
    {
        try {
            return (short) HANDLE_RegisterClassExW.invoke(i_winClass);
        } catch (Throwable e) {
            e.printStackTrace();
            System.exit(1);
        }
        return 0;
    }

	/*
	BOOL UpdateWindow(
		[in] HWND hWnd
	);
	*/

    private static MethodHandle HANDLE_UpdateWindow = Lib.loadFuncHandle("UpdateWindow", Lib.C_BOOL_T,
            Lib.C_POINTER_T);

    public static boolean UpdateWindow(MemoryAddress i_hWnd)
    {
        try {
            return ((int)HANDLE_UpdateWindow.invoke(i_hWnd) != 0);
        } catch (Throwable e) {
            e.printStackTrace();
            System.exit(1);
        }
        return false;
    }

	/*
	LRESULT DefWindowProcW(
		[in] HWND   hWnd,
		[in] UINT   Msg,
		[in] WPARAM wParam,
		[in] LPARAM lParam
	);
	*/

    private static MethodHandle HANDLE_DefWindowProcW = Lib.loadFuncHandle("DefWindowProcW", Lib.C_UINT64_T,
            Lib.C_POINTER_T, Lib.C_UINT32_T, Lib.C_UINT32_T, Lib.C_INT64_T);

    public static long DefWindowProcW(MemoryAddress i_hWnd, int i_Msg, int i_wParam, long i_lParam)
    {
        try {
            return (long) HANDLE_DefWindowProcW.invoke(i_hWnd, i_Msg, i_wParam, i_lParam);
        } catch (Throwable e) {
            e.printStackTrace();
            System.exit(1);
        }
        return 0;
    }

	/*
	DWORD GetLastError();
	 */

    private static MethodHandle HANDLE_GetLastError = Lib.loadFuncHandle("GetLastError", Lib.C_UINT32_T);

    public static int GetLastError()
    {
        try {
            return (int) HANDLE_GetLastError.invoke();
        } catch (Throwable e) {
            e.printStackTrace();
            System.exit(1);
        }
        return 0;
    }

    public static int HRESULT_FROM_WIN32(int error)
    {
        return error <= 0 ? error : (error & 0x0000FFFF) | (7 << 16) | 0x80000000;
    }

	/*
	BOOL DestroyWindow(
		[in] HWND hWnd
	);
	*/

    private static MethodHandle HANDLE_DestroyWindow = Lib.loadFuncHandle("DestroyWindow", Lib.C_BOOL_T,
            Lib.C_POINTER_T);

    public static boolean DestroyWindow(MemoryAddress i_hWnd)
    {
        try {
            return (int)HANDLE_DestroyWindow.invoke(i_hWnd) != 0;
        } catch (Throwable e) {
            e.printStackTrace();
            System.exit(1);
        }
        return false;
    }

	/*
	void PostQuitMessage(
		[in] int nExitCode
	);
	*/

    private static MethodHandle HANDLE_PostQuitMessage = Lib.loadFuncHandle("PostQuitMessage", null,
            Lib.C_INT32_T);

    public static void PostQuitMessage(int i_nExitCode)
    {
        try {
            HANDLE_PostQuitMessage.invoke(i_nExitCode);
        } catch (Throwable e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

	/*
	BOOL PeekMessageW(
		[out]          LPMSG lpMsg,
		[in, optional] HWND  hWnd,
		[in]           UINT  wMsgFilterMin,
		[in]           UINT  wMsgFilterMax,
		[in]           UINT  wRemoveMsg
	);
	*/

    private static MethodHandle HANDLE_PeekMessageW = Lib.loadFuncHandle("PeekMessageW", Lib.C_BOOL_T,
            Lib.C_POINTER_T, Lib.C_POINTER_T, Lib.C_UINT32_T, Lib.C_UINT32_T, Lib.C_UINT32_T);

    public static boolean PeekMessageW(MemoryAddress o_lpMsg, MemoryAddress i0_hWnd, int i_wMsgFilterMin, int i_wMsgFilterMax, int i_wRemoveMsg)
    {
        try {
            return (int)HANDLE_PeekMessageW.invoke(o_lpMsg, i0_hWnd, i_wMsgFilterMin, i_wMsgFilterMax, i_wRemoveMsg) != 0;
        } catch (Throwable e) {
            e.printStackTrace();
            System.exit(1);
        }
        return false;
    }

	/*
	BOOL GetMessageW(
		[out]          LPMSG lpMsg,
		[in, optional] HWND  hWnd,
		[in]           UINT  wMsgFilterMin,
		[in]           UINT  wMsgFilterMax
	);
	*/

    private static MethodHandle HANDLE_GetMessageW = Lib.loadFuncHandle("GetMessageW", Lib.C_BOOL_T,
            Lib.C_POINTER_T, Lib.C_POINTER_T, Lib.C_UINT32_T, Lib.C_UINT32_T);

    public static boolean GetMessageW(MemoryAddress o_lpMsg, MemoryAddress i0_hWnd, int i_wMsgFilterMin, int i_wMsgFilterMax)
    {
        try {
            return (int)HANDLE_GetMessageW.invoke(o_lpMsg, i0_hWnd, i_wMsgFilterMin, i_wMsgFilterMax) != 0;
        } catch (Throwable e) {
            e.printStackTrace();
            System.exit(1);
        }
        return false;
    }

	/*
	BOOL TranslateMessage(
		[in] const MSG *lpMsg
	);
	*/

    private static MethodHandle HANDLE_TranslateMessage = Lib.loadFuncHandle("TranslateMessage", Lib.C_BOOL_T,
            Lib.C_POINTER_T);

    public static boolean TranslateMessage(MemoryAddress i_lpMsg)
    {
        try {
            return (int)HANDLE_TranslateMessage.invoke(i_lpMsg) != 0;
        } catch (Throwable e) {
            e.printStackTrace();
            System.exit(1);
        }
        return false;
    }

	/*
	LRESULT DispatchMessageW(
		[in] const MSG *lpMsg
	);
	*/

    private static MethodHandle HANDLE_DispatchMessageW = Lib.loadFuncHandle("DispatchMessageW", Lib.C_UINT64_T,
            Lib.C_POINTER_T);

    public static long DispatchMessageW(MemoryAddress i_lpMsg)
    {
        try {
            return (long)HANDLE_DispatchMessageW.invoke(i_lpMsg);
        } catch (Throwable e) {
            e.printStackTrace();
            System.exit(1);
        }
        return 0;
    }
}

