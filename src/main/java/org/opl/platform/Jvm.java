package org.opl.platform;

import java.lang.reflect.Field;
import java.security.ProtectionDomain;

/**
 * <p>A wrapper around sun.misc.Unsafe that makes imports legal and safe.</p>
 *
 * <p>Prevents your application code from poisoning with sun.misc.* imports</p>
 *
 * <p>Almost all JavaDoc is copied from <a href="http://hg.openjdk.java.net/jdk8/jdk8/jdk/file/687fd7c7986d/src/share/classes/sun/misc/Unsafe.java">OpenJDK</a></p>
 *
 * @see <a href="http://mishadoff.com/blog/java-magic-part-4-sun-dot-misc-dot-unsafe">
 *     Java Magic. Part 4: sun.misc.Unsafe</a>
 * @see <a href="http://www.docjar.com/docs/api/sun/misc/Unsafe.html">
 *     java.misc.Unsafe (javadoc)</a>
 * @see <a href="https://dzone.com/articles/understanding-sunmiscunsafe">
 *     Understanding sun.misc.Unsafe</a>
 * @see <a href="http://openjdk.java.net/jeps/171">
 *     JEP 171: Fence Intrinsics</a>
 * @see <a href="https://stackoverflow.com/questions/23603304/java-8-unsafe-xxxfence-instructions">
 *     Java 8 Unsafe: xxxFence() instructions</a>
 * @see <a href="http://hg.openjdk.java.net/jdk8/jdk8/jdk/file/687fd7c7986d/src/share/classes/sun/misc/Unsafe.java">
 *     Java 8 Unsafe: Source</a>
 */
@SuppressWarnings({
    "PMD.ShortClassName",
    "unused",
    "WeakerAccess",
    "JavaDoc"
})
public final class Jvm {

    /**
     * This constant differs from all results that will ever be returned from
     * {@link #staticFieldOffset}, {@link #objectFieldOffset},
     * or {@link #arrayBaseOffset}.
     */
    @SuppressWarnings("sunapi")
    public static final int INVALID_FIELD_OFFSET = sun.misc.Unsafe.INVALID_FIELD_OFFSET;

    /** The value of {@code arrayBaseOffset(boolean[].class)} */
    @SuppressWarnings("sunapi")
    public static final int ARRAY_BOOLEAN_BASE_OFFSET = sun.misc.Unsafe.ARRAY_BOOLEAN_BASE_OFFSET;

    /** The value of {@code arrayBaseOffset(byte[].class)} */
    @SuppressWarnings("sunapi")
    public static final int ARRAY_BYTE_BASE_OFFSET = sun.misc.Unsafe.ARRAY_BYTE_BASE_OFFSET;

    /** The value of {@code arrayBaseOffset(short[].class)} */
    @SuppressWarnings("sunapi")
    public static final int ARRAY_SHORT_BASE_OFFSET = sun.misc.Unsafe.ARRAY_SHORT_BASE_OFFSET;

    /** The value of {@code arrayBaseOffset(char[].class)} */
    @SuppressWarnings("sunapi")
    public static final int ARRAY_CHAR_BASE_OFFSET = sun.misc.Unsafe.ARRAY_CHAR_BASE_OFFSET;

    /** The value of {@code arrayBaseOffset(int[].class)} */
    @SuppressWarnings("sunapi")
    public static final int ARRAY_INT_BASE_OFFSET = sun.misc.Unsafe.ARRAY_INT_BASE_OFFSET;

    /** The value of {@code arrayBaseOffset(long[].class)} */
    @SuppressWarnings("sunapi")
    public static final int ARRAY_LONG_BASE_OFFSET = sun.misc.Unsafe.ARRAY_LONG_BASE_OFFSET;

    /** The value of {@code arrayBaseOffset(float[].class)} */
    @SuppressWarnings("sunapi")
    public static final int ARRAY_FLOAT_BASE_OFFSET = sun.misc.Unsafe.ARRAY_FLOAT_BASE_OFFSET;

    /** The value of {@code arrayBaseOffset(double[].class)} */
    @SuppressWarnings("sunapi")
    public static final int ARRAY_DOUBLE_BASE_OFFSET = sun.misc.Unsafe.ARRAY_DOUBLE_BASE_OFFSET;

    /** The value of {@code arrayBaseOffset(Object[].class)} */
    @SuppressWarnings("sunapi")
    public static final int ARRAY_OBJECT_BASE_OFFSET = sun.misc.Unsafe.ARRAY_OBJECT_BASE_OFFSET;

