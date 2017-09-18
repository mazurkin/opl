package org.opl.platform;

import java.lang.reflect.Field;
import java.security.ProtectionDomain;

/**
 * <p>A wrapper around sun.misc.Unsafe that makes imports legal and safe.</p>
 *
 * <p>Prevents your application code from poisoning with sun.misc.* imports</p>
 *
 * @see <a href="http://mishadoff.com/blog/java-magic-part-4-sun-dot-misc-dot-unsafe">
 *     Java Magic. Part 4: sun.misc.Unsafe</a>
 * @see <a href="http://www.docjar.com/docs/api/sun/misc/Unsafe.html">
 *     java.misc.Unsafe (javadoc)</a>
 * @see <a href="https://dzone.com/articles/understanding-sunmiscunsafe">
 *     Understanding sun.misc.Unsafe</a>
 * @see <a href="http://openjdk.java.net/jeps/171>
 *     JEP 171: Fence Intrinsics</a>
 * @see <a href="https://stackoverflow.com/questions/23603304/java-8-unsafe-xxxfence-instructions">
 *     Java 8 Unsafe: xxxFence() instructions</a>
 */
@SuppressWarnings({"PMD.ShortClassName"})
public final class Jvm {

    @SuppressWarnings("sunapi")
    public static final int INVALID_FIELD_OFFSET = sun.misc.Unsafe.INVALID_FIELD_OFFSET;
    @SuppressWarnings("sunapi")
    public static final int ARRAY_BOOLEAN_BASE_OFFSET = sun.misc.Unsafe.ARRAY_BOOLEAN_BASE_OFFSET;
    @SuppressWarnings("sunapi")
    public static final int ARRAY_BYTE_BASE_OFFSET = sun.misc.Unsafe.ARRAY_BYTE_BASE_OFFSET;
    @SuppressWarnings("sunapi")
    public static final int ARRAY_SHORT_BASE_OFFSET = sun.misc.Unsafe.ARRAY_SHORT_BASE_OFFSET;
    @SuppressWarnings("sunapi")
    public static final int ARRAY_CHAR_BASE_OFFSET = sun.misc.Unsafe.ARRAY_CHAR_BASE_OFFSET;
    @SuppressWarnings("sunapi")
    public static final int ARRAY_INT_BASE_OFFSET = sun.misc.Unsafe.ARRAY_INT_BASE_OFFSET;
    @SuppressWarnings("sunapi")
    public static final int ARRAY_LONG_BASE_OFFSET = sun.misc.Unsafe.ARRAY_LONG_BASE_OFFSET;
    @SuppressWarnings("sunapi")
    public static final int ARRAY_FLOAT_BASE_OFFSET = sun.misc.Unsafe.ARRAY_FLOAT_BASE_OFFSET;
    @SuppressWarnings("sunapi")
    public static final int ARRAY_DOUBLE_BASE_OFFSET = sun.misc.Unsafe.ARRAY_DOUBLE_BASE_OFFSET;
    @SuppressWarnings("sunapi")
    public static final int ARRAY_OBJECT_BASE_OFFSET = sun.misc.Unsafe.ARRAY_OBJECT_BASE_OFFSET;
    @SuppressWarnings("sunapi")
    public static final int ARRAY_BOOLEAN_INDEX_SCALE = sun.misc.Unsafe.ARRAY_BOOLEAN_INDEX_SCALE;
    @SuppressWarnings("sunapi")
    public static final int ARRAY_BYTE_INDEX_SCALE = sun.misc.Unsafe.ARRAY_BYTE_INDEX_SCALE;
    @SuppressWarnings("sunapi")
    public static final int ARRAY_SHORT_INDEX_SCALE = sun.misc.Unsafe.ARRAY_SHORT_INDEX_SCALE;
    @SuppressWarnings("sunapi")
    public static final int ARRAY_CHAR_INDEX_SCALE = sun.misc.Unsafe.ARRAY_CHAR_INDEX_SCALE;
    @SuppressWarnings("sunapi")
    public static final int ARRAY_INT_INDEX_SCALE = sun.misc.Unsafe.ARRAY_INT_INDEX_SCALE;
    @SuppressWarnings("sunapi")
    public static final int ARRAY_LONG_INDEX_SCALE = sun.misc.Unsafe.ARRAY_LONG_INDEX_SCALE;
    @SuppressWarnings("sunapi")
    public static final int ARRAY_FLOAT_INDEX_SCALE = sun.misc.Unsafe.ARRAY_FLOAT_INDEX_SCALE;
    @SuppressWarnings("sunapi")
    public static final int ARRAY_DOUBLE_INDEX_SCALE = sun.misc.Unsafe.ARRAY_DOUBLE_INDEX_SCALE;
    @SuppressWarnings("sunapi")
    public static final int ARRAY_OBJECT_INDEX_SCALE = sun.misc.Unsafe.ARRAY_OBJECT_INDEX_SCALE;

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

