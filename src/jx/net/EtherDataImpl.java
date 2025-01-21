package jx.net;


import jx.zero.Memory;

public class EtherDataImpl implements EtherData {
    public byte[] srcAddress;
    public byte[] dstAddress;
    public Memory mem;
    public int offset, size; // only used when memory splitting is avoided

    @Override
    public Memory getMemory() {
        return mem;
    }

    @Override
    public int getOffset() {
        return offset;
    }

    @Override
    public int Size() {
        return size;
    }

    @Override
    public void setMemory(Memory newMem) {
        this.mem = newMem;
    }

    @Override
    public void setOffset(int i) {
        this.offset = i;
    }

    @Override
    public void setSize(int i) {
        this.size = i;
    }

    @Override
    public void setSrcAddress(byte[] sourceAddress) {
        this.srcAddress = sourceAddress;
    }

    @Override
    public void setDstAddress(byte[] destAddress) {
        this.dstAddress = destAddress;
    }
}
