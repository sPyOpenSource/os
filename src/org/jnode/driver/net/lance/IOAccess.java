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
public abstract class IOAccess {
    protected Ports io;
    protected int iobase;

    public IOAccess(Ports io, int iobase) {
        this.io = io;
        this.iobase = iobase;
    }

    public abstract String getType();

    /**
     * Reset the device.
     */
    public abstract void reset();

    /**
     * Gets the contents of a Control and Status Register.
     *
     * @param csrnr
     * @return 
     */
    public abstract int getCSR(int csrnr);

    /**
     * Sets the contents of a Control and Status Register.
     *
     * @param csrnr
     * @param value
     */
    public abstract void setCSR(int csrnr, int value);

    /**
     * Gets the contents of a Bus Configuration Register.
     *
     * @param bcrnr
     * @return 
     */
    public abstract int getBCR(int bcrnr);

    /**
     * Sets the contents of a Bus Configuration Register.
     *
     * @param bcrnr
     * @param value
     */
    public abstract void setBCR(int bcrnr, int value);
}
