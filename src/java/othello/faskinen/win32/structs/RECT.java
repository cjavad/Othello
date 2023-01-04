package othello.faskinen.win32.structs;

import othello.faskinen.Lib;

import java.lang.foreign.GroupLayout;
import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.invoke.VarHandle;


public class RECT {

	/*
	typedef struct tagRECT {
		LONG left;
		LONG top;
		LONG right;
		LONG bottom;
	} RECT
	*/

    public static final GroupLayout layout = MemoryLayout.structLayout(
            Lib.C_INT64_T("left"),
            Lib.C_INT64_T("top"),
            Lib.C_INT64_T("right"),
            Lib.C_INT64_T("bottom")
    );

    private static final VarHandle
            left		= Lib.getStructField(layout, "left"),
            top			= Lib.getStructField(layout, "top"),
            right		= Lib.getStructField(layout, "right"),
            bottom		= Lib.getStructField(layout, "bottom");

    public static final long SIZE = RECT.layout.byteSize();

    public static long size() { return SIZE; }

    public static long get_left(MemorySegment struct) { return (long) RECT.left.get(struct); }
    public static void set_left(MemorySegment struct, long left) { RECT.left.set(struct, left); }

    public static long get_top(MemorySegment struct) { return (long) RECT.top.get(struct); }
    public static void set_top(MemorySegment struct, long top) { RECT.top.set(struct, top); }

    public static long get_right(MemorySegment struct) { return (long) RECT.right.get(struct); }
    public static void set_right(MemorySegment struct, long right) { RECT.right.set(struct, right); }

    public static long get_bottom(MemorySegment struct) { return (long) RECT.bottom.get(struct); }
    public static void set_bottom(MemorySegment struct, long bottom) { RECT.bottom.set(struct, bottom); }

}

