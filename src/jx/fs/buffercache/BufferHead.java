package jx.fs.buffercache;

import jx.zero.Memory;

public abstract class BufferHead extends jx.buffer.BufferHead {

    public int b_flushtime;
    public BufferHead(Memory buf) { super(buf); }

    @Override
    public abstract int getBlock();
    @Override
    public abstract int getSize();
    @Override
    public abstract void endIo(boolean error, boolean synchronous);

    public abstract boolean isDirty();
    public abstract void markClean();

    public abstract boolean isUptodate();

    public abstract boolean isLocked();
    public abstract void lock();
    public abstract void unlock();
    public abstract void waitUntilUnlocked();
    public abstract void waitOn();


    public abstract void clear(int from, int to);

    public abstract boolean isUsed();
    public abstract boolean isUsedOnlyByMe();

    public boolean isInFreeList() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
