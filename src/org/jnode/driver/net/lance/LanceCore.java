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

import jx.devices.pci.PCIDevice;
import jx.zero.Debug;
import jx.zero.FirstLevelIrqHandler;
import jx.zero.IRQ;
import jx.zero.InitialNaming;
import jx.zero.Memory;
import jx.zero.MemoryManager;
import jx.zero.Ports;
import metaxa.os.devices.net.EthernetAdress;
import metaxa.os.devices.net.WrongEthernetAdressFormat;
//import org.jnode.driver.net.NetworkException;
//import org.jnode.util.TimeoutException;

/**
 * This is the DeviceCore for LANCE and PCnet 32 compatable ethernet PCI drivers.
 * <p/>
 * The current implementation was specificaly designed for the AMD PCnet-PCI II
 * Ethernet Controller (Am79C970A), but should work for other AMD PCnet PCI devices.
 * The driver is based on information in the following specification from AMD.
 * http://www.amd.com/files/connectivitysolutions/networking/archivednetworking/19436.pdf
 * <p/>
 * Specificaly the following assumptions were made:
 * - Device supports Software Style 2 (PCnet-PCI) which defines the layout of the initialaztion
 * block and the descriptor rings.
 * <p/>
 * Note: It should be easy to expand this driver to remove these assuptions.
 *
 * @author Chirs Cole
 */
public class LanceCore implements FirstLevelIrqHandler, LanceConstants {

    // This is the number of descriptors for the receive and transmit rings
    // Note: Valid numbers are 2^x where x is 0..8 (1, 2, 4, 8, 16, .., 512)
    private static final int RX_DESCRIPTOR_LENGTH = 4;
    private static final int TX_DESCRIPTOR_LENGTH = 4;
    /**
     * Device Driver
     */
    private final LanceDriver driver;
    
    /**
     * Start of IO address space
     */
    private final int iobase;
    
    /**
     * IO address space resource
     */
    private final Ports ioResource;
    private final IOAccess io;
    
    /**
     * IRQ resource
     */
    private final IRQ irq;
    
    /**
     * My ethernet address
     */
    private final EthernetAdress hwAddress;
    
    /**
     * Flags for the specific device found
     */
    private final LanceFlags flags;
    
    /**
     * Manager for receive and transmit rings as well as data buffers
     */
    private final BufferManager bufferManager;

    /**
     * Create a new instance and allocate all resources
     *
     * @param driver
     * @param device
     * @param flags
     * @param irq
     * @param ports
     * @param rm
     */
    public LanceCore(LanceDriver driver, PCIDevice device, LanceFlags flags, IRQ irq, Ports ports, MemoryManager rm) {
        this.driver = driver;
        this.flags = (LanceFlags) flags;

        //final PCIHeaderType0 config = device.getConfig().asHeaderType0();
        //final int irqnum = config.getInterruptLine();
        int irqnum = device.getInterruptLine();

        /*final PCIBaseAddress[] addrs = config.getBaseAddresses();
        if (addrs.length < 1) {
            throw new DriverException("Cannot find iobase: not base addresses");
        }
        if (!addrs[0].isIOSpace()) {
            throw new DriverException("Cannot find iobase: first address is not I/O");
        }*/

        // Get the start of the IO address space
        iobase = device.getBaseAddress(0) - 1;//addrs[0].getIOBase();
        //final int iolength = addrs[0].getSize();
        //log.debug("Found Lance IOBase: 0x" + NumberUtils.hex(iobase) + ", length: " + iolength);
        //MemoryManager rm;
        //try {
          //  rm = (MemoryManager) InitialNaming.getInitialNaming().lookup("MemoryManager");
        //} catch (NameNotFoundException ex) {
          //  throw new DriverException("Cannot find ResourceManager");
        //}
        this.irq = irq;
        this.irq.installFirstLevelHandler(irqnum, this);
    	this.irq.enableIRQ(irqnum);
        //try {
            ioResource = ports;
        //} catch (ResourceNotFreeException ex) {
            //this.irq.release();
        //    throw ex;
        //}
        // Determine the type of IO access (Word or DWord)
        io = getIOAccess();
        //log.debug("IO Access set to " + io.getType());
        // Set the flags based on the version of the device found
        setFlags();
        // Load the hw address
        this.hwAddress = loadHWAddress();
        /*log.info("Found " + this.flags.getChipName() + " at 0x" + NumberUtils.hex(iobase, 4) +
                " with MAC Address " + hwAddress);*/

        // Create rx & tx descriptor rings, initdata and databuffers
        this.bufferManager =
                new BufferManager(RX_DESCRIPTOR_LENGTH, TX_DESCRIPTOR_LENGTH,
                        CSR15_DRX | CSR15_DTX, hwAddress, 0, rm);

        // Enable device to become a bus master on the PCI bus.
        //config.setCommand(config.getCommand() | PCIConstants.PCI_COMMAND_MASTER);
        Debug.out.println("set");
        device.setCommand((short) (0x4 | device.getCommand()));
        Debug.out.println("end");
    }

