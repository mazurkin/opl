package org.opl.access;

import org.opl.access.block.MemoryBlock;
import org.opl.platform.Jvm;
import org.opl.platform.Mem;

import javax.annotation.concurrent.NotThreadSafe;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.EOFException;
import java.io.IOException;

@NotThreadSafe
public class MemoryAccessor implements DataInput, DataOutput {

    private final MemoryBlock block;

    private long address;

    public MemoryAccessor(MemoryBlock block) {
        if (block.size() > (long) Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Block is too huge for DataInput and DataOutput");
        }

        this.block = block;
        this.address = block.address();
    }

    public long available() {
        return block.available(address);
    }

    public long offset() {
        return block.offset(address);
    }

    public void offset(long offset) {
        this.address = block.address(offset);
    }

    public void shift(long delta) {
        this.address = block.address(block.offset(address) + delta);
    }

    @Override
    public int skipBytes(int n) throws IOException {
        int skipped = (int) Math.min(n, block.available(address));

        shift(skipped);

        return skipped;
    }

    @Override
    public void write(byte[] b) throws IOException {
        if (b == null) {
            throw new NullPointerException("Accepting array is not specified");
        }

        for (int i = 0, limit = b.length; i < limit; i++) {
            Jvm.putByte(address, b[i]);
            shift(Mem.BYTE_SIZE_BYTES);
        }
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        if (b == null) {
            throw new NullPointerException("Accepting array is not specified");
        }
        if (off < 0) {
            throw new IndexOutOfBoundsException("Offset is invalid");
        }
        if (len < 0) {
            throw new IndexOutOfBoundsException("Length is invalid");
        }
        if (off + len > b.length) {
            throw new IndexOutOfBoundsException("Offset and length are invalid");
        }

        for (int i = off, limit = off + len; i < limit; i++) {
            Jvm.putByte(address, b[i]);
            shift(Mem.BYTE_SIZE_BYTES);
        }
    }

    @Override
    public void readFully(byte[] b) throws IOException {
        if (b == null) {
            throw new NullPointerException("Accepting array is not specified");
        }
        if (b.length == 0) {
            return;
        }
        if (block.available(address) < b.length) {
            throw new EOFException("There are not enough bytes to fill the specified array");
        }

        for (int i = 0, limit = b.length; i < limit; i++) {
            b[i] = Jvm.getByte(address);
            shift(Mem.BYTE_SIZE_BYTES);
        }
    }

    @Override
    public void readFully(byte[] b, int off, int len) throws IOException {
        if (b == null) {
            throw new NullPointerException("Accepting array is not specified");
        }
        if (off < 0) {
            throw new IndexOutOfBoundsException("Offset is invalid");
        }
        if (len < 0) {
            throw new IndexOutOfBoundsException("Length is invalid");
        }
        if (off + len > b.length) {
            throw new IndexOutOfBoundsException("Offset and length are invalid");
        }
        if (block.available(address) < len) {
            throw new EOFException("There are not enough bytes to fill the specified array");
        }

        for (int i = off, limit = off + len; i < limit; i++) {
            b[i] = Jvm.getByte(address);
            shift(Mem.BYTE_SIZE_BYTES);
        }
    }

    @Override
    public void write(int b) throws IOException {

    }

    @Override
    public void writeBoolean(boolean v) throws IOException {

    }

    @Override
    public boolean readBoolean() throws IOException {
        return false;
    }

    @Override
    public void writeByte(int v) throws IOException {
        Jvm.putByte(address, (byte) v);
        shift(Mem.BYTE_SIZE_BYTES);
    }

    @Override
    public byte readByte() throws IOException {
        byte b = Jvm.getByte(address);
        shift(Mem.BYTE_SIZE_BYTES);
        return b;
    }

    @Override
    public int readUnsignedByte() throws IOException {
        return 0;
    }

    @Override
    public void writeShort(int v) throws IOException {

    }

    @Override
    public short readShort() throws IOException {
        return 0;
    }

    @Override
    public int readUnsignedShort() throws IOException {
        return 0;
    }

    @Override
    public void writeChar(int v) throws IOException {

    }

    @Override
    public char readChar() throws IOException {
        return 0;
    }

    @Override
    public void writeInt(int v) throws IOException {

    }

    @Override
    public int readInt() throws IOException {
        return 0;
    }

    @Override
    public void writeLong(long v) throws IOException {

    }

    @Override
    public long readLong() throws IOException {
        return 0;
    }

    @Override
    public void writeFloat(float v) throws IOException {

    }

    @Override
    public float readFloat() throws IOException {
        return 0;
    }

    @Override
    public void writeDouble(double v) throws IOException {

    }

    @Override
    public double readDouble() throws IOException {
        return 0;
    }

    @Override
    public void writeUTF(String s) throws IOException {

    }
    @Override
    public String readUTF() throws IOException {
        return null;
    }

    @Override
    public void writeBytes(String s) throws IOException {

    }

    @Override
    public void writeChars(String s) throws IOException {

    }

    @Override
    public String readLine() throws IOException {
        return null;
    }


}
