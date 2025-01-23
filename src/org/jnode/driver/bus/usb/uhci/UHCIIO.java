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

import jx.zero.InitialNaming;
import jx.zero.Ports;

/**
 * @author Ewout Prangsma (epr@users.sourceforge.net)
 */
public class UHCIIO implements UHCIConstants {

    private final Ports io;
    private final int base;

    /**
     * Create a new instance
     *
     * @param io
     */
    public UHCIIO() {
        this.io = (Ports)InitialNaming.getInitialNaming().lookup("Ports");
        this.base = 0;//io.getStartPort();
    }

    /**
     * Release all resources
     */
    public void release() {
        //io.release();
    }

    /**
     * Gets the command register
     */
    protected final int getCommand() {
        return io.inw_p(base + USBCMD);
    }

    /**
     * Are the given bits in the command register set?
     */
    protected final boolean getCommandBits(int mask) {
        return ((io.inw_p(base + USBCMD) & mask) == mask);
    }

    /**
     * Sets the command register
     */
    protected final void setCommand(int command) {
        io.outw_p(base + USBCMD, (short)command);
    }

    /**
     * Sets certain bits of the command register on/off
     */
    protected final void setCommandBits(int mask, boolean on) {
        int cmd = io.inw_p(base + USBCMD);
        if (on) {
            cmd |= mask;
        } else {
            cmd &= ~mask;
        }
        io.outw_p(base + USBCMD, (short)cmd);
    }

    /**
     * Gets the status register
     */
    protected final int getStatus() {
        return io.inw_p(base + USBSTS);
    }

    /**
     * Are the given bits in the status register set?
     */
    protected final boolean getStatusBits(int mask) {
        return ((io.inw_p(base + USBSTS) & mask) == mask);
    }

    /**
     * Clear the status register
     */
    protected final void clearStatus(int clearMask) {
        io.outw_p(base + USBSTS, (short)clearMask);
    }

    /**
     * Gets the interrupt enable register
     */
    protected final int getInterruptEnable() {
        return io.inw_p(base + USBINTR);
    }

    /**
     * Sets the interrupt enable register
     */
    protected final void setInterruptEnable(int mask) {
        io.outw_p(base + USBINTR, (short)mask);
    }

    /**
     * Gets the frame number register
     */
    protected final int getFrameNumber() {
        return io.inw_p(base + USBFRNUM);
    }

    /**
     * Sets the frame number register
     */
    protected final void setFrameNumber(int frnum) {
        io.outw_p(base + USBFRNUM, (short)frnum);
    }

    /**
     * Gets the frame list base address register
     */
    protected final int getFrameListBaseAddress() {
        return io.inl_p(base + USBFLBASEADD);
    }

    /**
     * Sets the frame list base address register
     */
    protected final void setFrameListBaseAddress(int baseAddress) {
        io.outl_p(base + USBFLBASEADD, baseAddress);
    }

    /**
     * Gets the start of frame (SOF) modify register
     */
    protected final int getStartOfFrame() {
        return io.inb_p(base + USBSOF);
    }

    /**
     * Sets the start of frame (SOF) modify register
     */
    protected final void setStartOfFrame(int sof) {
        io.outb_p(base + USBSOF, (byte)sof);
    }

    /**
     * Gets the port status and control register
     */
    protected final int getPortSC(int port) {
        return io.inw_p(base + USBPORTSC1 + (port << 1));
    }

    /**
     * Are the given bits in the port status and control register set?
     */
    protected final boolean getPortSCBits(int port, int mask) {
        return ((io.inw_p(base + USBPORTSC1 + (port << 1)) & mask) == mask);
    }

    /**
     * Sets certain bits of the port1 status and control register on/off
     */
    protected final void setPortSCBits(int port, int mask, boolean on) {
        int sc = io.inw_p(base + USBPORTSC1 + (port << 1));
        sc &= 0xFFF5;
        if (on) {
            sc |= mask;
        } else {
            sc &= ~mask;
        }
        io.outw_p(base + USBPORTSC1 + (port << 1), (short)sc);
    }

    /**
     * Sets all bits of the port1 status and control register
     */
    protected final void setPortSC(int port, int value) {
        io.outw_p(base + USBPORTSC1 + (port << 1), (short)value);
    }
}
