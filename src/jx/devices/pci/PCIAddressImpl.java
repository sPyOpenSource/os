package jx.devices.pci;

public class PCIAddressImpl implements PCIAddress {
   int bus;
   int device;
   int function;
   
   public PCIAddressImpl(int bus, int device, int function){
      this.bus = bus;
      this.device = device;
      this.function = function;
   }
   
   public PCIAddress getSubfunction(int subfunction){
      return new PCIAddressImpl(bus, device, subfunction);
   }
   
   public boolean equals(PCIAddress other){
      return (this == other) ||
	((this.bus == other.getBus()) &&
	 (this.device == other.getDevice()) &&
	 (this.function == other.getFunction()));
   }
     
   @Override
   public String toString(){
      return "@PCI(" + bus + "," + device + "," + function + ")";
   }

    @Override
    public int getBus() {
        return bus;
    }

    @Override
    public int getDevice() {
        return device;
    }

    @Override
    public int getFunction() {
        return function;
    }
}
