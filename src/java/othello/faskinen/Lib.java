package othello.faskinen;

import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.GroupLayout;
import java.lang.foreign.Linker;
import java.lang.foreign.MemoryAddress;
import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.MemorySession;
import java.lang.foreign.SegmentAllocator;
import java.lang.foreign.SequenceLayout;
import java.lang.foreign.SymbolLookup;
import java.lang.foreign.ValueLayout;
import java.lang.foreign.ValueLayout.OfByte;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.VarHandle;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class Lib {

    public static final ValueLayout C_BOOL_T = ValueLayout.JAVA_INT.withBitAlignment(32);
    public static ValueLayout C_BOOL_T(String name) { return Lib.C_BOOL_T.withName(name); }

    public static final ValueLayout C_CHAR_T = ValueLayout.JAVA_BYTE.withBitAlignment(8);
    public static ValueLayout C_CHAR_T(String name) { return Lib.C_CHAR_T.withName(name); }

    public static final ValueLayout C_WCHAR_T = ValueLayout.JAVA_CHAR.withBitAlignment(16);
    public static ValueLayout C_WCHAR_T(String name) { return Lib.C_WCHAR_T.withName(name); }

    public static final ValueLayout C_INT8_T = ValueLayout.JAVA_BYTE.withBitAlignment(8);
    public static ValueLayout C_INT8_T(String name) { return Lib.C_INT8_T.withName(name); }

    public static final ValueLayout C_UINT8_T = ValueLayout.JAVA_BYTE.withBitAlignment(8);
    public static ValueLayout C_UINT8_T(String name) { return Lib.C_UINT8_T.withName(name); }

    public static final ValueLayout C_INT16_T = ValueLayout.JAVA_SHORT.withBitAlignment(16);
    public static ValueLayout C_INT16_T(String name) { return Lib.C_INT16_T.withName(name); }

    public static final ValueLayout C_UINT16_T = ValueLayout.JAVA_SHORT.withBitAlignment(16);
    public static ValueLayout C_UINT16_T(String name) { return Lib.C_UINT16_T.withName(name); }

    public static final ValueLayout C_INT32_T = ValueLayout.JAVA_INT.withBitAlignment(32);
    public static ValueLayout C_INT32_T(String name) { return Lib.C_INT32_T.withName(name); }

    public static final ValueLayout C_UINT32_T = ValueLayout.JAVA_INT.withBitAlignment(32);
    public static ValueLayout C_UINT32_T(String name) { return Lib.C_UINT32_T.withName(name); }

    public static final ValueLayout C_INT64_T = ValueLayout.JAVA_LONG.withBitAlignment(64);
    public static ValueLayout C_INT64_T(String name) { return Lib.C_INT64_T.withName(name); }

    public static final ValueLayout C_UINT64_T = ValueLayout.JAVA_LONG.withBitAlignment(64);
    public static ValueLayout C_UINT64_T(String name) { return Lib.C_UINT64_T.withName(name); }

    public static final ValueLayout C_FLOAT32_T = ValueLayout.JAVA_FLOAT.withBitAlignment(32);
    public static ValueLayout C_FLOAT32_T(String name) { return Lib.C_FLOAT32_T.withName(name); }

    public static final ValueLayout C_FLOAT64_T = ValueLayout.JAVA_DOUBLE.withBitAlignment(64);
    public static ValueLayout C_FLOAT64_T(String name) { return Lib.C_FLOAT64_T.withName(name); }

    public static final ValueLayout C_POINTER_T = ValueLayout.ADDRESS.withBitAlignment(64);
    public static ValueLayout C_POINTER_T(String name) { return Lib.C_POINTER_T.withName(name); }

    public static final MemoryAddress NULLPTR = MemoryAddress.ofLong(0);

    private static Linker linker = Linker.nativeLinker();
    private static SymbolLookup stdLib = linker.defaultLookup();
    private static SymbolLookup extLib = SymbolLookup.loaderLookup();

    public static MethodHandle loadFuncHandle(String name, MemoryLayout ret, MemoryLayout... params)
    {
        Optional<MemorySegment> stdAddr = stdLib.lookup(name);
        Optional<MemorySegment> extAddr = extLib.lookup(name);

        if (!(stdAddr.isPresent() || extAddr.isPresent()))
        {
            System.out.println("ERROR => function \"" + name + "\" from could not be loaded");
            return null;
        }

        MemorySegment addr = extAddr.isPresent() ? extAddr.get() : stdAddr.get();

        FunctionDescriptor desc;
        if (ret != null) {
            desc = FunctionDescriptor.of(ret, (MemoryLayout[]) params);
        } else {
            desc = FunctionDescriptor.ofVoid((MemoryLayout[]) params);
        }
        return linker.downcallHandle(addr, desc);
    }

    public static MethodHandle loadFuncFromAddr(MemoryAddress address, MemoryLayout ret, MemoryLayout... params)
    {
        FunctionDescriptor desc;
        if (ret != null) {
            desc = FunctionDescriptor.of(ret, (MemoryLayout[]) params);
        } else {
            desc = FunctionDescriptor.ofVoid((MemoryLayout[]) params);
        }
        return linker.downcallHandle(address, desc);
    }

    private static MethodHandle getJavaFuncHandle(Class<?> refc, String name, MethodType type)
    {
        try {
            return MethodHandles.lookup().findStatic(refc, name, type);
        } catch (NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }

    private static MethodHandle getJavaFuncHandleInstance(Class<?> refc, String name, MethodType type)
    {
        try {
            MethodHandles.lookup().findVirtual(refc, name, type);
        } catch (NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }

    public static MemorySegment getJavaFuncPointer(Class<?> refc, String name, MethodType type, FunctionDescriptor descriptor)
    {
        return getJavaFuncPointer(refc, name, type, descriptor, MemorySession.global());
    }

    public static MemorySegment getJavaFuncPointer(Class<?> refc, String name, MethodType type, FunctionDescriptor descriptor, MemorySession session)
    {
        return linker.upcallStub(getJavaFuncHandle(refc, name, type), descriptor, session);
    }

    public static String strToJava(MemorySegment str)
    {
        return str.getUtf8String(0);
    }

    public static String wstrToJava(MemorySegment wstr)
    {
        CharBuffer buffer = StandardCharsets.UTF_16LE.decode(wstr.asByteBuffer());

        char[] chars = buffer.array();

        int len = 0;
        while (chars[len] != 0) len++;

        return new String(chars, 0, len);
    }

    public static MemorySegment javaToStr(SegmentAllocator alloc, String str)
    {
        return alloc.allocateUtf8String(str);
    }

    public static MemorySegment javaToStr(String str)
    {
        return MemorySession.global().allocateUtf8String(str);
    }

    public static MemorySegment javaToWStr(SegmentAllocator alloc, String wstr)
    {
        return alloc.allocateArray(OfByte.JAVA_BYTE, (wstr + '\0').getBytes(StandardCharsets.UTF_16LE));
    }

    public static MemorySegment javaToWStr(String wstr)
    {
        return MemorySession.global().allocateArray(OfByte.JAVA_BYTE, (wstr + '\0').getBytes(StandardCharsets.UTF_16LE));
    }

    public static VarHandle getStructField(GroupLayout layout, String field)
    {
        return layout.varHandle(MemoryLayout.PathElement.groupElement(field));
    }

    public static VarHandle getSequenceField(SequenceLayout layout, String field)
    {
        return layout.varHandle(MemoryLayout.PathElement.sequenceElement(), MemoryLayout.PathElement.groupElement(field));
    }

    public static VarHandle getStructArrayField(GroupLayout layout, String field, String name)
    {
        return layout.varHandle(
                MemoryLayout.PathElement.groupElement(field),
                MemoryLayout.PathElement.sequenceElement(),
                MemoryLayout.PathElement.groupElement(name)
        );
    }

    public static long getStructFieldOffset(GroupLayout layout, String field)
    {
        return layout.byteOffset(MemoryLayout.PathElement.groupElement(field));
    }

    public static long getSequenceFieldOffset(SequenceLayout layout, String field)
    {
        return layout.byteOffset(MemoryLayout.PathElement.sequenceElement(), MemoryLayout.PathElement.groupElement(field));
    }

}