    /** The value of {@code arrayIndexScale(boolean[].class)} */
    @SuppressWarnings("sunapi")
    public static final int ARRAY_BOOLEAN_INDEX_SCALE = sun.misc.Unsafe.ARRAY_BOOLEAN_INDEX_SCALE;

    /** The value of {@code arrayIndexScale(byte[].class)} */
    @SuppressWarnings("sunapi")
    public static final int ARRAY_BYTE_INDEX_SCALE = sun.misc.Unsafe.ARRAY_BYTE_INDEX_SCALE;

    /** The value of {@code arrayIndexScale(short[].class)} */
    @SuppressWarnings("sunapi")
    public static final int ARRAY_SHORT_INDEX_SCALE = sun.misc.Unsafe.ARRAY_SHORT_INDEX_SCALE;

    /** The value of {@code arrayIndexScale(char[].class)} */
    @SuppressWarnings("sunapi")
    public static final int ARRAY_CHAR_INDEX_SCALE = sun.misc.Unsafe.ARRAY_CHAR_INDEX_SCALE;

    /** The value of {@code arrayIndexScale(int[].class)} */
    @SuppressWarnings("sunapi")
    public static final int ARRAY_INT_INDEX_SCALE = sun.misc.Unsafe.ARRAY_INT_INDEX_SCALE;

    /** The value of {@code arrayIndexScale(long[].class)} */
    @SuppressWarnings("sunapi")
    public static final int ARRAY_LONG_INDEX_SCALE = sun.misc.Unsafe.ARRAY_LONG_INDEX_SCALE;

    /** The value of {@code arrayIndexScale(float[].class)} */
    @SuppressWarnings("sunapi")
    public static final int ARRAY_FLOAT_INDEX_SCALE = sun.misc.Unsafe.ARRAY_FLOAT_INDEX_SCALE;

    /** The value of {@code arrayIndexScale(double[].class)} */
    @SuppressWarnings("sunapi")
    public static final int ARRAY_DOUBLE_INDEX_SCALE = sun.misc.Unsafe.ARRAY_DOUBLE_INDEX_SCALE;

    /** The value of {@code arrayIndexScale(Object[].class)} */
    @SuppressWarnings("sunapi")
    public static final int ARRAY_OBJECT_INDEX_SCALE = sun.misc.Unsafe.ARRAY_OBJECT_INDEX_SCALE;

    /**
     * Singleton of sun.misc.Unsafe. Normally you don't need this as {@link Jvm} class contains all
     * required methods.
     */
    @SuppressWarnings("sunapi")
    public static final sun.misc.Unsafe UNSAFE = pullUnsafe();

    /** The value of {@code addressSize()} */
    @SuppressWarnings("sunapi")
    public static final long ADDRESS_SIZE = sun.misc.Unsafe.ADDRESS_SIZE;

    /** The value of {@code pageSize()} */
    public static final long PAGE_SIZE = UNSAFE.pageSize();

