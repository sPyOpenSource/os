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

import jx.zero.Ports;

/**
 * @author Chris
 */
public class WordIOAccess extends IOAccess implements LanceConstants {

    public WordIOAccess(Ports io, int iobase) {
        super(io, iobase);
    }

    public String getType() {
        return "Word";
    }

    public void reset() {
        // Read triggers a reset
        io.inw(iobase + WIO_RESET);
    }

    public int getCSR(int csrnr) {
        io.outw(iobase + WIO_RAP, (short) csrnr);
        return io.inw(iobase + WIO_RDP);
    }

    public void setCSR(int csrnr, int value) {
        io.outw(iobase + WIO_RAP, (short) csrnr);
        io.outw(iobase + WIO_RDP, (short) value);
    }

    public int getBCR(int bcrnr) {
        io.outw(iobase + WIO_RAP, (short) bcrnr);
        return io.inw(iobase + WIO_BDP);
    }

    public void setBCR(int bcrnr, int value) {
        io.outw(iobase + WIO_RAP, (short) bcrnr);
        io.outw(iobase + WIO_BDP, (short) value);
    }
}
