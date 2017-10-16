package org.opl.access;

import org.opl.platform.Mem;

import javax.annotation.concurrent.NotThreadSafe;

import java.io.DataOutput;
import java.io.IOException;
import java.io.UTFDataFormatException;

@NotThreadSafe
public class MemoryCalculator implements DataOutput {

    private long size;

    public MemoryCalculator() {
        this.size = 0;
    }

    private void increment(long delta) {
        this.size += delta;
    }

    @Override
    public void write(int b) throws IOException {
        increment(Mem.BYTE_SIZE_BYTES);
    }

    @Override
    public void write(byte[] b) throws IOException {
        increment(Mem.BYTE_SIZE_BYTES * b.length);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        increment(Mem.BYTE_SIZE_BYTES * len);
    }

    @Override
    public void writeBoolean(boolean v) throws IOException {
        increment(Mem.BYTE_SIZE_BYTES);
    }

    @Override
    public void writeByte(int v) throws IOException {
        increment(Mem.BYTE_SIZE_BYTES);
    }

    @Override
    public void writeShort(int v) throws IOException {
        increment(Mem.SHORT_SIZE_BYTES);
    }

    @Override
    public void writeChar(int v) throws IOException {
        increment(Mem.CHAR_SIZE_BYTES);
    }

    @Override
    public void writeInt(int v) throws IOException {
        increment(Mem.INTEGER_SIZE_BYTES);
    }

    @Override
    public void writeLong(long v) throws IOException {
        increment(Mem.LONG_SIZE_BYTES);
    }

    @Override
    public void writeFloat(float v) throws IOException {
        increment(Mem.FLOAT_SIZE_BYTES);
    }

    @Override
    public void writeDouble(double v) throws IOException {
        increment(Mem.DOUBLE_SIZE_BYTES);
    }

    @Override
    public void writeBytes(String s) throws IOException {
        increment(Mem.BYTE_SIZE_BYTES * s.length());
    }

    @Override
    public void writeChars(String s) throws IOException {
        increment(Mem.CHAR_SIZE_BYTES * s.length());
    }

    @Override
    public void writeUTF(String s) throws IOException {
        int strlen = s.length();
        int utflen = 0;
        int count = 0;

        /* use charAt instead of copying String to char array */
        for (int i = 0; i < strlen; i++) {
            int c = s.charAt(i);
            if ((c >= 0x0001) && (c <= 0x007F)) {
                utflen++;
            } else if (c > 0x07FF) {
                utflen += 3;
            } else {
                utflen += 2;
            }
        }

        if (utflen > 65535) {
            throw new UTFDataFormatException("encoded string too long: " + utflen + " bytes");
        }

        increment(utflen);
    }
}