    private Jvm() {
        // nothing to do in an utility class
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

    /**
     * Allocate an instance but do not run any constructor.
     * Initializes the class if it has not yet been.
     */
    public static Object allocateInstance(Class<?> clazz) throws InstantiationException {
        return UNSAFE.allocateInstance(clazz);
    }

    /**
     * Define a class but do not make it known to the class loader or system dictionary.
     * <p>
     * For each CP entry, the corresponding CP patch must either be null or have
     * the a format that matches its tag:
     * <ul>
     * <li>Integer, Long, Float, Double: the corresponding wrapper object type from java.lang
     * <li>Utf8: a string (must have suitable syntax if used as signature or name)
     * <li>Class: any java.lang.Class object
     * <li>String: any object (not just a java.lang.String)
     * <li>InterfaceMethodRef: (NYI) a method handle to invoke on that call site's arguments
     * </ul>
     * @param hostClass context for linkage, access control, protection domain, and class loader
     * @param data      bytes of a class file
     * @param cpPatches where non-null entries exist, they replace corresponding CP entries in data
     */
    public static Class defineAnonymousClass(Class hostClass, byte[] data, Object[] cpPatches) {
        return UNSAFE.defineAnonymousClass(hostClass, data, cpPatches);
    }

    /**
     * Tell the VM to define a class, without security checks.  By default, the
     * class loader and protection domain come from the caller's class.
     */
    public static Class defineClass(String name, byte[] b, int off, int len,
                                    ClassLoader loader, ProtectionDomain protectionDomain)
    {
        return UNSAFE.defineClass(name, b, off, len, loader, protectionDomain);
    }


    /**
     * Ensure the given class has been initialized. This is often
     * needed in conjunction with obtaining the static field base of a
     * class.
     */
    public static void ensureClassInitialized(Class<?> c) {
        UNSAFE.ensureClassInitialized(c);
    }

    /**
     * Detect if the given class may need to be initialized. This is often
     * needed in conjunction with obtaining the static field base of a
     * class.
     * @return false only if a call to {@code ensureClassInitialized} would have no effect
     */
    public static boolean shouldBeInitialized(Class<?> c) {
        return UNSAFE.shouldBeInitialized(c);
    }

    // Field operations

    /**
     * Report the location of a given field in the storage allocation of its
     * class.  Do not expect to perform any sort of arithmetic on this offset;
     * it is just a cookie which is passed to the unsafe heap memory accessors.
     *
     * <p>Any given field will always have the same offset and base, and no
     * two distinct fields of the same class will ever have the same offset
     * and base.
     *
     * <p>As of 1.4.1, offsets for fields are represented as long values,
     * although the Sun JVM does not use the most significant 32 bits.
     * However, JVM implementations which store static fields at absolute
     * addresses can use long offsets and null base pointers to express
     * the field locations in a form usable by {@link #getInt(Object,long)}.
     * Therefore, code which will be ported to such JVMs on 64-bit platforms
     * must preserve all bits of static field offsets.
     * @see #getInt(Object, long)
     */
    public static long staticFieldOffset(Field field) {
        return UNSAFE.staticFieldOffset(field);
    }

    /**
     * Report the location of a given static field, in conjunction with {@link
     * #staticFieldBase}.
     * <p>Do not expect to perform any sort of arithmetic on this offset;
     * it is just a cookie which is passed to the unsafe heap memory accessors.
     *
     * <p>Any given field will always have the same offset, and no two distinct
     * fields of the same class will ever have the same offset.
     *
     * <p>As of 1.4.1, offsets for fields are represented as long values,
     * although the Sun JVM does not use the most significant 32 bits.
     * It is hard to imagine a JVM technology which needs more than
     * a few bits to encode an offset within a non-array object,
     * However, for consistency with other methods in this class,
     * this method reports its result as a long value.
     * @see #getInt(Object, long)
     */
    public static long objectFieldOffset(Field field) {
        return UNSAFE.objectFieldOffset(field);
    }

    /**
     * Report the location of a given static field, in conjunction with {@link
     * #staticFieldOffset}.
     * <p>Fetch the base "Object", if any, with which static fields of the
     * given class can be accessed via methods like {@link #getInt(Object,
     * long)}.  This value may be null.  This value may refer to an object
     * which is a "cookie", not guaranteed to be a real Object, and it should
     * not be used in any way except as argument to the get and put routines in
     * this class.
     */
    public static Object staticFieldBase(Field field) {
        return UNSAFE.staticFieldBase(field);
    }

    /**
     * Report the offset of the first element in the storage allocation of a
     * given array class.  If {@link #arrayIndexScale} returns a non-zero value
     * for the same class, you may use that scale factor, together with this
     * base offset, to form new offsets to access elements of arrays of the
     * given class.
     *
     * @see #getInt(Object, long)
     * @see #putInt(Object, long, int)
     */
    public static int arrayBaseOffset(Class<?> c) {
        return UNSAFE.arrayBaseOffset(c);
    }

    /**
     * Report the scale factor for addressing elements in the storage
     * allocation of a given array class.  However, arrays of "narrow" types
     * will generally not work properly with accessors like {@link
     * #getByte(Object, long)}, so the scale factor for such classes is reported
     * as zero.
     *
     * @see #arrayBaseOffset
     * @see #getInt(Object, long)
     * @see #putInt(Object, long, int)
     */
    public static int arrayIndexScale(Class<?> c) {
        return UNSAFE.arrayIndexScale(c);
    }

    // Memory operations

    /**
     * Allocates a new block of native memory, of the given size in bytes.  The
     * contents of the memory are uninitialized; they will generally be
     * garbage.  The resulting native pointer will never be zero, and will be
     * aligned for all value types.  Dispose of this memory by calling {@link
     * #freeMemory}, or resize it with {@link #reallocateMemory}.
     *
     * @throws IllegalArgumentException if the size is negative or too large
     *         for the native size_t type
     *
     * @throws OutOfMemoryError if the allocation is refused by the system
     *
     * @see #getByte(long)
     * @see #putByte(long, byte)
     */
    public static long allocateMemory(long size) {
        return UNSAFE.allocateMemory(size);
    }

    /**
     * Resizes a new block of native memory, to the given size in bytes.  The
     * contents of the new block past the size of the old block are
     * uninitialized; they will generally be garbage.  The resulting native
     * pointer will be zero if and only if the requested size is zero.  The
     * resulting native pointer will be aligned for all value types.  Dispose
     * of this memory by calling {@link #freeMemory}, or resize it with {@link
     * #reallocateMemory}.  The address passed to this method may be null, in
     * which case an allocation will be performed.
     *
     * @throws IllegalArgumentException if the size is negative or too large
     *         for the native size_t type
     *
     * @throws OutOfMemoryError if the allocation is refused by the system
     *
     * @see #allocateMemory
     */
    public static long reallocateMemory(long address, long size) {
        return UNSAFE.reallocateMemory(address, size);
    }

    /**
     * Disposes of a block of native memory, as obtained from {@link
     * #allocateMemory} or {@link #reallocateMemory}.  The address passed to
     * this method may be null, in which case no action is taken.
     *
     * @see #allocateMemory
     */
    public static void freeMemory(long address) {
        UNSAFE.freeMemory(address);
    }

    /**
     * Sets all bytes in a given block of memory to a fixed value
     * (usually zero).  This provides a <em>single-register</em> addressing mode,
     * as discussed in {@link #getInt(Object,long)}.
     *
     * <p>Equivalent to <code>setMemory(null, address, bytes, value)</code>.
     */
    public static void setMemory(long address, long bytes, byte value) {
        UNSAFE.setMemory(address, bytes, value);
    }

    /**
     * Sets all bytes in a given block of memory to a fixed value
     * (usually zero).
     *
     * <p>This method determines a block's base address by means of two parameters,
     * and so it provides (in effect) a <em>double-register</em> addressing mode,
     * as discussed in {@link #getInt(Object,long)}.  When the object reference is null,
     * the offset supplies an absolute base address.
     *
     * <p>The stores are in coherent (atomic) units of a size determined
     * by the address and length parameters.  If the effective address and
     * length are all even modulo 8, the stores take place in 'long' units.
     * If the effective address and length are (resp.) even modulo 4 or 2,
     * the stores take place in units of 'int' or 'short'.
     *
     * @since 1.7
     */
    public static void setMemory(Object o, long offset, long bytes, byte value) {
        UNSAFE.setMemory(o, offset, bytes, value);
    }

    /**
     * Sets all bytes in a given block of memory to a copy of another
     * block.  This provides a <em>single-register</em> addressing mode,
     * as discussed in {@link #getInt(Object,long)}.
     *
     * Equivalent to <code>copyMemory(null, srcAddress, null, destAddress, bytes)</code>.
     */
    public static void copyMemory(long srcAddress, long dstAddress, long bytes) {
        UNSAFE.copyMemory(srcAddress, dstAddress, bytes);
    }

    /**
     * Sets all bytes in a given block of memory to a copy of another
     * block.
     *
     * <p>This method determines each block's base address by means of two parameters,
     * and so it provides (in effect) a <em>double-register</em> addressing mode,
     * as discussed in {@link #getInt(Object,long)}.  When the object reference is null,
     * the offset supplies an absolute base address.
     *
     * <p>The transfers are in coherent (atomic) units of a size determined
     * by the address and length parameters.  If the effective addresses and
     * length are all even modulo 8, the transfer takes place in 'long' units.
     * If the effective addresses and length are (resp.) even modulo 4 or 2,
     * the transfer takes place in units of 'int' or 'short'.
     *
     * @since 1.7
     */
    public static void copyMemory(Object srcBase, long srcOffset, Object dstBase, long dstOffset, long bytes) {
        UNSAFE.copyMemory(srcBase, srcOffset, dstBase, dstOffset, bytes);
    }

    // Exception handling

    /** Throw the exception without telling the verifier. */
    public static void throwException(Throwable throwable) {
        UNSAFE.throwException(throwable);
    }

    // Thread control

    /**
     * Ensures lack of reordering of loads before the fence
     * with loads or stores after the fence.
     * @since 1.8
     */
    public static void loadFence() {
        UNSAFE.loadFence();
    }

    /**
     * Ensures lack of reordering of stores before the fence
     * with loads or stores after the fence.
     * @since 1.8
     */
    public static void storeFence() {
        UNSAFE.storeFence();
    }

    /**
     * Ensures lack of reordering of loads or stores before the fence
     * with loads or stores after the fence.
     * @since 1.8
     */
    public static void fullFence() {
        UNSAFE.fullFence();
    }

    /**
     * Block current thread, returning when a balancing
     * <tt>unpark</tt> occurs, or a balancing <tt>unpark</tt> has
     * already occurred, or the thread is interrupted, or, if not
     * absolute and time is not zero, the given time nanoseconds have
     * elapsed, or if absolute, the given deadline in milliseconds
     * since Epoch has passed, or spuriously (i.e., returning for no
     * "reason"). Note: This operation is in the Unsafe class only
     * because <tt>unpark</tt> is, so it would be strange to place it
     * elsewhere.
     */
    public static void park(boolean isAbsolute, long time) {
        UNSAFE.park(isAbsolute, time);
    }

    /**
     * Unblock the given thread blocked on <tt>park</tt>, or, if it is
     * not blocked, cause the subsequent call to <tt>park</tt> not to
     * block.  Note: this operation is "unsafe" solely because the
     * caller must somehow ensure that the thread has not been
     * destroyed. Nothing special is usually required to ensure this
     * when called from Java (in which there will ordinarily be a live
     * reference to the thread) but this is not nearly-automatically
     * so when calling from native code.
     * @param thread the thread to unpark.
     *
     */
    public static void unpark(Object thread) {
        UNSAFE.unpark(thread);
    }

    // statistics

    /**
     * Gets the load average in the system run queue assigned
     * to the available processors averaged over various periods of time.
     * This method retrieves the given <tt>nelem</tt> samples and
     * assigns to the elements of the given <tt>loadavg</tt> array.
     * The system imposes a maximum of 3 samples, representing
     * averages over the last 1,  5,  and  15 minutes, respectively.
     *
     * @param loadavg an array of double of size nelems
     * @param nelems the number of samples to be retrieved and
     *         must be 1 to 3.
     *
     * @return the number of samples actually retrieved; or -1
     *         if the load average is unobtainable.
     */
    public static int getLoadAverage(double[] loadavg, int nelems) {
        return UNSAFE.getLoadAverage(loadavg, nelems);
    }

    // CAS

    /**
     * Atomically update Java variable to <tt>x</tt> if it is currently
     * holding <tt>expected</tt>.
     * @return <tt>true</tt> if successful
     */
    public static boolean compareAndSwapInt(Object o, long offset, int expected, int x) {
        return UNSAFE.compareAndSwapInt(o, offset, expected, x);
    }

    /**
     * Atomically update Java variable to <tt>x</tt> if it is currently
     * holding <tt>expected</tt>.
     * @return <tt>true</tt> if successful
     */
    public static boolean compareAndSwapLong(Object o, long offset, long expected, long x) {
        return UNSAFE.compareAndSwapLong(o, offset, expected, x);
    }

    /**
     * Atomically update Java variable to <tt>x</tt> if it is currently
     * holding <tt>expected</tt>.
     * @return <tt>true</tt> if successful
     */
    public static boolean compareAndSwapObject(Object o, long offset, Object expected, Object x) {
        return UNSAFE.compareAndSwapObject(o, offset, expected, x);
    }

    // Plain value accessors

    /**
     * Stores a value into a given memory address.  If the address is zero, or
     * does not point into a block obtained from {@link #allocateMemory}, the
     * results are undefined.
     *
     * @see #getByte(long)
     */
    public static void putByte(long address, byte value) {
        UNSAFE.putByte(address, value);
    }

    /**
     * Fetches a value from a given memory address.  If the address is zero, or
     * does not point into a block obtained from {@link #allocateMemory}, the
     * results are undefined.
     *
     * @see #allocateMemory
     */
    public static byte getByte(long address) {
        return UNSAFE.getByte(address);
    }

    /** @see #putByte(long, byte) */
    public static void putLong(long address, long value) {
        UNSAFE.putLong(address, value);
    }

    /** @see #getByte(long) */
    public static long getLong(long address) {
        return UNSAFE.getLong(address);
    }

    /** @see #putByte(long, byte) */
    public static void putInt(long address, int value) {
        UNSAFE.putInt(address, value);
    }

    /** @see #getByte(long) */
    public static int getInt(long address) {
        return UNSAFE.getInt(address);
    }

    /** @see #putByte(long, byte) */
    public static void putShort(long address, short value) {
        UNSAFE.putShort(address, value);
    }

    /** @see #getByte(long) */
    public static short getShort(long address) {
        return UNSAFE.getShort(address);
    }

    /** @see #putByte(long, byte) */
    public static void putChar(long address, char value) {
        UNSAFE.putChar(address, value);
    }

    /** @see #getByte(long) */
    public static char getChar(long address) {
        return UNSAFE.getChar(address);
    }

    /** @see #putByte(long, byte) */
    public static void putDouble(long address, double value) {
        UNSAFE.putDouble(address, value);
    }

    /** @see #getByte(long) */
    public static double getDouble(long address) {
        return UNSAFE.getDouble(address);
    }

    /** @see #putByte(long, byte) */
    public static void putFloat(long address, float value) {
        UNSAFE.putFloat(address, value);
    }

    /** @see #getByte(long) */
    public static float getFloat(long address) {
        return UNSAFE.getFloat(address);
    }

    /**
     * Stores a native pointer into a given memory address.  If the address is
     * zero, or does not point into a block obtained from {@link
     * #allocateMemory}, the results are undefined.
     *
     * <p> The number of bytes actually written at the target address maybe
     * determined by consulting {@link #addressSize}.
     *
     * @see #getAddress(long)
     */
    public static void putAddress(long address, long value) {
        UNSAFE.putAddress(address, value);
    }

    /**
     * Fetches a native pointer from a given memory address.  If the address is
     * zero, or does not point into a block obtained from {@link
     * #allocateMemory}, the results are undefined.
     *
     * <p> If the native pointer is less than 64 bits wide, it is extended as
     * an unsigned number to a Java long.  The pointer may be indexed by any
     * given byte offset, simply by adding that offset (as a simple integer) to
     * the long representing the pointer.  The number of bytes actually read
     * from the target address maybe determined by consulting {@link
     * #addressSize}.
     *
     * @see #allocateMemory
     */
    public static long getAddress(long address) {
        return UNSAFE.getAddress(address);
    }

    // Object value accessors

    /**
     * Fetches a reference value from a given Java variable.
     * @see #getInt(Object, long)
     */
    public static Object getObject(Object o, long offset) {
        return UNSAFE.getObject(o, offset);
    }

    /**
     * Stores a reference value into a given Java variable.
     * <p>
     * Unless the reference <code>x</code> being stored is either null
     * or matches the field type, the results are undefined.
     * If the reference <code>o</code> is non-null, car marks or
     * other store barriers for that object (if the VM requires them)
     * are updated.
     * @see #putInt(Object, long, int)
     */
    public static void putObject(Object o, long offset, Object value) {
        UNSAFE.putObject(o, offset, value);
    }

    /**
     * Fetches a value from a given Java variable.
     * More specifically, fetches a field or array element within the given
     * object <code>o</code> at the given offset, or (if <code>o</code> is
     * null) from the memory address whose numerical value is the given
     * offset.
     * <p>
     * The results are undefined unless one of the following cases is true:
     * <ul>
     * <li>The offset was obtained from {@link #objectFieldOffset} on
     * the {@link java.lang.reflect.Field} of some Java field and the object
     * referred to by <code>o</code> is of a class compatible with that
     * field's class.
     *
     * <li>The offset and object reference <code>o</code> (either null or
     * non-null) were both obtained via {@link #staticFieldOffset}
     * and {@link #staticFieldBase} (respectively) from the
     * reflective {@link Field} representation of some Java field.
     *
     * <li>The object referred to by <code>o</code> is an array, and the offset
     * is an integer of the form <code>B+N*S</code>, where <code>N</code> is
     * a valid index into the array, and <code>B</code> and <code>S</code> are
     * the values obtained by {@link #arrayBaseOffset} and {@link
     * #arrayIndexScale} (respectively) from the array's class.  The value
     * referred to is the <code>N</code><em>th</em> element of the array.
     *
     * </ul>
     * <p>
     * If one of the above cases is true, the call references a specific Java
     * variable (field or array element).  However, the results are undefined
     * if that variable is not in fact of the type returned by this method.
     * <p>
     * This method refers to a variable by means of two parameters, and so
     * it provides (in effect) a <em>double-register</em> addressing mode
     * for Java variables.  When the object reference is null, this method
     * uses its offset as an absolute address.  This is similar in operation
     * to methods such as {@link #getInt(long)}, which provide (in effect) a
     * <em>single-register</em> addressing mode for non-Java variables.
     * However, because Java variables may have a different layout in memory
     * from non-Java variables, programmers should not assume that these
     * two addressing modes are ever equivalent.  Also, programmers should
     * remember that offsets from the double-register addressing mode cannot
     * be portably confused with longs used in the single-register addressing
     * mode.
     *
     * @param o Java heap object in which the variable resides, if any, else
     *        null
     * @param offset indication of where the variable resides in a Java heap
     *        object, if any, else a memory address locating the variable
     *        statically
     * @return the value fetched from the indicated Java variable
     * @throws RuntimeException No defined exceptions are thrown, not even
     *         {@link NullPointerException}
     */
    public static int getInt(Object o, long offset) {
        return UNSAFE.getInt(o, offset);
    }

    /**
     * Stores a value into a given Java variable.
     * <p>
     * The first two parameters are interpreted exactly as with
     * {@link #getInt(Object, long)} to refer to a specific
     * Java variable (field or array element).  The given value
     * is stored into that variable.
     * <p>
     * The variable must be of the same type as the method
     * parameter <code>x</code>.
     *
     * @param o Java heap object in which the variable resides, if any, else
     *        null
     * @param offset indication of where the variable resides in a Java heap
     *        object, if any, else a memory address locating the variable
     *        statically
     * @param value the value to store into the indicated Java variable
     * @throws RuntimeException No defined exceptions are thrown, not even
     *         {@link NullPointerException}
     */
    public static void putInt(Object o, long offset, int value) {
        UNSAFE.putInt(o, offset, value);
    }

    /** @see #getInt(Object, long) */
    public static boolean getBoolean(Object o, long offset) {
        return UNSAFE.getBoolean(o, offset);
    }

    /** @see #putInt(Object, long, int) */
    public static void putBoolean(Object o, long offset, boolean value) {
        UNSAFE.putBoolean(o, offset, value);
    }

    /** @see #getInt(Object, long) */
    public static byte getByte(Object o, long offset) {
        return UNSAFE.getByte(o, offset);
    }

    /** @see #putInt(Object, long, int) */
    public static void putByte(Object o, long offset, byte value) {
        UNSAFE.putByte(o, offset, value);
    }

    /** @see #getInt(Object, long) */
    public static short getShort(Object o, long offset) {
        return UNSAFE.getShort(o, offset);
    }

    /** @see #putInt(Object, long, int) */
    public static void putShort(Object o, long offset, short value) {
        UNSAFE.putShort(o, offset, value);
    }

    /** @see #getInt(Object, long) */
    public static char getChar(Object o, long offset) {
        return UNSAFE.getChar(o, offset);
    }

    /** @see #putInt(Object, long, int) */
    public static void putChar(Object o, long offset, char value) {
        UNSAFE.putChar(o, offset, value);
    }

    /** @see #getInt(Object, long) */
    public static long getLong(Object o, long offset) {
        return UNSAFE.getLong(o, offset);
    }

    /** @see #putInt(Object, long, int) */
    public static void putLong(Object o, long offset, long value) {
        UNSAFE.putLong(o, offset, value);
    }

    /** @see #getInt(Object, long) */
    public static float getFloat(Object o, long offset) {
        return UNSAFE.getFloat(o, offset);
    }

    /** @see #putInt(Object, long, int) */
    public static void putFloat(Object o, long offset, float value) {
        UNSAFE.putFloat(o, offset, value);
    }

    /** @see #getInt(Object, long) */
    public static double getDouble(Object o, long offset) {
        return UNSAFE.getDouble(o, offset);
    }

    /** @see #putInt(Object, long, int) */
    public static void putDouble(Object o, long offset, double value) {
        UNSAFE.putDouble(o, offset, value);
    }

    // Object volatile value accessors

    /**
     * Fetches a reference value from a given Java variable, with volatile
     * load semantics. Otherwise identical to {@link #getObject(Object, long)}
     */
    public static Object getObjectVolatile(Object o, long offset) {
        return UNSAFE.getObjectVolatile(o, offset);
    }

    /**
     * Stores a reference value into a given Java variable, with
     * volatile store semantics. Otherwise identical to {@link #putObject(Object, long, Object)}
     */
    public static void putObjectVolatile(Object o, long offset, Object value) {
        UNSAFE.putObjectVolatile(o, offset, value);
    }

    /** Volatile version of {@link #getInt(Object, long)}  */
    public static int getIntVolatile(Object o, long offset) {
        return UNSAFE.getIntVolatile(o, offset);
    }

    /** Volatile version of {@link #putInt(Object, long, int)}  */
    public static void putIntVolatile(Object o, long offset, int value) {
        UNSAFE.putIntVolatile(o, offset, value);
    }

    /** Volatile version of {@link #getBoolean(Object, long)}  */
    public static boolean getBooleanVolatile(Object o, long offset) {
        return UNSAFE.getBooleanVolatile(o, offset);
    }

    /** Volatile version of {@link #putBoolean(Object, long, boolean)}  */
    public static void putBooleanVolatile(Object o, long offset, boolean value) {
        UNSAFE.putBooleanVolatile(o, offset, value);
    }

    /** Volatile version of {@link #getByte(Object, long)}  */
    public static byte getByteVolatile(Object o, long offset) {
        return UNSAFE.getByteVolatile(o, offset);
    }

    /** Volatile version of {@link #putByte(Object, long, byte)}  */
    public static void putByteVolatile(Object o, long offset, byte value) {
        UNSAFE.putByteVolatile(o, offset, value);
    }

    /** Volatile version of {@link #getShort(Object, long)}  */
    public static short getShortVolatile(Object o, long offset) {
        return UNSAFE.getShortVolatile(o, offset);
    }

    /** Volatile version of {@link #putShort(Object, long, short)}  */
    public static void putShortVolatile(Object o, long offset, short value) {
        UNSAFE.putShortVolatile(o, offset, value);
    }

    /** Volatile version of {@link #getChar(Object, long)}  */
    public static char getCharVolatile(Object o, long offset) {
        return UNSAFE.getCharVolatile(o, offset);
    }

    /** Volatile version of {@link #putChar(Object, long, char)}  */
    public static void putCharVolatile(Object o, long offset, char value) {
        UNSAFE.putCharVolatile(o, offset, value);
    }

    /** Volatile version of {@link #getLong(Object, long)}  */
    public static long getLongVolatile(Object o, long offset) {
        return UNSAFE.getLongVolatile(o, offset);
    }

    /** Volatile version of {@link #putLong(Object, long, long)}  */
    public static void putLongVolatile(Object o, long offset, long value) {
        UNSAFE.putLongVolatile(o, offset, value);
    }

    /** Volatile version of {@link #getFloat(Object, long)}  */
    public static float getFloatVolatile(Object o, long offset) {
        return UNSAFE.getFloatVolatile(o, offset);
    }

    /** Volatile version of {@link #putFloat(Object, long, float)}  */
    public static void putFloatVolatile(Object o, long offset, float value) {
        UNSAFE.putFloatVolatile(o, offset, value);
    }

    /** Volatile version of {@link #getDouble(Object, long)}  */
    public static double getDoubleVolatile(Object o, long offset) {
        return UNSAFE.getDoubleVolatile(o, offset);
    }

    /** Volatile version of {@link #putDouble(Object, long, double)}  */
    public static void putDoubleVolatile(Object o, long offset, double value) {
        UNSAFE.putDoubleVolatile(o, offset, value);
    }

    // Object ordered value accessors

    /**
     * Version of {@link #putObjectVolatile(Object, long, Object)}
     * that does not guarantee immediate visibility of the store to
     * other threads. This method is generally only useful if the
     * underlying field is a Java volatile (or if an array cell, one
     * that is otherwise only accessed using volatile accesses).
     */
    public static void putOrderedObject(Object o, long offset, Object value) {
        UNSAFE.putOrderedObject(o, offset, value);
    }

    /**
     * Ordered/Lazy version of {@link #putIntVolatile(Object, long, int)}
     */
    public static void putOrderedInt(Object o, long offset, int value) {
        UNSAFE.putOrderedInt(o, offset, value);
    }

    /**
     * Ordered/Lazy version of {@link #putLongVolatile(Object, long, long)}
     */
    public static void putOrderedLong(Object o, long offset, long value) {
        UNSAFE.putOrderedLong(o, offset, value);
    }

    /**
     * Report the size in bytes of a native pointer, as stored via {@link
     * #putAddress}.  This value will be either 4 or 8.  Note that the sizes of
     * other primitive types (as stored in native memory blocks) is determined
     * fully by their information content.
     */
    public static void addressSize() {
        UNSAFE.addressSize();
    }

    /**
     * Report the size in bytes of a native memory page (whatever that is).
     * This value will always be a power of two.
     */
    public static void pageSize() {
        UNSAFE.pageSize();
    }

}
