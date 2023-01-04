package othello.faskinen.win32.structs;

import othello.faskinen.Lib;

import java.lang.foreign.GroupLayout;
import java.lang.foreign.MemoryAddress;
import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.invoke.VarHandle;

public class MSG {

	/*
	typedef struct tagMSG {
		HWND   hwnd;
		UINT   message;
		WPARAM wParam;
		LPARAM lParam;
		DWORD  time;
		POINT  pt;
		DWORD  lPrivate;
	} MSG
	*/

    public static final GroupLayout layout = MemoryLayout.structLayout(
            Lib.C_POINTER_T("hwnd"),
            Lib.C_UINT32_T("message"),
            Lib.C_INT32_T("wParam"),
            Lib.C_UINT64_T("lParam"),
            Lib.C_UINT32_T("time"),
            POINT.layout.withName("pt"),
            Lib.C_UINT32_T("lPrivate")
    );

    private static final VarHandle
            hwnd		= Lib.getStructField(layout, "hwnd"),
            message		= Lib.getStructField(layout, "message"),
            wParam		= Lib.getStructField(layout, "wParam"),
            lParam		= Lib.getStructField(layout, "lParam"),
            time		= Lib.getStructField(layout, "time"),
            lPrivate	= Lib.getStructField(layout, "lPrivate");

    private static final long
            pt			= Lib.getStructFieldOffset(layout, "pt");

    public static final long SIZE = MSG.layout.byteSize();
    public static long sizeOf() { return SIZE; }

    public static MemorySegment index(MemorySegment structArray, int index) { return structArray.asSlice(index * SIZE, SIZE); }

    public static MemoryAddress get_hwnd(MemorySegment struct) { return (MemoryAddress) MSG.hwnd.get(struct); }
    public static void set_hwnd(MemorySegment struct, MemoryAddress hwnd) { MSG.hwnd.set(struct, hwnd); }

    public static int get_message(MemorySegment struct) { return (int) MSG.message.get(struct); }
    public static void set_message(MemorySegment struct, int message) { MSG.message.set(struct, message); }

    public static int get_wParam(MemorySegment struct) { return (int) MSG.wParam.get(struct); }
    public static void set_wParam(MemorySegment struct, int wParam) { MSG.wParam.set(struct, wParam); }

    public static long get_lParam(MemorySegment struct) { return (long) MSG.lParam.get(struct); }
    public static void set_lParam(MemorySegment struct, long lParam) { MSG.lParam.set(struct, lParam); }

    public static int get_time(MemorySegment struct) { return (int) MSG.time.get(struct); }
    public static void set_time(MemorySegment struct, int time) { MSG.time.set(struct, time); }

    public static MemorySegment pt(MemorySegment struct) { return struct.asSlice(pt, POINT.SIZE); }

    public static int get_lPrivate(MemorySegment struct) { return (int) MSG.lPrivate.get(struct); }
    public static void set_lPrivate(MemorySegment struct, int lPrivate) { MSG.lPrivate.set(struct, lPrivate); }
}