    private IOAccess getIOAccess() {
        // reset
        ioResource.inw(iobase + WIO_RESET);
        ioResource.outw(iobase + WIO_RAP, (short)0);
        if (ioResource.inw(iobase + WIO_RDP) == 4) {
            ioResource.outw(iobase + WIO_RAP, (short)88);
            if (ioResource.inw(iobase + WIO_RAP) == 88) {
                return new WordIOAccess(ioResource, iobase);
            }
        }

        ioResource.inl(iobase + DWIO_RESET);
        ioResource.outl(iobase + DWIO_RAP, 0);
        if (ioResource.inl(iobase + DWIO_RDP) == 4) {
            ioResource.outl(iobase + DWIO_RAP, 88);
            if ((ioResource.inl(iobase + DWIO_RAP) & 0xFFFF) == 88) {
                return new DWordIOAccess(ioResource, iobase);
            }
        }
        return null;
    }

    /**
     * Initialize this device.
     */
    public void initialize() {
        // reset the chip
        io.reset();

        // Set the Software Style to mode 2 (PCnet-PCI)
        // Note: this may not be compatable with older lance controllers (non PCnet)
        io.setBCR(20, 2);

        // TODO the device should be setup based on the flags for the chip version
        // Auto select port
        io.setBCR(2, BCR2_ASEL);
        // Enable full duplex
        io.setBCR(9, BCR9_FDEN);
        io.setCSR(4, CSR4_DMAPLUS | CSR4_APAD_XMT);
        io.setCSR(5, CSR5_LTINTEN | CSR5_SINTE | CSR5_SLPINTE | CSR5_EXDINTE | CSR5_MPINTE);

        // Set the address of the Initialization Block
        final int iaddr = bufferManager.getInitDataAddressAs32Bit();
        io.setCSR(1, iaddr & 0xFFFF);
        io.setCSR(2, (iaddr >> 16) & 0xFFFF);

        // Initialize the device with the Initialization Block and enable interrupts
        io.setCSR(0, CSR0_INIT | CSR0_IENA);
    }

    /**
     * Disable this device
     */
    public void disable() {
        io.reset();
        io.setCSR(0, CSR0_STOP);
    }

    /**
     * Release all resources
     */
    public void release() {
        //ioResource.release();
        //irq.release();
    }

    /**
     * Gets the hardware address of this card.
     * @return 
     */
    public EthernetAdress getHwAddress() {
        return hwAddress;
    }

