package jx.devices.pci;

import jx.devices.Device;
import jx.devices.DeviceConfiguration;
import jx.devices.DeviceConfigurationTemplate;
import jx.zero.Debug;

public class PCIDeviceImpl implements PCIDevice {
   private final PCIAddress pciaddress;
   private final PCIAccess  pcibus;
   
   private boolean haveCapabilities = false;
   private PCICapability capabilities[] = null;
   
   // only the hostbridge should generate PCIDevices
   PCIDeviceImpl(PCIAccess bus, PCIAddress address){
      pcibus     = bus;
      pciaddress = address;
      
      setupCapabilities();
   }
   
   @Override
   public PCIAddress getAddress(){
      return pciaddress;
   }
   
   /********************************************************************/
   /* basic register access                                            */
   /********************************************************************/
   
   /**
    * Read a DWord from the Configuration Space of the PCI device.
    * @param reg Number of the desired DWORD register. (NOT the address!)
    * @return Current value of the register.
    */
   @Override
   public int readConfig(int reg){
      return pcibus.readDeviceConfig(pciaddress, reg);
   }
   
   /** 
    * Write a DWord into the Configuration Space of the PCI device. 
    * 
    * Some registers or parts of them may be readonly, but this method does
    * not try to avoid writing to readonly registers.
    * 
    * @param reg Number of the desired DWORD register. (NOT the address!)
    * @param value New value which is written into the register.
    */
   @Override
   public void writeConfig(int reg, int value){
      pcibus.writeDeviceConfig(pciaddress, reg, value);
   }
   
   /**
    * Read only a part of a DWord of the Configuration Space.
    * 
     * @param reg
     * @param mask
     * @param shift
    * @return (value & mask) >> shift
    * 
    */
   @Override
   public int readPackedConfig(int reg, int mask, int shift){
      return (readConfig(reg) & mask) >> shift;
   }
   public int readPackedConfig(int reg, int mask){
      return readConfig(reg) & mask;
   }
   
   /**
    * Write a Configuration Register partially.
    * 
    * Before writing a complete DWord into the register, the old value is
    * read out and the bits not covered by the mask are merged into the
    * given value. The merged value is written into the register.
    * 
     * @param reg
     * @param mask
     * @param shift
     * @param value
    */
   public void writePackedConfig(int reg, int mask, int shift, int value){
      int oldval = readConfig(reg) & ~mask;
      writeConfig(reg, oldval | ((value << shift) & mask) );
   }
   @Override
   public void writePackedConfig(int reg, int mask, int value){
      int oldval = readConfig(reg) & ~mask;
      writeConfig(reg, oldval | (value & mask) );
   }
   
   /********************************************************************/
   /* PCI Header Access                                                */
   /********************************************************************/
   
   /* 
    * This layer does nothing you can't do with direct calls to
    * readConfig/writeConfig, but it is much easier to use this methods.
    */
   
   /***************************************************/
   /* mandatory header registers for header type zero */
   /**
     * @return *************************************************/
   
   // register 0
   @Override
   public short getVendorID(){
      return (short)readPackedConfig(PCI.REG_DEVVEND, PCI.VENDOR_MASK, PCI.VENDOR_SHIFT);
   }
   @Override
   public short getDeviceID(){
      return (short)readPackedConfig(PCI.REG_DEVVEND, PCI.DEVICE_MASK, PCI.DEVICE_SHIFT);
   }
   
   // register 11
   public short getSubsystemVendorID(){
      return (short)readPackedConfig(PCI.REG_SUBDEVVEND, PCI.SUBVENDOR_MASK, PCI.SUBVENDOR_SHIFT);
   }
   public short getSubsystemID(){
      return (short)readPackedConfig(PCI.REG_SUBDEVVEND, PCI.SUBSYSTEM_MASK, PCI.SUBSYSTEM_SHIFT);
   }
   
   // register 1
   @Override
   public short getCommand(){
      return (short)readPackedConfig(PCI.REG_STATCMD, PCI.COMMAND_MASK, PCI.COMMAND_SHIFT);
   }
   @Override
   public void setCommand(short cmd){
      writePackedConfig(PCI.REG_STATCMD, PCI.COMMAND_MASK, PCI.COMMAND_SHIFT, cmd);
   }
   public short getStatus(){
      return (short)readPackedConfig(PCI.REG_STATCMD, PCI.STATUS_MASK, PCI.STATUS_SHIFT);
   }
   public void clearStatus(int mask){
      writePackedConfig(PCI.REG_STATCMD, PCI.STATUS_MASK, PCI.STATUS_SHIFT, mask);
   }
   
   // register 2
   @Override
   public byte getRevisionID(){
      return (byte)readPackedConfig(PCI.REG_CLASSREV, PCI.REVISION_MASK, PCI.REVISION_SHIFT);
   }
   
   @Override
   public int getClassCode(){
      return readPackedConfig(PCI.REG_CLASSREV, PCI.CLASSCODE_MASK, PCI.CLASSCODE_SHIFT);
   }
   
   // register 3
   @Override
   public byte getHeaderType(){
      return (byte)readPackedConfig(PCI.REG_BHLC, PCI.HEADERTYPE_MASK, PCI.HEADERTYPE_SHIFT);
   }
   
   /***************************************************/
   /* optional registers for header type zero         */
   /**
     * @return *************************************************/
   
