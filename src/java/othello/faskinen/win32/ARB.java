package othello.faskinen.win32;

import othello.faskinen.Lib;

import java.lang.foreign.MemoryAddress;
import java.lang.foreign.MemorySession;
import java.lang.invoke.MethodHandle;

public class ARB {

     /*
    HGLRC wglCreateContextAttribsARB(HDC hDC, HGLRC hshareContext, const int *attribList);
     */

    private static MethodHandle HANDLE_wglCreateContextAttribsARB = Lib.loadFuncFromAddr(
            Win32.wglGetProcAddress(Lib.javaToStr("wglCreateContextAttribsARB").address()),
            Lib.C_POINTER_T,
            Lib.C_POINTER_T, Lib.C_POINTER_T, Lib.C_POINTER_T
    );

    public static MemoryAddress wglCreateContextAttribsARB(MemoryAddress i_hDC, MemoryAddress i_hshareContext, MemoryAddress i_attribList) {
        try {
            return (MemoryAddress) HANDLE_wglCreateContextAttribsARB.invoke(i_hDC, i_hshareContext, i_attribList);
        } catch (Throwable e) {
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }

}
