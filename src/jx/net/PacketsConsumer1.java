package jx.net;

import jx.zero.*;

public interface PacketsConsumer1 {

    Memory processMemory(Memory data, int offset, int size);

    /**
     * Get number of bytes need for header information
     * @return 
     */
    int requiresSpace();
    /**
     * Get Maximum Transfer Unit of the underlying network 
     * @return 
     */
    int getMTU();
}
