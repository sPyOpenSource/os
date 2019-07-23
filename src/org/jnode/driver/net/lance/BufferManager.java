/*
 * $Id$
 *
 * Copyright (C) 2003-2015 JNode.org
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public 
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; If not, write to the Free Software Foundation, Inc., 
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
 
package org.jnode.driver.net.lance;

//import org.apache.log4j.Logger;
import jx.zero.Debug;
import jx.zero.Memory;
import jx.zero.MemoryManager;
import metaxa.os.devices.net.AdapterLimits;
//import org.jnode.net.ethernet.EthernetAddress;
//import org.jnode.system.resource.ResourceNotFreeException;
//import org.jnode.system.resource.ResourceOwner;

/**
 * @author Chris Cole
 */
public class BufferManager {
    public static final int DATA_BUFFER_SIZE = 1544;

    /**
     * MemoryResource to hold initialization block, descriptor rings, and data buffers
     */
    private Memory mem;
    private MemoryManager rm;

    private InitializationBlock32Bit initBlock;
    private RxDescriptorRing rxRing;
    private TxDescriptorRing txRing;

    private final int size;

    public BufferManager(int rxRingLength, int txRingLength, int mode,
            metaxa.os.devices.net.EthernetAdress physicalAddr, long logicalAddr, MemoryManager rm) {

        // Compute the required size for the memory resource
        size = InitializationBlock32Bit.INIT_BLOCK_SIZE +
                (rxRingLength + txRingLength) * (Descriptor.MESSAGE_DESCRIPTOR_SIZE + DATA_BUFFER_SIZE);
        Debug.out.println(size);

        // Get the memory
        //try {
        this.rm = rm;
        /*mem = rm.alloc(size);//rm.claimMemoryResource(owner, null, size, ResourceManager.MEMMODE_NORMAL);
        //} catch (ResourceNotFreeException e) {
          //  System.out.println("buffer memory resouce not free exception");
        //}
Debug.out.println("rxring");
        // define the offsets into the memory resource for the entities
        final int rxRingOffset = InitializationBlock32Bit.INIT_BLOCK_SIZE;

        final int txRingOffset = rxRingOffset + (rxRingLength * Descriptor.MESSAGE_DESCRIPTOR_SIZE);

        final int rxDataBufferOffset =
                txRingOffset + (txRingLength * Descriptor.MESSAGE_DESCRIPTOR_SIZE);

        final int txDataBufferOffset = rxDataBufferOffset + (rxRingLength * DATA_BUFFER_SIZE);

        // Create and initialize the receive ring
        rxRing = new RxDescriptorRing(mem, rxRingOffset, rxRingLength, rxDataBufferOffset);

        // Create and initialize the transmit ring
        txRing = new TxDescriptorRing(mem, txRingOffset, txRingLength, txDataBufferOffset);

        // Create and initialize the initialization block
        initBlock = new InitializationBlock32Bit(
                mem, 0, (short) mode, physicalAddr, logicalAddr, rxRing, txRing);*/
    }

    /**
     * Gets the address of the initdata structure as a 32-bit int
     * @return 
     */
    public final int getInitDataAddressAs32Bit() {
        return mem.getStartAddress();
    }

    public void transmit(Memory buf) {
        final int len = buf.size();
        if (len > DATA_BUFFER_SIZE) {
            System.out.println("Length must be <= " + DATA_BUFFER_SIZE);
        }
        txRing.transmit(buf);
    }

    public Memory getPacket() {
        return rxRing.getPacket(rm);
    }

    /*public void dumpData(Logger out) {
        initBlock.dumpData(out);
        rxRing.dumpData(out);
        txRing.dumpData(out);
    }*/
}
