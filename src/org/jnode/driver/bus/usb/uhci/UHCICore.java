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

import AI.AIZeroLogic;
import AI.Models.IRQHandler;
import java.security.PrivilegedExceptionAction;
import javax.naming.NameNotFoundException;
import jx.devices.pci.PCIAddress;
import jx.devices.pci.PCIDevice;
import jx.zero.FirstLevelIrqHandler;
import jx.zero.IRQ;
import jx.zero.InitialNaming;
import jx.zero.MemoryManager;
import jx.zero.Naming;
import jx.zero.timer.SleepManager;
import jx.zero.timer.TimerManager;

//import org.jnode.driver.DriverException;
import org.jnode.driver.bus.usb.USBBus;
import org.jnode.driver.bus.usb.USBControlPipe;
import org.jnode.driver.bus.usb.USBDevice;
import org.jnode.driver.bus.usb.USBEndPoint;
import org.jnode.driver.bus.usb.USBHostControllerAPI;
import org.jnode.driver.bus.usb.USBHubAPI;
import org.jnode.driver.bus.usb.USBPipe;

/*import org.jnode.system.resource.IOResource;
import org.jnode.system.resource.IRQHandler;
import org.jnode.system.resource.IRQResource;
import org.jnode.system.resource.ResourceNotFreeException;
import org.jnode.system.resource.ResourceOwner;
import org.jnode.util.AccessControllerUtils;*/
import org.jnode.util.NumberUtils;

/**
 * @author Ewout Prangsma (epr@users.sourceforge.net)
 */
public class UHCICore implements USBHostControllerAPI, UHCIConstants, FirstLevelIrqHandler {

    /**
     * My logger
     */
    //private static final Logger log = Logger.getLogger(UHCICore.class);
    /**
     * The pci device
     */
    private final PCIDevice device;
    /**
     * The io methods of the controller
     */
    private final UHCIIO io;
    /**
     * The IRQ resource
     */
    private final IRQ irq;
    /**
     * The resource manager
     */
    private final MemoryManager rm;
    /**
     * The active request list
     */
    private final UHCIPipeManager pipeMgr;
    /**
     * The Root HUB
     */
    private final UHCIRootHub rootHub;
    /**
     * The bus this HostController is providing
     */
    private final USBBus bus;
    SleepManager sleepManager;
    /**
     * Create and initialize a new instance
     *
     * @param device
     */
    public UHCICore(PCIDevice device) {
        this.device = device;
        sleepManager = new jx.timerpc.SleepManagerImpl();
        final PCIAddress baseAddr = device.getAddress();
        this.rm = (MemoryManager)InitialNaming.getInitialNaming().lookup("MemoryManager");
        final int ioBase = device.getBaseAddress(0) - 1;
        //final int ioSize = baseAddr.getSize();
        System.out.println("Found UHCI at 0x" + NumberUtils.hex(baseAddr.getDevice()));

        this.io = new UHCIIO();
        this.bus = new USBBus(device, this);
        this.rootHub = new UHCIRootHub(io, bus);
        final Schedule schedule = new Schedule(rm);
        this.pipeMgr = new UHCIPipeManager(rm, schedule);

        final int irqNr = device.getInterruptLine() & 0xF;
        // Workaround for some VIA chips
        device.setInterruptLine((byte)irqNr);
        this.irq = (IRQ)InitialNaming.getInitialNaming().lookup("IRQ");
        AIZeroLogic.createIRQ(irqNr, this);
        if(IRQHandler.first(irqNr)){
            this.irq.installFirstLevelHandler(irqNr, new IRQHandler(irqNr));
            this.irq.enableIRQ(irqNr);
            System.out.println("Using IRQ " + irqNr);
        }
        // Reset the HC
        resetHC();
System.out.println("reset");
        // Set the enabled interrupts
        io.setInterruptEnable(0x000F);
        // Set the framelist pointer
        io.setFrameListBaseAddress(schedule.getFrameList().getDescriptorAddress());
        // Go!
        setRun(true);
        System.out.println("OK");
    }

    /**
     * Release all resources
     */
    public void release() {
        // Disable both ports
        final int max = rootHub.getNumPorts();
        for (int i = 0; i < max; i++) {
            rootHub.setPortEnabled(i, false);
        }
        // Stop the HC
        setRun(false);
        //irq.release();
        io.release();
    }

    /**
     * Get the IO base address from the given PCI config
     *
     * @param cfg
     * @return @throws
     *         DriverException
     */
    private PCIAddress getBaseAddress() throws Exception {
        final PCIAddress[] addresses = null;//cfg.asHeaderType0().getBaseAddresses();
        for (int i = 0; i < addresses.length; i++) {
            final PCIAddress a = addresses[i];
            if (a != null) {
                //log.debug("Found address " + i + " (" + a + ")");
                return a;
            }
        }
        throw new Exception("No IO base address found");
    }

    /**
     * Create a default control pipe for a given device.
     *
     * @param device
     * @return The created pipe.
     */
    @Override
    public USBControlPipe createDefaultControlPipe(USBDevice device) {
        return pipeMgr.createDefaultControlPipe(device);
    }

    /**
     * Create a new pipe for a given endpoint.
     *
     * @param endPoint
     * @return The new pipe.
     */
    @Override
    public USBPipe createPipe(USBEndPoint endPoint) {
        return pipeMgr.createPipe(endPoint);
    }

    /**
     * @see org.jnode.system.resource.IRQHandler#handleInterrupt(int)
     */
    @Override
    public final void interrupt() {
        final int status = io.getStatus();
        if (status == 0) {
            //log.debug("UHCI IRQ, status == 0, so probably not for me");
            // This is a shared interrupt and apperently not for me
            return;
        } else {
            // Clear the status
            io.clearStatus(status);
        }

        /*if ((status & USBSTS_ERROR) != 0) {
              log.debug("UHCI interrupt due to error");
          }*/

        pipeMgr.handleInterrupt();
    }

    /**
     * Reset the Host Controller.
     */
    private void resetHC() {
        io.setCommand(USBCMD_HCRESET);
        //try {
            //Thread.sleep(50);
            sleepManager.mdelay(50);
        //} catch (InterruptedException ex) {
            // Ignore
        //}
        io.setCommand(0);
        //try {
            //Thread.sleep(10);
            sleepManager.mdelay(10);
        //} catch (InterruptedException ex) {
            // Ignore
        //}
        device.writeConfig(USBLEGSUP, USBLEGSUP_DEFAULT);
        rootHub.resetHub();
    }

    /**
     * Set the HC Run bit.
     *
     * @param run True to start running, false to stop
     */
    private void setRun(boolean run) {
        io.setCommandBits(USBCMD_MAXP | USBCMD_CF | USBCMD_RS, run);
    }

    /**
     * @return Returns the rootHub.
     */
    @Override
    public final USBHubAPI getRootHUB() {
        return this.rootHub;
    }

    /*private IOResource claimPorts(final MemoryManager rm, final ResourceOwner owner, final int low, final int length)
        throws ResourceNotFreeException, DriverException {
        try {
            return AccessControllerUtils.doPrivileged(new PrivilegedExceptionAction<IOResource>() {
                public IOResource run() throws ResourceNotFreeException {
                    return rm.claimIOResource(owner, low, length);
                }
            });
        } catch (ResourceNotFreeException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new DriverException("Unknown exception", ex);
        }
    }*/
}
