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

//import java.util.logging.Level;
//import java.util.logging.Logger;
import jx.buffer.separator.NonBlockingMemoryConsumer;
import jx.devices.DeviceConfiguration;
import jx.devices.DeviceConfigurationTemplate;
import jx.devices.net.NetworkDevice;
import jx.devices.pci.PCIDevice;
import jx.zero.IRQ;
import jx.zero.Memory;
import jx.zero.MemoryManager;
import jx.zero.Ports;

/**
 * @author epr
 */
public class LanceDriver implements NetworkDevice {

    /*public LanceDriver(ConfigurationElement config) {
        this(new LanceFlags(config));
    }*/
    private LanceCore abstractDeviceCore;
    public static final int ETH_DATA_LEN   = 1500;  /* Max. octets in payload */

    public LanceDriver(PCIDevice device, LanceFlags flags, IRQ irq, Ports ports, MemoryManager rm) {
        //this.flags = flags;
        abstractDeviceCore = newCore(device, flags, irq, ports, rm);
    }

    /**
     * Create a new LanceCore instance
     * @param device
     * @param flags
     * @param irq
     * @param ports
     * @return 
     */
    protected LanceCore newCore(PCIDevice device, LanceFlags flags, IRQ irq, Ports ports, MemoryManager rm) {
        return new LanceCore(this, device, flags, irq, ports, rm);
    }

    @Override
    public void setReceiveMode(int mode) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Memory transmit(Memory buf) {
        try {
            abstractDeviceCore.transmit(buf);
        } catch (InterruptedException ex) {
            //Logger.getLogger(LanceDriver.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Memory transmit1(Memory buf, int offset, int size) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public byte[] getMACAddress() {
        return abstractDeviceCore.getHwAddress().get_Addr();
    }

    @Override
    public int getMTU() {
        return ETH_DATA_LEN;
    }

    @Override
    public boolean registerNonBlockingConsumer(NonBlockingMemoryConsumer consumer) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DeviceConfigurationTemplate[] getSupportedConfigurations() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void open(DeviceConfiguration conf) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void close() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    void onReceive(Memory skbuf) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
