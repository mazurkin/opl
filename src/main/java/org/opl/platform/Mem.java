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

    public static final long BYTE_SIZE_BYTES = Byte.BYTES;
    public static final long BYTE_SIZE_BITS = Byte.SIZE;
    public static final int BYTE_MASK = 0x0000_00FF;

    public static final long CHAR_SIZE_BYTES = Character.BYTES;
    public static final long CHAR_SIZE_BITS = Character.SIZE;
    public static final int CHAR_MASK = 0x0000_FFFF;

    public static final long SHORT_SIZE_BYTES = Short.BYTES;
    public static final long SHORT_SIZE_BITS = Short.SIZE;
    public static final int SHORT_MASK = 0x0000_FFFF;

    public static final long INTEGER_SIZE_BYTES = Integer.BYTES;
    public static final long INTEGER_SIZE_BITS = Integer.SIZE;
    public static final long INTEGER_MASK = 0x0000_0000_FFFF_FFFF;

    public static final long LONG_SIZE_BYTES = Long.BYTES;
    public static final long LONG_SIZE_BITS = Long.SIZE;

    public static final long FLOAT_SIZE_BYTES = Float.BYTES;
    public static final long FLOAT_SIZE_BITS = Float.SIZE;

    public static final long DOUBLE_SIZE_BYTES = Double.BYTES;
    public static final long DOUBLE_SIZE_BITS = Double.SIZE;

    public static final byte BIT_00 = (byte) bit(0);
    public static final byte BIT_01 = (byte) bit(1);
    public static final byte BIT_02 = (byte) bit(2);
    public static final byte BIT_03 = (byte) bit(3);
    public static final byte BIT_04 = (byte) bit(4);
    public static final byte BIT_05 = (byte) bit(5);
    public static final byte BIT_06 = (byte) bit(6);
    public static final byte BIT_07 = (byte) bit(7);

    public static final short BIT_08 = (short) bit(8);
    public static final short BIT_09 = (short) bit(9);
    public static final short BIT_10 = (short) bit(10);
    public static final short BIT_11 = (short) bit(11);
    public static final short BIT_12 = (short) bit(12);
    public static final short BIT_13 = (short) bit(13);
    public static final short BIT_14 = (short) bit(14);
    public static final short BIT_15 = (short) bit(15);

    public static final int BIT_16 = (int) bit(16);
    public static final int BIT_17 = (int) bit(17);
    public static final int BIT_18 = (int) bit(18);
    public static final int BIT_19 = (int) bit(19);
    public static final int BIT_20 = (int) bit(20);
    public static final int BIT_21 = (int) bit(21);
    public static final int BIT_22 = (int) bit(22);
    public static final int BIT_23 = (int) bit(23);

    public static final int BIT_24 = (int) bit(24);
    public static final int BIT_25 = (int) bit(25);
    public static final int BIT_26 = (int) bit(26);
    public static final int BIT_27 = (int) bit(27);
    public static final int BIT_28 = (int) bit(28);
    public static final int BIT_29 = (int) bit(29);
    public static final int BIT_30 = (int) bit(30);
    public static final int BIT_31 = (int) bit(31);

    public static final long BIT_32 = bit(32);
    public static final long BIT_33 = bit(33);
    public static final long BIT_34 = bit(34);
    public static final long BIT_35 = bit(35);
    public static final long BIT_36 = bit(36);
    public static final long BIT_37 = bit(37);
    public static final long BIT_38 = bit(38);
    public static final long BIT_39 = bit(39);

    public static final long BIT_40 = bit(40);
    public static final long BIT_41 = bit(41);
    public static final long BIT_42 = bit(42);
    public static final long BIT_43 = bit(43);
    public static final long BIT_44 = bit(44);
    public static final long BIT_45 = bit(45);
    public static final long BIT_46 = bit(46);
    public static final long BIT_47 = bit(47);

    public static final long BIT_48 = bit(48);
    public static final long BIT_49 = bit(49);
    public static final long BIT_50 = bit(50);
    public static final long BIT_51 = bit(51);
    public static final long BIT_52 = bit(52);
    public static final long BIT_53 = bit(53);
    public static final long BIT_54 = bit(54);
    public static final long BIT_55 = bit(55);

    public static final long BIT_56 = bit(56);
    public static final long BIT_57 = bit(57);
    public static final long BIT_58 = bit(58);
    public static final long BIT_59 = bit(59);
    public static final long BIT_60 = bit(60);
    public static final long BIT_61 = bit(61);
    public static final long BIT_62 = bit(62);
    public static final long BIT_63 = bit(63);

    private static final int BIT_INDEX_SHIFT = 3;
    private static final int BIT_INDEX_MASK = 0x07;

    private Mem() {
    }

    // single bits

    public static long bit(int bit) {
        return longBit(bit);
    }

    public static long longBit(int bit) {
        if (0 <= bit && bit < LONG_SIZE_BITS) {
            return 1L << bit;
        } else {
            throw new IndexOutOfBoundsException("Illegal bit number: " + bit);
        }
    }

    public static int intBit(int bit) {
        if (0 <= bit && bit < INTEGER_SIZE_BITS) {
            return 1 << bit;
        } else {
            throw new IndexOutOfBoundsException("Illegal bit number: " + bit);
        }
    }

    public static short shortBit(int bit) {
        if (0 <= bit && bit < SHORT_SIZE_BITS) {
            return (short) (1 << bit);
        } else {
            throw new IndexOutOfBoundsException("Illegal bit number: " + bit);
        }
    }

    public static byte byteBit(int bit) {
        if (0 <= bit && bit < SHORT_SIZE_BITS) {
            return (byte) (1 << bit);
        } else {
            throw new IndexOutOfBoundsException("Illegal bit number: " + bit);
        }
    }

    // masks

    public static long mask(int bitFromIncluding, int bitToExcluding) {
        return longMask(bitFromIncluding, bitToExcluding);
    }

    public static long longMask(int bitFromIncluding, int bitToExcluding) {
        if (bitToExcluding >= bitFromIncluding) {
            long m1 = (bitToExcluding == LONG_SIZE_BITS) ? -1 : longBit(bitToExcluding) - 1;
            long m2 = longBit(bitFromIncluding) - 1;

            return m1 & ~m2;
        } else {
            throw new IndexOutOfBoundsException("Invalid bit range: " + bitFromIncluding + ".." + bitToExcluding);
        }
    }

    public static int intMask(int bitFromIncluding, int bitToExcluding) {
        if (bitToExcluding >= bitFromIncluding) {
            int m1 = (bitToExcluding == INTEGER_SIZE_BITS) ? -1 : intBit(bitToExcluding) - 1;
            int m2 = intBit(bitFromIncluding) - 1;

            return m1 & ~m2;
        } else {
            throw new IndexOutOfBoundsException("Invalid bit range: " + bitFromIncluding + ".." + bitToExcluding);
        }
    }

    public static short shortMask(int bitFromIncluding, int bitToExcluding) {
        if (bitToExcluding >= bitFromIncluding) {
            int m1 = (bitToExcluding == SHORT_SIZE_BITS) ? -1 : shortBit(bitToExcluding) - 1;
            int m2 = shortBit(bitFromIncluding) - 1;

            return (short) (m1 & ~m2);
        } else {
            throw new IndexOutOfBoundsException("Invalid bit range: " + bitFromIncluding + ".." + bitToExcluding);
        }
    }

    public static byte byteMask(int bitFromIncluding, int bitToExcluding) {
        if (bitToExcluding >= bitFromIncluding) {
            int m1 = (bitToExcluding == BYTE_SIZE_BITS) ? -1 : byteBit(bitToExcluding) - 1;
            int m2 = byteBit(bitFromIncluding) - 1;

            return (byte) (m1 & ~m2);
        } else {
            throw new IndexOutOfBoundsException("Invalid bit range: " + bitFromIncluding + "..-" + bitToExcluding);
        }
    }

    // bit operations

    public static byte setByteBits(byte value, byte mask) {
        return (byte) (value | mask);
    }

    public static byte clearByteBits(byte value, byte mask) {
        return (byte) (value & ~mask);
    }

    public static byte toggleByteBits(byte value, byte mask) {
        return (byte) (value ^ mask);
    }

    public static boolean checkByteBitsSet(byte value, byte mask) {
        return (value & mask) != 0;
    }

    public static boolean checkByteBitsClear(byte value, byte mask) {
        return (value & mask) == 0;
    }

    public static short setShortBits(short value, short mask) {
        return (short) (value | mask);
    }

    public static short clearShortBits(short value, short mask) {
        return (short) (value & ~mask);
    }

    public static short toggleShortBits(short value, short mask) {
        return (short) (value ^ mask);
    }

    public static boolean checkShortBitsSet(short value, short mask) {
        return (value & mask) != 0;
    }

    public static boolean checkShortBitsClear(short value, short mask) {
        return (value & mask) == 0;
    }

    public static int setIntBits(int value, int mask) {
        return value | mask;
    }

    public static int clearIntBits(int value, int mask) {
        return value & ~mask;
    }

    public static int toggleIntBits(int value, int mask) {
        return value ^ mask;
    }

    public static boolean checkIntBitsSet(int value, int mask) {
        return (value & mask) != 0;
    }

    public static boolean checkIntBitsClear(int value, int mask) {
        return (value & mask) == 0;
    }

    public static long setLongBits(long value, long mask) {
        return value | mask;
    }

    public static long clearLongBits(long value, long mask) {
        return value & ~mask;
    }

    public static long toggleLongBits(long value, long mask) {
        return value ^ mask;
    }

    public static boolean checkLongBitsSet(long value, long mask) {
        return (value & mask) != 0;
    }

    public static boolean checkLongBitsClear(long value, long mask) {
        return (value & mask) == 0;
    }

    // bit indexing

    public static long offsetFromBitIndex(long bitIndex) {
        return bitIndex >> BIT_INDEX_SHIFT;
    }

    public static byte maskFromBitIndex(long bitIndex) {
        return (byte) (1 << (bitIndex & BIT_INDEX_MASK));
    }

}
