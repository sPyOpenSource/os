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
 
package org.jnode.driver.bus.usb.uhci;

import jx.zero.Memory;
import jx.zero.MemoryManager;

/**
 * Generic descriptor class, that supports memory alignment
 * of the structure itself.
 *
 * @author Ewout Prangsma (epr@users.sourceforge.net)
 */
public abstract class AbstractStructure {

    private final byte[] data;
    private final Memory dataRes;
    private final int offset;
    private final int descrAddr;

    /**
     * Create a new instance
     *
     * @param rm        The resource manager
     * @param size      The size of the descriptor in bytes
     * @param alignment The alignment of the descriptor in bytes
     */
    protected AbstractStructure(MemoryManager rm, int size, int alignment) {
        this.data = new byte[size + alignment];
        this.dataRes = null;//rm.asMemoryResource(data);
        //Address ptr = dataRes.getAddress();
        int offset = 0;
        /*while (((offset + ptr.toInt()) & (alignment - 1)) != 0) {
            offset++;
        }*/
        this.offset = offset;
        this.descrAddr = 0;//dataRes.getAddress().toInt() + offset;
    }

    /**
     * Get a 32-bit integer at the given offset out of the descriptor
     *
     * @param ofs
     */
    protected final int getInt(int ofs) {
        return dataRes.get32(this.offset + ofs);
    }

    /**
     * Set a 32-bit integer at the given offset into the descriptor
     *
     * @param ofs
     * @param value
     */
    protected final void setInt(int ofs, int value) {
        dataRes.set32(this.offset + ofs, value);
    }

    /**
     * Gets the address (in physical memory) of the start of the descriptor.
     */
    public int getDescriptorAddress() {
        return descrAddr;
    }
}