    // Class operations

    public static Object allocateInstance(Class<?> clazz) throws InstantiationException {
        return UNSAFE.allocateInstance(clazz);
    }

    public static Class defineAnonymousClass(Class hostClass, byte[] data, Object[] cpPatches) {
        return UNSAFE.defineAnonymousClass(hostClass, data, cpPatches);
    }

    public static Class defineClass(String name, byte[] b, int off, int len,
                                    ClassLoader loader, ProtectionDomain protectionDomain)
    {
        return UNSAFE.defineClass(name, b, off, len, loader, protectionDomain);
    }

    public static void ensureClassInitialized(Class<?> c) {
        UNSAFE.ensureClassInitialized(c);
    }

    public static boolean shouldBeInitialized(Class<?> c) {
        return UNSAFE.shouldBeInitialized(c);
    }

    // Field operations

    public static long staticFieldOffset(Field field) {
        return UNSAFE.staticFieldOffset(field);
    }

    public static Object staticFieldBase(Field field) {
        return UNSAFE.staticFieldBase(field);
    }

    public static long objectFieldOffset(Field field) {
        return UNSAFE.objectFieldOffset(field);
    }

    public static int arrayBaseOffset(Class<?> c) {
        return UNSAFE.arrayBaseOffset(c);
    }

    public static int arrayIndexScale(Class<?> c) {
        return UNSAFE.arrayIndexScale(c);
    }

    // Memory operations

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

    public static void setMemory(Object o, long offset, long bytes, byte value) {
        UNSAFE.setMemory(o, offset, bytes, value);
    }

    public static void copyMemory(long srcAddress, long dstAddress, long bytes) {
        UNSAFE.copyMemory(srcAddress, dstAddress, bytes);
    }

    public static void copyMemory(Object srcBase, long srcOffset, Object dstBase, long dstOffset, long bytes) {
        UNSAFE.copyMemory(srcBase, srcOffset, dstBase, dstOffset, bytes);
    }

    // Exception handling

    public static void throwException(Throwable throwable) {
        UNSAFE.throwException(throwable);
    }

    // Thread control

    public static void loadFence() {
        UNSAFE.loadFence();
    }

    public static void storeFence() {
        UNSAFE.storeFence();
    }

    public static void fullFence() {
        UNSAFE.fullFence();
    }

    public static void park(boolean isAbsolute, long time) {
        UNSAFE.park(isAbsolute, time);
    }

    public static void unpark(Object thread) {
        UNSAFE.unpark(thread);
    }

    public static int getLoadAverage(double[] loadavg, int nelems) {
        return UNSAFE.getLoadAverage(loadavg, nelems);
    }

    // CAS

    public static boolean compareAndSwapInt(Object o, long offset, int expected, int x) {
        return UNSAFE.compareAndSwapInt(o, offset, expected, x);
    }

    public static boolean compareAndSwapLong(Object o, long offset, long expected, long x) {
        return UNSAFE.compareAndSwapLong(o, offset, expected, x);
    }

    public static boolean compareAndSwapObject(Object o, long offset, Object expected, Object x) {
        return UNSAFE.compareAndSwapObject(o, offset, expected, x);
    }

    // Plain value accessors

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

    // Object value accessors

    public static int getInt(Object o, long offset) {
        return UNSAFE.getInt(o, offset);
    }

    public static void putInt(Object o, long offset, int value) {
        UNSAFE.putInt(o, offset, value);
    }

    public static Object getObject(Object o, long offset) {
        return UNSAFE.getObject(o, offset);
    }

    public static void putObject(Object o, long offset, Object value) {
        UNSAFE.putObject(o, offset, value);
    }