    /**
     * Read the hardware address
     */
    private EthernetAdress loadHWAddress() {
        final byte[] addr = new byte[6];
        for (int i = 0; i < addr.length; i++) {
            addr[i] = (byte) ioResource.inb(iobase + R_ETH_ADDR_OFFSET + i);
        }
        try {
            return new EthernetAdress(addr);
        } catch (WrongEthernetAdressFormat ex) {
            Debug.out.println(ex.getMessage());
            //Logger.getLogger(LanceCore.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    private void setFlags() {
        int chipVersion = io.getCSR(88) | (io.getCSR(89) << 16);
        chipVersion = (chipVersion >> 12) & 0xffff;

        flags.setForVersion(chipVersion);
    }

    /**
     * Transmit the given buffer
     *
     * @param buf
     * @param destination
     * @param timeout
     * @throws InterruptedException
     */
    public synchronized void transmit(Memory buf)
        throws InterruptedException {
        // Set the source address
        hwAddress.writeTo(buf, 6);

        // debug dump for investigating VWWare 3 network problems
        // dumpDebugInfo();

        // ask buffer manager to send out the data
        bufferManager.transmit(buf);

        // force the device to poll current transmit descriptor for new data
        io.setCSR(0, io.getCSR(0) | CSR0_TDMD);
    }

    /**
     * @param irq
     * @see org.jnode.system.resource.IRQHandler#handleInterrupt(int)
     */
    public void handleInterrupt(int irq) {
        while ((io.getCSR(0) & CSR0_INTR) != 0) {
            final int csr0 = io.getCSR(0);
            final int csr4 = io.getCSR(4);
            final int csr5 = io.getCSR(5);

            io.setCSR(0, csr0);
            io.setCSR(4, csr4);
            io.setCSR(5, csr5);

            // check if interrupt is due to Initialization Done
            if ((csr0 & CSR0_IDON) != 0) {
                //log.info(flags.getChipName() + " Initialization Complete");

                // Now enable RX/TX
                io.setCSR(15, 0);

                // assert the Start and clear Initialization Done (IDON) flag
                // Note: there are reported errors due to setting IDON here but I have not seen any
                io.setCSR(0, CSR0_STRT | CSR0_IENA | CSR0_IDON);
            }

            // check if interrupt is due to Transmition Interrupt
            if ((csr0 & CSR0_TINT) != 0) {
                //log.debug("Transmition Interrupt");
            }

            // check if interrupt is due to Receive Interrupt
            if ((csr0 & CSR0_RINT) != 0) {
                //log.debug("Receive Interrupt");
                rxProcess();
            }

            // check if interrupt is due an error
            if ((csr0 & CSR0_ERR) != 0) {
                //log.debug("Error Interrupt");

                // check if interrupt is due to Memory Error
                if ((csr0 & CSR0_MERR) != 0) {
                    //log.debug("Memory Error");
                }

                // check if interrupt is due to Missed Frame
                if ((csr0 & CSR0_MISS) != 0) {
                    //log.debug("Missed Frame");
                }

                // check if interrupt is due to Collision Error
                if ((csr0 & CSR0_CERR) != 0) {
                    //log.debug("Collision Error");
                }

                // check if interrupt is due to a Bable transmitter time-out
                if ((csr0 & CSR0_BABL) != 0) {
                    //log.debug("Bable transmitter time-out");
                }
            }

            // check if interrupt is due to a Missed Frame Counter Overflow
            if ((csr4 & CSR4_MFCO) == CSR4_MFCO) {
                //log.debug("Missed Frame Counter Overflow");
            }

            // check if interrupt is due to a User Interrupt
            if ((csr4 & CSR4_UINT) == CSR4_UINT) {
                //log.debug("User Interrupt");
            }

            // check if interrupt is due to a Receive Collision Counter Overflow
            if ((csr4 & CSR4_RCVCCO) == CSR4_RCVCCO) {
                //log.debug("Receive Collision Counter Overflow");
            }

            // check if interrupt is due to a Transmit Start
            if ((csr4 & CSR4_TXSTRT) == CSR4_TXSTRT) {
                //log.debug("Transmit Start");
            }

            // check if interrupt is due to a Jabber Error
            if ((csr4 & CSR4_JAB) == CSR4_JAB) {
               // log.debug("Jabber Error");
            }

            // check if interrupt is due to a Jabber Error
            if ((csr4 & CSR4_JAB) == CSR4_JAB) {
               // log.debug("Jabber Error");
            }

            // check if interrupt is due to a System Interrupt
            if ((csr5 & CSR5_SINT) == CSR5_SINT) {
               // log.debug("System Interrupt");
            }

            // check if interrupt is due to a Sleep Interrupt
            if ((csr5 & CSR5_SLPINT) == CSR5_SLPINT) {
                //log.debug("Sleep Interrupt");
            }

            // check if interrupt is due to a Excessive Deferral Interrupt
            if ((csr5 & CSR5_EXDINT) == CSR5_EXDINT) {
                //log.debug("Excessive Deferral Interrupt");
            }

            // check if interrupt is due to a Magic Packet Interrupt
            if ((csr5 & CSR5_MPINT) == CSR5_MPINT) {
                //log.debug("Magic Packet Interrupt");
            }

        }
    }

    private void rxProcess() {
        Memory skbuf;

        while ((skbuf = bufferManager.getPacket()) != null) {
            //try {
                if (skbuf != null)
                    driver.onReceive(skbuf);
            //} catch (NetworkException e) {
                // FIXME
              //  e.printStackTrace();
            //} finally {
                // FIXME
            //}
        }
    }

    /*private Ports claimPorts(final MemoryManager rm, final ResourceOwner owner,
            final int low, final int length) throws ResourceNotFreeException, DriverException {
        try {
            return AccessControllerUtils.doPrivileged(new PrivilegedExceptionAction<IOResource>() {
                public Ports run() throws ResourceNotFreeException {
                    return rm.claimIOResource(owner, low, length);
                }
            });
        } catch (Exception ex) {
            throw new DriverException("Unknown exception", ex);
        }

    }*/

    /*final void dumpDebugInfo() {
        //log.debug("Debug Dump");
        //log.debug("CSR0 = " + io.getCSR(0));

        // stop the device so we can read all registers
        io.setCSR(0, CSR0_STOP);

        bufferManager.dumpData(log);

        int validVMWareLanceRegs[] = {
            0, 1, 2, 3, 4, 5, 8, 9, 10, 11, 12, 13, 14, 15, 24, 25, 30, 31, 58, 76, 77, 80,
            82, 88, 89, 112, 124};

        for (int validVMWareLanceReg : validVMWareLanceRegs) {
            int csr_val = io.getCSR(validVMWareLanceReg);
            log.debug("CSR" + validVMWareLanceReg + " : " + NumberUtils.hex(csr_val, 4));
        }

        // try to start again, not sure if this works?
        io.setCSR(0, CSR0_STRT);
    }*/

    @Override
    public void interrupt() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
