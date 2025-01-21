package jx.net;


import jx.zero.Memory;

public class UDPDataImpl implements UDPData {
    public IPAddress sourceAddress;
    public int sourcePort;
    public Memory mem;
    public int offset, size; // only used when memory splitting is avoided

    @Override
    public void setMemory(Memory mem) {
        this.mem = mem;
    }

    @Override
    public void setSourcePort(int srcPort) {
        this.sourcePort = srcPort;
    }

    @Override
    public void setSourceAddress(IPAddress sourceAddress) {
        this.sourceAddress = sourceAddress;
    }

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
    public IPAddress getSourceAddress() {
        return sourceAddress;
    }

    @Override
    public int getSourcePort() {
        return sourcePort;
    }
}
