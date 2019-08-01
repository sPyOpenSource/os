package jx.net;

import jx.buffer.separator.MemoryConsumer;


public interface PacketsConsumer extends MemoryConsumer  {

    // public Memory sendPacket(byte[] addr, Memory buf, int offset, int count);
    /**
     * Get number of bytes need for header information
     * @return 
     */
    public int requiresSpace();
    /**
     * Get Maximum Transfer Unit of the underlying network 
     * @return 
     */
    public int getMTU();
}
