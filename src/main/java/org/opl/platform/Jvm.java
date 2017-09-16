package org.opl.platform;

import java.lang.reflect.Field;

@SuppressWarnings({"PMD.ShortClassName"})
public final class Jvm {

    @SuppressWarnings("sunapi")
    private static final sun.misc.Unsafe UNSAFE = pullUnsafe();

    public static final long ADDRESS_SIZE = UNSAFE.addressSize();

    public static final long PAGE_SIZE = UNSAFE.pageSize();

    private Jvm() {
    }

    @SuppressWarnings({"restriction", "sunapi"})
    private static sun.misc.Unsafe pullUnsafe() {
        try {
            Field field = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            return (sun.misc.Unsafe) field.get(null);
        } catch (Exception e) {
            throw new IllegalStateException("Fail to resolve the instance of sun.misc.Unsafe", e);
        }
    }

    public static long allocateMemory(long size) {
        return UNSAFE.allocateMemory(size);
    }

    public static long reallocateMemory(long address, long size) {
        return UNSAFE.reallocateMemory(address, size);
    }

    public static void freeMemory(long address) {
        UNSAFE.freeMemory(address);
    }

    public static void putLong(long address, long value) {
        UNSAFE.putLong(address, value);
    }

    public static long getLong(long address) {
        return UNSAFE.getLong(address);
    }

}