    public static boolean getBoolean(Object o, long offset) {
        return UNSAFE.getBoolean(o, offset);
    }

    public static void putBoolean(Object o, long offset, boolean value) {
        UNSAFE.putBoolean(o, offset, value);
    }

    public static byte getByte(Object o, long offset) {
        return UNSAFE.getByte(o, offset);
    }

    public static void putByte(Object o, long offset, byte value) {
        UNSAFE.putByte(o, offset, value);
    }

    public static short getShort(Object o, long offset) {
        return UNSAFE.getShort(o, offset);
    }

    public static void putShort(Object o, long offset, short value) {
        UNSAFE.putShort(o, offset, value);
    }

    public static char getChar(Object o, long offset) {
        return UNSAFE.getChar(o, offset);
    }

    public static void putChar(Object o, long offset, char value) {
        UNSAFE.putChar(o, offset, value);
    }

    public static long getLong(Object o, long offset) {
        return UNSAFE.getLong(o, offset);
    }

    public static void putLong(Object o, long offset, long value) {
        UNSAFE.putLong(o, offset, value);
    }

    public static float getFloat(Object o, long offset) {
        return UNSAFE.getFloat(o, offset);
    }

    public static void putFloat(Object o, long offset, float value) {
        UNSAFE.putFloat(o, offset, value);
    }

    public static double getDouble(Object o, long offset) {
        return UNSAFE.getDouble(o, offset);
    }

    public static void putDouble(Object o, long offset, double value) {
        UNSAFE.putDouble(o, offset, value);
    }

    // Object volatile value accessors

    public static Object getObjectVolatile(Object o, long offset) {
        return UNSAFE.getObjectVolatile(o, offset);
    }

    public static void putObjectVolatile(Object o, long offset, Object value) {
        UNSAFE.putObjectVolatile(o, offset, value);
    }

    public static int getIntVolatile(Object o, long offset) {
        return UNSAFE.getIntVolatile(o, offset);
    }

    public static void putIntVolatile(Object o, long offset, int value) {
        UNSAFE.putIntVolatile(o, offset, value);
    }

    public static boolean getBooleanVolatile(Object o, long offset) {
        return UNSAFE.getBooleanVolatile(o, offset);
    }

    public static void putBooleanVolatile(Object o, long offset, boolean value) {
        UNSAFE.putBooleanVolatile(o, offset, value);
    }

    public static byte getByteVolatile(Object o, long offset) {
        return UNSAFE.getByteVolatile(o, offset);
    }

    public static void putByteVolatile(Object o, long offset, byte value) {
        UNSAFE.putByteVolatile(o, offset, value);
    }

    public static short getShortVolatile(Object o, long offset) {
        return UNSAFE.getShortVolatile(o, offset);
    }

    public static void putShortVolatile(Object o, long offset, short value) {
        UNSAFE.putShortVolatile(o, offset, value);
    }

    public static char getCharVolatile(Object o, long offset) {
        return UNSAFE.getCharVolatile(o, offset);
    }

    public static void putCharVolatile(Object o, long offset, char value) {
        UNSAFE.putCharVolatile(o, offset, value);
    }

    public static long getLongVolatile(Object o, long offset) {
        return UNSAFE.getLongVolatile(o, offset);
    }

    public static void putLongVolatile(Object o, long offset, long value) {
        UNSAFE.putLongVolatile(o, offset, value);
    }

    public static float getFloatVolatile(Object o, long offset) {
        return UNSAFE.getFloatVolatile(o, offset);
    }

    public static void putFloatVolatile(Object o, long offset, float value) {
        UNSAFE.putFloatVolatile(o, offset, value);
    }

    public static double getDoubleVolatile(Object o, long offset) {
        return UNSAFE.getDoubleVolatile(o, offset);
    }

    public static void putDoubleVolatile(Object o, long offset, double value) {
        UNSAFE.putDoubleVolatile(o, offset, value);
    }

    // Object ordered value accessors

    public static void putOrderedObject(Object o, long offset, Object value) {
        UNSAFE.putOrderedObject(o, offset, value);
    }

    public static void putOrderedInt(Object o, long offset, int value) {
        UNSAFE.putOrderedInt(o, offset, value);
    }

    public static void putOrderedLong(Object o, long offset, long value) {
        UNSAFE.putOrderedLong(o, offset, value);
    }

}
