package othello.faskinen.win32.structs;

import othello.faskinen.Lib;

import java.lang.foreign.GroupLayout;
import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.invoke.VarHandle;

// i ded
public class PIXELFORMATDESCRIPTOR {

    /*
    typedef struct tagPIXELFORMATDESCRIPTOR {
        WORD  nSize;
        WORD  nVersion;
        DWORD dwFlags;
        BYTE  iPixelType;
        BYTE  cColorBits;
        BYTE  cRedBits;
        BYTE  cRedShift;
        BYTE  cGreenBits;
        BYTE  cGreenShift;
        BYTE  cBlueBits;
        BYTE  cBlueShift;
        BYTE  cAlphaBits;
        BYTE  cAlphaShift;
        BYTE  cAccumBits;
        BYTE  cAccumRedBits;
        BYTE  cAccumGreenBits;
        BYTE  cAccumBlueBits;
        BYTE  cAccumAlphaBits;
        BYTE  cDepthBits;
        BYTE  cStencilBits;
        BYTE  cAuxBuffers;
        BYTE  iLayerType;
        BYTE  bReserved;
        DWORD dwLayerMask;
        DWORD dwVisibleMask;
        DWORD dwDamageMask;
    } PIXELFORMATDESCRIPTOR, *PPIXELFORMATDESCRIPTOR, *LPPIXELFORMATDESCRIPTOR;
     */

    public static final GroupLayout layout = MemoryLayout.structLayout(
            Lib.C_UINT16_T("nSize"),
            Lib.C_UINT32_T("nVersion"),
            Lib.C_UINT32_T("dwFlags"),
            Lib.C_UINT8_T("iPixelType"),
            Lib.C_UINT8_T("cColorBits"),
            Lib.C_UINT8_T("cRedBits"),
            Lib.C_UINT8_T("cRedShift"),
            Lib.C_UINT8_T("cGreenBits"),
            Lib.C_UINT8_T("cGreenShift"),
            Lib.C_UINT8_T("cBlueBits"),
            Lib.C_UINT8_T("cBlueShift"),
            Lib.C_UINT8_T("cAlphaBits"),
            Lib.C_UINT8_T("cAlphaShift"),
            Lib.C_UINT8_T("cAccumBits"),
            Lib.C_UINT8_T("cAccumRedBits"),
            Lib.C_UINT8_T("cAccumGreenBits"),
            Lib.C_UINT8_T("cAccumBlueBits"),
            Lib.C_UINT8_T("cAccumAlphaBits"),
            Lib.C_UINT8_T("cDepthBits"),
            Lib.C_UINT8_T("cStencilBits"),
            Lib.C_UINT8_T("cAuxBuffers"),
            Lib.C_UINT8_T("iLayerType"),
            Lib.C_UINT8_T("bReserved"),
            Lib.C_UINT32_T("dwLayerMask"),
            Lib.C_UINT32_T("dwVisibleMask"),
            Lib.C_UINT32_T("dwDamageMask")
    );

    private static final VarHandle
            nSize = Lib.getStructField(layout, "nSize"),
            nVersion = Lib.getStructField(layout, "nVersion"),
            dwFlags = Lib.getStructField(layout, "dwFlags"),
            iPixelType = Lib.getStructField(layout, "iPixelType"),
            cColorBits = Lib.getStructField(layout, "cColorBits"),
            cRedBits = Lib.getStructField(layout, "cRedBits"),
            cRedShift = Lib.getStructField(layout, "cRedShift"),
            cGreenBits = Lib.getStructField(layout, "cGreenBits"),
            cGreenShift = Lib.getStructField(layout, "cGreenShift"),
            cBlueBits = Lib.getStructField(layout, "cBlueBits"),
            cBlueShift = Lib.getStructField(layout, "cBlueShift"),
            cAlphaBits = Lib.getStructField(layout, "cAlphaBits"),
            cAlphaShift = Lib.getStructField(layout, "cAlphaShift"),
            cAccumBits = Lib.getStructField(layout, "cAccumBits"),
            cAccumRedBits = Lib.getStructField(layout, "cAccumRedBits"),
            cAccumGreenBits = Lib.getStructField(layout, "cAccumGreenBits"),
            cAccumBlueBits = Lib.getStructField(layout, "cAccumBlueBits"),
            cAccumAlphaBits = Lib.getStructField(layout, "cAccumAlphaBits"),
            cDepthBits = Lib.getStructField(layout, "cDepthBits"),
            cStencilBits = Lib.getStructField(layout, "cStencilBits"),
            cAuxBuffers = Lib.getStructField(layout, "cAuxBuffers"),
            iLayerType = Lib.getStructField(layout, "iLayerType"),
            bReserved = Lib.getStructField(layout, "bReserved"),
            dwLayerMask = Lib.getStructField(layout, "dwLayerMask"),
            dwVisibleMask = Lib.getStructField(layout, "dwVisibleMask"),
            dwDamageMask = Lib.getStructField(layout, "dwDamageMask");

