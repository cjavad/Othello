package othello.faskinen.win32.structs;

import othello.faskinen.Lib;

import java.lang.foreign.GroupLayout;
import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.invoke.VarHandle;

public class POINT {

	/*
	typedef struct tagPOINT {
		LONG x;
		LONG y;
	} POINT
	*/

    public static final GroupLayout layout = MemoryLayout.structLayout(
            Lib.C_INT32_T("x"),
            Lib.C_INT32_T("y")
    );

    private static final VarHandle
            x		= Lib.getStructField(layout, "x"),
            y		= Lib.getStructField(layout, "y");

    public static final long SIZE = POINT.layout.byteSize();
    public static long sizeOf() { return SIZE; }

    public static int get_x(MemorySegment struct) { return (int) POINT.x.get(struct); }
    public static void set_x(MemorySegment struct, int x) { POINT.x.set(struct, x); }

    public static int get_y(MemorySegment struct) { return (int) POINT.y.get(struct); }
    public static void set_y(MemorySegment struct, int y) { POINT.y.set(struct, y); }
}

