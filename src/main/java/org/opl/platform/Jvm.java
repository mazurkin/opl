package org.opl.platform;

import java.lang.reflect.Field;

/**
 * <p>A wrapper around sun.misc.Unsafe that makes imports legal and safe.</p>
 * @see <a href="http://mishadoff.com/blog/java-magic-part-4-sun-dot-misc-dot-unsafe">
 *     Java Magic. Part 4: sun.misc.Unsafe</a>
 * @see <a href="http://www.docjar.com/docs/api/sun/misc/Unsafe.html">
 *     java.misc.Unsafe (javadoc)</a>
 * @see <a href="https://dzone.com/articles/understanding-sunmiscunsafe">
 *     Understanding sun.misc.Unsafe</a>
 */
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

    public static Object allocateInstance(Class<?> clazz) throws InstantiationException {
        return UNSAFE.allocateInstance(clazz);
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

    public static void setMemory(long address, long bytes, byte value) {
        UNSAFE.setMemory(address, bytes, value);
    }

    public static void putLong(long address, long value) {
        UNSAFE.putLong(address, value);
    }

    public static long getLong(long address) {
        return UNSAFE.getLong(address);
    }

    public static void putInt(long address, int value) {
        UNSAFE.putInt(address, value);
    }

    public static int getInt(long address) {
        return UNSAFE.getInt(address);
    }

    public static void putShort(long address, short value) {
        UNSAFE.putShort(address, value);
    }

    public static short getShort(long address) {
        return UNSAFE.getShort(address);
    }

    public static void putChar(long address, char value) {
        UNSAFE.putChar(address, value);
    }

    public static char getChar(long address) {
        return UNSAFE.getChar(address);
    }

    public static void putByte(long address, byte value) {
        UNSAFE.putByte(address, value);
    }

    public static byte getByte(long address) {
        return UNSAFE.getByte(address);
    }

    public static void putDouble(long address, double value) {
        UNSAFE.putDouble(address, value);
    }

    public static double getDouble(long address) {
        return UNSAFE.getDouble(address);
    }

    public static void putFloat(long address, float value) {
        UNSAFE.putFloat(address, value);
    }

    public static float getFloat(long address) {
        return UNSAFE.getFloat(address);
    }

    public static void putAddress(long address, long value) {
        UNSAFE.putAddress(address, value);
    }

    public static long getAddress(long address) {
        return UNSAFE.getAddress(address);
    }
}
