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
 * @author Chris Cole
 */
public class DWordIOAccess extends IOAccess implements LanceConstants {

    public DWordIOAccess(Ports io, int iobase) {
        super(io, iobase);
    }

    @Override
    public String getType() {
        return "DWord";
    }

    @Override
    public void reset() {
        // Read triggers a reset
        io.inl(iobase + DWIO_RESET);
    }

    @Override
    public int getCSR(int csrnr) {
        io.outl(iobase + DWIO_RAP, csrnr);
        return io.inl(iobase + DWIO_RDP);
    }

    @Override
    public void setCSR(int csrnr, int value) {
        io.outl(iobase + DWIO_RAP, csrnr);
        io.outl(iobase + DWIO_RDP, value);
    }

    @Override
    public int getBCR(int bcrnr) {
        io.outl(iobase + DWIO_RAP, bcrnr);
        return io.inl(iobase + DWIO_BDP);
    }

    @Override
    public void setBCR(int bcrnr, int value) {
        io.outl(iobase + DWIO_RAP, bcrnr);
        io.outl(iobase + DWIO_BDP, value);
    }
}
