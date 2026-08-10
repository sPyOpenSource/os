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
import org.jnode.driver.bus.usb.USBConstants;
import org.jnode.driver.bus.usb.USBEndPoint;
import org.jnode.driver.bus.usb.USBPacket;
import org.jnode.driver.bus.usb.spi.AbstractUSBDataRequest;

/**
 * @author Ewout Prangsma (epr@users.sourceforge.net)
 */
public class UHCIDataRequest extends AbstractUSBDataRequest implements UHCIRequest, USBConstants {

    /**
     * The first TD of this request
     */
    private TransferDescriptor firstTD;

    /**
     * Initialize this instance.
     *
     * @param dataPacket
     */
    public UHCIDataRequest(USBPacket dataPacket) {
        super(dataPacket);
    }

    @Override
    public void createTDs(UHCIPipe pipe) {
        System.out.println("i'm here");
        final USBPacket dataPacket = getDataPacket();
        System.out.println("DEBUG createTDs: dataPacket=" + dataPacket);
        if (dataPacket == null) {
            System.out.println("ERROR: dataPacket is null");
            return;
        }
        int offset = 0;
        int length = dataPacket.getSize();
        System.out.println("DEBUG createTDs: length=" + length + ", offset=" + offset);
        final USBEndPoint ep = pipe.getEndPoint();
        System.out.println("DEBUG createTDs: ep=" + ep);
        if (ep == null) {
            System.out.println("ERROR: pipe.getEndPoint() returned null");
            return;
        }
        System.out.println("datapid");
        final int dataPid = (ep.getDescriptor().isDirIn() ? USB_PID_IN : USB_PID_OUT);
        System.out.println("DEBUG createTDs: dataPid=0x" + Integer.toHexString(dataPid));
        final int maxPacketSize = pipe.getMaxPacketSize();
        System.out.println("DEBUG createTDs: maxPacketSize=" + maxPacketSize);
        System.out.println("DEBUG createTDs: pipe.rm=" + pipe.getMemoryManager());
        System.out.println("DEBUG createTDs: pipe.device=" + pipe.device);
        Memory dataBuf = dataPacket.getData();
        System.out.println("DEBUG createTDs: dataBuffer=" + dataBuf);
        if (dataBuf != null) {
            System.out.println("DEBUG createTDs: dataBuffer size=" + dataBuf.size() + " start=" + dataBuf.getStartAddress());
        }
        TransferDescriptor firstTD = null;
        System.out.println("here");
        while (length > 0) {
            final int curlen = Math.min(length, maxPacketSize);
            final TransferDescriptor dataTD;
            final boolean ioc = (curlen == length);
            System.out.println("DEBUG createTDs: creating TD curlen=" + curlen + " offset=" + offset + " ioc=" + ioc);
            dataTD = pipe.createTD(dataPid, ep.getDataToggle(), dataPacket.getData(), offset, curlen, ioc);
            System.out.println("DEBUG createTDs: TD created=" + dataTD);
            if (firstTD == null) {
                firstTD = dataTD;
            } else {
                firstTD.append(dataTD, false);
            }
            ep.toggle();
            length -= curlen;
            offset += curlen;
        }
        this.firstTD = firstTD;
    }

    /**
     * @see org.jnode.driver.bus.usb.uhci.UHCIRequest#getFirstTD()
     */
    public final TransferDescriptor getFirstTD() {
        return firstTD;
    }
}
