package othello.faskinen.win32.structs;

import othello.faskinen.Lib;

import java.lang.foreign.GroupLayout;
import java.lang.foreign.MemoryAddress;
import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.invoke.VarHandle;

public class WNDCLASSEXW {

	/*
	UINT      cbSize;
	UINT      style;
	WNDPROC   lpfnWndProc;
	int       cbClsExtra;
	int       cbWndExtra;
	HINSTANCE hInstance;
	HICON     hIcon;
	HCURSOR   hCursor;
	HBRUSH    hbrBackground;
	LPCWSTR   lpszMenuName;
	LPCWSTR   lpszClassName;
	HICON     hIconSm;
	*/

    public static final GroupLayout layout = MemoryLayout.structLayout(
            Lib.C_UINT32_T("cbSize"),
            Lib.C_UINT32_T("style"),
            Lib.C_POINTER_T("lpfnWndProc"),
            Lib.C_INT32_T("cbClsExtra"),
            Lib.C_INT32_T("cbWndExtra"),
            Lib.C_POINTER_T("hInstance"),
            Lib.C_POINTER_T("hIcon"),
            Lib.C_POINTER_T("hCursor"),
            Lib.C_POINTER_T("hbrBackground"),
            Lib.C_POINTER_T("lpszMenuName"),
            Lib.C_POINTER_T("lpszClassName"),
            Lib.C_POINTER_T("hIconSm")
    );

    private static final VarHandle
            cbSize			= Lib.getStructField(layout, "cbSize"),
            style			= Lib.getStructField(layout, "style"),
            lpfnWndProc		= Lib.getStructField(layout, "lpfnWndProc"),
            cbClsExtra		= Lib.getStructField(layout, "cbClsExtra"),
            cbWndExtra		= Lib.getStructField(layout, "cbWndExtra"),
            hInstance		= Lib.getStructField(layout, "hInstance"),
            hIcon			= Lib.getStructField(layout, "hIcon"),
            hCursor			= Lib.getStructField(layout, "hCursor"),
            hbrBackground	= Lib.getStructField(layout, "hbrBackground"),
            lpszMenuName	= Lib.getStructField(layout, "lpszMenuName"),
            lpszClassName	= Lib.getStructField(layout, "lpszClassName"),
            hIconSm			= Lib.getStructField(layout, "hIconSm")
                    ;

    public static final long SIZE = WNDCLASSEXW.layout.byteSize();
    public static long sizeOf() { return SIZE; }

    public static MemorySegment index(MemorySegment structArray, int index) { return structArray.asSlice(index * SIZE, SIZE); }

    public static int get_cbSize(MemorySegment struct) { return (int) WNDCLASSEXW.cbSize.get(struct); }
    public static void set_cbSize(MemorySegment struct, int cbSize) { WNDCLASSEXW.cbSize.set(struct, cbSize); }

    public static int get_style(MemorySegment struct) { return (int) WNDCLASSEXW.style.get(struct); }
    public static void set_style(MemorySegment struct, int style) { WNDCLASSEXW.style.set(struct, style); }

    public static MemoryAddress get_lpfnWndProc(MemorySegment struct) { return (MemoryAddress) WNDCLASSEXW.lpfnWndProc.get(struct); }
    public static void set_lpfnWndProc(MemorySegment struct, MemoryAddress lpfnWndProc) { WNDCLASSEXW.lpfnWndProc.set(struct, lpfnWndProc); }

    public static int get_cbClsExtra(MemorySegment struct) { return (int) WNDCLASSEXW.cbClsExtra.get(struct); }
    public static void set_cbClsExtra(MemorySegment struct, int cbClsExtra) { WNDCLASSEXW.cbClsExtra.set(struct, cbClsExtra); }

    public static int get_cbWndExtra(MemorySegment struct) { return (int) WNDCLASSEXW.cbWndExtra.get(struct); }
    public static void set_cbWndExtra(MemorySegment struct, int cbWndExtra) { WNDCLASSEXW.cbWndExtra.set(struct, cbWndExtra); }

    public static MemoryAddress get_hInstance(MemorySegment struct) { return (MemoryAddress) WNDCLASSEXW.hInstance.get(struct); }
    public static void set_hInstance(MemorySegment struct, MemoryAddress hInstance) { WNDCLASSEXW.hInstance.set(struct, hInstance); }

    public static MemoryAddress get_hIcon(MemorySegment struct) { return (MemoryAddress) WNDCLASSEXW.hIcon.get(struct); }
    public static void set_hIcon(MemorySegment struct, MemoryAddress hIcon) { WNDCLASSEXW.hIcon.set(struct, hIcon); }

    public static MemoryAddress get_hCursor(MemorySegment struct) { return (MemoryAddress) WNDCLASSEXW.hCursor.get(struct); }
    public static void set_hCursor(MemorySegment struct, MemoryAddress hCursor) { WNDCLASSEXW.hCursor.set(struct, hCursor); }

    public static MemoryAddress get_hbrBackground(MemorySegment struct) { return (MemoryAddress) WNDCLASSEXW.hbrBackground.get(struct); }
    public static void set_hbrBackground(MemorySegment struct, MemoryAddress hbrBackground) { WNDCLASSEXW.hbrBackground.set(struct, hbrBackground); }

    public static MemoryAddress get_lpszMenuName(MemorySegment struct) { return (MemoryAddress) WNDCLASSEXW.lpszMenuName.get(struct); }
    public static void set_lpszMenuName(MemorySegment struct, MemoryAddress lpszMenuName) { WNDCLASSEXW.lpszMenuName.set(struct, lpszMenuName); }

    public static MemoryAddress get_lpszClassName(MemorySegment struct) { return (MemoryAddress) WNDCLASSEXW.lpszClassName.get(struct); }
    public static void set_lpszClassName(MemorySegment struct, MemoryAddress lpszClassName) { WNDCLASSEXW.lpszClassName.set(struct, lpszClassName); }

    public static MemoryAddress get_hIconSm(MemorySegment struct) { return (MemoryAddress) WNDCLASSEXW.hIconSm.get(struct); }
    public static void set_hIconSm(MemorySegment struct, MemoryAddress hIconSm) { WNDCLASSEXW.hIconSm.set(struct, hIconSm); }

}

