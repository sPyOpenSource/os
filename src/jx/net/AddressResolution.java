package jx.net;

public interface AddressResolution {
    public boolean register(Object o);
    public void notifyAddressChange(Object o);

    /** map fromAddress to target address
     * @param fromAddress
     * @return 
     * @throws jx.net.UnknownAddressException */
    public byte[] lookup(byte[] fromAddress) throws UnknownAddressException;
}
