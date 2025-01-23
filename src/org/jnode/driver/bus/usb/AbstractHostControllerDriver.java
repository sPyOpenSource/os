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
 
package org.jnode.driver.bus.usb;

import javax.naming.NameNotFoundException;
import jx.devices.Device;
//import org.jnode.driver.DeviceAlreadyRegisteredException;
//import org.jnode.driver.DeviceManager;
//import org.jnode.driver.Driver;
//import org.jnode.driver.DriverException;

/**
 * Abstract Host Controller driver.
 *
 * @author Ewout Prangsma (epr@users.sourceforge.net)
 */
public abstract class AbstractHostControllerDriver {

    /**
     * My logger
     */
    //private static final Logger log = Logger.getLogger(AbstractHostControllerDriver.class);
    /**
     * The root hub monitor
     */
    private USBHubMonitor rootHubMonitor;

    /**
     * @see org.jnode.driver.Driver#startDevice()
     */
    protected final void startDevice() throws Exception {
        final Device device = null;//getDevice();
        //final DeviceManager dm;
        try {
            //dm = InitialNaming.lookup(DeviceManager.NAME);
            //dm.rename(device, getDevicePrefix(), true);
        } catch (Exception ex) {
            //log.error("Cannot rename device", ex);
            throw new Exception("Cannot rename device", ex);
        }
        claimResources();
        //device.registerAPI(USBHostControllerAPI.class, getAPIImplementation());
        // Create & start a monitor
        final USBHubAPI hubApi = getAPIImplementation().getRootHUB();
        this.rootHubMonitor = new USBHubMonitor(device, hubApi);
        rootHubMonitor.startMonitor();
    }

    /**
     * @see org.jnode.driver.Driver#stopDevice()
     */
    protected final void stopDevice() throws Exception {
        if (this.rootHubMonitor != null) {
            rootHubMonitor.stopMonitor();
            this.rootHubMonitor = null;
        }
        //getDevice().unregisterAPI(USBHostControllerAPI.class);
        releaseResources();
    }

    /**
     * Gets the prefix for the device name
     */
    protected abstract String getDevicePrefix();

    /**
     * Claim all resources required to start the device
     */
    protected abstract void claimResources() throws Exception;

    /**
     * Release all claimed resources
     */
    protected abstract void releaseResources();

    /**
     * Gets the API implementation.
     */
    protected abstract USBHostControllerAPI getAPIImplementation();
}
