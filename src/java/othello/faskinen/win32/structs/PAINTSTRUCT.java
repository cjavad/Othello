package othello.faskinen.win32.structs;

import othello.faskinen.Lib;

import java.lang.foreign.GroupLayout;
import java.lang.foreign.MemoryAddress;
import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.invoke.VarHandle;

public class PAINTSTRUCT {

	/*
	typedef struct tagPAINTSTRUCT {
		HDC  hdc;
		BOOL fErase;
		RECT rcPaint;
		BOOL fRestore;
		BOOL fIncUpdate;
		BYTE rgbReserved[32];
	} PAINTSTRUCT
	*/

    public static final GroupLayout layout = MemoryLayout.structLayout(
            Lib.C_POINTER_T("hdc"),
            Lib.C_BOOL_T("fErase"),
            RECT.layout.withName("rcPaint"),
            Lib.C_BOOL_T("fRestore"),
            Lib.C_BOOL_T("fIncUpdate"),
            MemoryLayout.sequenceLayout(32, Lib.C_UINT8_T("byte")).withName("rgbReserved")
    );

    public static final VarHandle
            hdc			= Lib.getStructField(layout, "hdc"),
            fErase		= Lib.getStructField(layout, "fErase"),
            fRestore	= Lib.getStructField(layout, "fIncUpdate"),
            rgbReserved = null;//	= Lib.getStructArrayField(layout, "rgbReserved", "byte");

    public static final long
            rcPaint		= Lib.getStructFieldOffset(layout, "rcPaint");

    public static final long SIZE = PAINTSTRUCT.layout.byteSize();

    public static long sizeOf() { return SIZE; }

    public static MemoryAddress get_hdc(MemorySegment struct) { return (MemoryAddress) PAINTSTRUCT.hdc.get(struct); }
    public static void set_hdc(MemorySegment struct, MemoryAddress hdc) { PAINTSTRUCT.hdc.set(struct, hdc); }

    public static boolean get_fErase(MemorySegment struct) { return (int) PAINTSTRUCT.fErase.get(struct) != 0; }
    public static void set_fErase(MemorySegment struct, boolean fErase) { PAINTSTRUCT.fErase.set(struct, fErase ? 1 : 0); }

    public static MemorySegment get_rcPaint(MemorySegment struct) { return struct.asSlice(rcPaint, RECT.SIZE); }

}
