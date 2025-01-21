package jx.net;


import jx.zero.Memory;

public class IPDataImpl implements IPData {
    public IPAddress sourceAddress;
    public IPAddress destinationAddress;
    public Memory mem;
    public int offset, size; // only used when memory splitting is avoided

    @Override
    public int Size() {
        return size;
    }

    @Override
    public int getOffset() {
        return offset;
    }

    @Override
    public Memory getMemory() {
        return mem;
    }

    @Override
    public void setMemory(Memory buf) {
        this.mem = buf;
    }

    @Override
    public IPAddress getSourceAddress() {
        return sourceAddress;
    }

    @Override
    public void setOffset(int i) {
        this.offset = i;
    }

    @Override
    public IPAddress getDestinationAddress() {
        return destinationAddress;
    }

    @Override
    public void setSourceAddress(IPAddress addr) {
        this.sourceAddress = addr;
    }

    @Override
    public void setDestinationAddress(IPAddress dAddr) {
        this.destinationAddress = dAddr;
    }

    @Override
    public void setSize(int i) {
        this.size = i;
    }
}