    public static final long SIZE = PIXELFORMATDESCRIPTOR.layout.byteSize();

    public static long size() { return SIZE; }

    public static short get_nSize(MemorySegment struct) { return (short) PIXELFORMATDESCRIPTOR.nSize.get(struct); }
    public static void set_nSize(MemorySegment struct, short nSize) { PIXELFORMATDESCRIPTOR.nSize.set(struct, nSize); }

    public static short get_nVersion(MemorySegment struct) { return (short) PIXELFORMATDESCRIPTOR.nVersion.get(struct); }
    public static void set_nVersion(MemorySegment struct, short nVersion) { PIXELFORMATDESCRIPTOR.nVersion.set(struct, nVersion); }

    public static int get_dwFlags(MemorySegment struct) { return (int) PIXELFORMATDESCRIPTOR.dwFlags.get(struct); }
    public static void set_dwFlags(MemorySegment struct, int dwFlags) { PIXELFORMATDESCRIPTOR.dwFlags.set(struct, dwFlags); }

    public static byte get_iPixelType(MemorySegment struct) { return (byte) PIXELFORMATDESCRIPTOR.iPixelType.get(struct); }
    public static void set_iPixelType(MemorySegment struct, byte iPixelType) { PIXELFORMATDESCRIPTOR.iPixelType.set(struct, iPixelType);}

    public static byte get_cColorBits(MemorySegment struct) { return (byte) PIXELFORMATDESCRIPTOR.cColorBits.get(struct); }
    public static void set_cColorBits(MemorySegment struct, byte cColorBits) { PIXELFORMATDESCRIPTOR.cColorBits.set(struct, cColorBits); }

    // cRedBits
//    public static byte get_cRegBit

    // cRedShift

    // cGreenBits

    // cGreenShift

    // cBlueBits

    // cBlueShift

    // cAlphaBits

    // cAlphaShift

    public static byte get_cAccumBits(MemorySegment struct) { return (byte) PIXELFORMATDESCRIPTOR.cAccumBits.get(struct); }
    public static void set_cAccumBits(MemorySegment struct, byte cAccumBits) { PIXELFORMATDESCRIPTOR.cAccumBits.set(struct, cAccumBits); }

    // cAccumRedBits

    // cAccumGreenBits

    // cAccumBlueBits

    // cAccumAlphaBits

    public static byte get_cDepthBits(MemorySegment struct) { return (byte) PIXELFORMATDESCRIPTOR.cDepthBits.get(struct); }
    public static void set_cDepthBits(MemorySegment struct, byte cDepthBits) { PIXELFORMATDESCRIPTOR.cDepthBits.set(struct, cDepthBits); }

    public static byte get_cStencilBits(MemorySegment struct) { return (byte) PIXELFORMATDESCRIPTOR.cStencilBits.get(struct); }
    public static void set_cStencilBits(MemorySegment struct, byte cStencilBits) { PIXELFORMATDESCRIPTOR.cStencilBits.set(struct, cStencilBits); }

    // cAuxBuffers

    public static byte get_iLayerType(MemorySegment struct) { return (byte) PIXELFORMATDESCRIPTOR.iLayerType.get(struct); }
    public static void set_iLayerType(MemorySegment struct, byte iLayerType) { PIXELFORMATDESCRIPTOR.iLayerType.set(struct, iLayerType); }

    // bReserved

    public static int get_dwLayerMask(MemorySegment struct) { return (int) PIXELFORMATDESCRIPTOR.dwLayerMask.get(struct); }
    public static void set_dwLayerMask(MemorySegment struct, int dwLayerMask) { PIXELFORMATDESCRIPTOR.dwLayerMask.set(struct, dwLayerMask); }

    // dwVisibleMask

    // dwDamageMask
}