   // registers 3
   @Override
   public byte getCacheLineSize(){
      return (byte)readPackedConfig(PCI.REG_BHLC, PCI.CACHELINE_MASK, PCI.CACHELINE_SHIFT);
   }
   public void setCachLineSize(byte value){
      writePackedConfig(PCI.REG_BHLC, PCI.CACHELINE_MASK, PCI.CACHELINE_SHIFT, value);
   }
   @Override
   public int getLatencyTimer(){
      return readPackedConfig(PCI.REG_BHLC, PCI.LATENCYTIMER_MASK, PCI.LATENCYTIMER_SHIFT);
   }
   @Override
   public void setLatencyTimer(byte clocks){
      writePackedConfig(PCI.REG_BHLC, PCI.LATENCYTIMER_MASK, PCI.LATENCYTIMER_SHIFT, clocks);
   }
   public byte getBIST(){
      return (byte)readPackedConfig(PCI.REG_BHLC, PCI.BIST_MASK, PCI.BIST_SHIFT);
   }
   public void setBIST(byte val){
      writePackedConfig(PCI.REG_BHLC, PCI.BIST_MASK, PCI.BIST_SHIFT, val);
   }
   
   // register 4 - 9
   @Override
   public int getBaseAddress(int no){
      //Debug.assert(no >= 0 && no <= 5, "base address index out of range: "+no);
      return readConfig(PCI.REG_BASEADDRESS_0 + no);
   }
   @Override
   public void setBaseAddress(int no, int val){
      //Debug.assert(no >= 0 && no <= 5, "base address index out of range: "+no);
      writeConfig(PCI.REG_BASEADDRESS_0+no, val);
   }
   
   // register 12
   public int getExpansionROMAddress(){
      return readConfig(PCI.REG_EXPANSIONROM);
   }
   public void setExpansionROMAddress(int val){
      writeConfig(PCI.REG_EXPANSIONROM, val);
   }
   
   // register 15
   public int getMaxLatency(){
      return readPackedConfig(PCI.REG_LGII, PCI.MAXLATENCY_MASK, PCI.MAXLATENCY_SHIFT);
   }
   public int getMinGNT(){
      return readPackedConfig(PCI.REG_LGII, PCI.MINGNT_MASK, PCI.MINGNT_SHIFT);
   }
   public byte getInterruptPin(){
      return (byte)readPackedConfig(PCI.REG_LGII, PCI.INTERRUPTPIN_MASK, PCI.INTERRUPTPIN_SHIFT);
   }
   @Override
   public byte getInterruptLine(){
      return (byte)readPackedConfig(PCI.REG_LGII, PCI.INTERRUPTLINE_MASK, PCI.INTERRUPTLINE_SHIFT);
   }
   @Override
   public void setInterruptLine(byte val){
      writePackedConfig(PCI.REG_LGII, PCI.INTERRUPTLINE_MASK, PCI.INTERRUPTLINE_SHIFT, val);
   }
   
   
   /********************************************************************/
   /* Capability Support                                               */
   /********************************************************************/
   
   private void setupCapabilities(){
      if( (getStatus() & PCI.STATUS_CAPABILITY) == 0 )   /* no capability support */
	return;
      
      int capreg = readPackedConfig(PCI.REG_CAP, PCI.CAP_MASK, PCI.CAP_SHIFT)/4;
      if( capreg == 0 )	{	
	  Debug.out.println("capability bit set, but no capability list (uhh??)");
	  return;
	  //	throw new Error("capability list missing");
      }

      haveCapabilities = true;
      
      int numCaps = countCapabilities(capreg);
      capabilities = new PCICapability[numCaps];
      
      int capentry, capid, capnext;
      for(int i = 0; i < numCaps; ++i){
	 capentry = readConfig(capreg);
	 capid = (capentry & PCICap.CAP_ID_MASK);
	 capnext = (capentry & PCICap.CAP_NEXT_MASK) >> PCICap.CAP_NEXT_SHIFT;
	 
	 capabilities[i] = PCICapability.createCapability(this, capreg);
	 capreg = capnext / 4;
      }
   }
   
   private int countCapabilities(int reg){
      int count = 0;
      while( reg != 0 ){
	 ++count;
	 reg = readPackedConfig(reg, PCICap.CAP_NEXT_MASK, PCICap.CAP_NEXT_SHIFT)/4;
      }
      return count;
   }
   
   public boolean haveCapabilities(){
      return haveCapabilities;
   }
   
   public PCICapability[] getCapabilities(){
      if( haveCapabilities )
	return capabilities;
      throw new Error("no capabilities");
   }
   
   public PCICapability getCapability(byte capID){
      if( !haveCapabilities )
	return null;
      for(int i=0; i<capabilities.length; ++i){
	 if( capabilities[i].getID() ==  capID )
	   return capabilities[i];
      }
      return null;
   }
   
   /**
     * @return ******************************************************************/
   
   @Override
   public String toString(){
      return pciaddress.toString() + ": 0x" + Integer.toHexString(readConfig(PCI.REG_DEVVEND));
   }


   /***********  additions
     * @return  **************************************************/
   @Override
    public boolean busmasterCapable() { return false; }
   @Override
    public boolean enforceBusmaster() { return false; }
   @Override
    public int readIRQLine() {return 0;}

    @Override
    public Device getChild(int index) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public DeviceConfigurationTemplate[] getSupportedConfigurations() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void open(DeviceConfiguration conf) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void close() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
