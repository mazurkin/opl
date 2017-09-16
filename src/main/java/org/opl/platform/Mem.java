package org.opl.platform;

/**
 * Memory constant definitions
 */
@SuppressWarnings("PMD.ShortClassName")
public final class Mem {

    public static final long KB = 1024;

    public static final long MB = 1024 * KB;

    public static final long GB = 1024 * MB;

    public static final long TB = 1024 * GB;

    public static final long PB = 1024 * TB;

    public static final long EB = 1024 * PB;

    public static final long BYTE_SIZE = Byte.BYTES;

    public static final long CHAR_SIZE = Character.BYTES;

    public static final long SHORT_SIZE = Short.BYTES;

    public static final long INTEGER_SIZE = Integer.BYTES;

    public static final long LONG_SIZE = Long.BYTES;

    public static final long FLOAT_SIZE = Float.BYTES;

    public static final long DOUBLE_SIZE = Double.BYTES;

    private Mem() {
    }
}
