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

import jx.zero.Memory;

/**
 * @author Chris Cole
 */
public class RxDescriptor extends Descriptor {
    public static final int STATUS_FRAM = 0x2000;
    public static final int STATUS_OFLO = 0x1000;
    public static final int STATUS_CRC = 0x0800;
    public static final int STATUS_BUFF = 0x0400;

    public RxDescriptor(Memory mem, int offset, int dataBufferOffset) {
        super(mem, offset, dataBufferOffset);

        setOwnerSelf(false);
    }

    public Memory getPacket() {
        // setOwnerSelf()
        return null;//new Memory();
    }

    public void clearStatus() {
        mem.set16(offset + STATUS, (short) STATUS_OWN);
    }

    public short getMessageByteCount() {
        return mem.get16(offset + BCNT);
    }

    public byte[] getDataBuffer() {
        /*byte[] buf = new byte[getMessageByteCount()];
        //mem.getBytes(dataBufferOffset, buf, 0, buf.length);
        for(int i = 0; i < buf.length; i++){
            buf[i] = mem.get8(dataBufferOffset + i);
        }
        return buf;*/
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
