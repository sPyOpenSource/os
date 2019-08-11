package metaxa.os.devices.net;

import jx.zero.Debug;
import jx.zero.Memory;

/* Ethernet-Adresse mit Zugriffsfunktionen */

public class EthernetAdress {
    final static int ETH_ADDR_SIZE = 6; 

    private byte Addr[];
/**
     * Gets the type of this address. This type is used by (e.g.) ARP.
     */
    public int getType() {
        return 1; // For ethernet
    }
    public EthernetAdress(byte[] array) throws WrongEthernetAdressFormat {
	if (array.length != ETH_ADDR_SIZE) throw new WrongEthernetAdressFormat();
	Addr = new byte[ETH_ADDR_SIZE];
        System.arraycopy(array, 0, Addr, 0, 6);
    }

    /* 
     * gibt die Ethernet-Adresse als Byte-Array zurck, allerdings als Kopie des internen Arrays,
     * das nicht von auen gendert werden knnen soll
     */

    public byte[] get_Addr() {
	byte feld[] = new byte[ETH_ADDR_SIZE];//(byte[])Addr.clone();
        //Debug.out.println(print_Addr());
        /*for (int i = 0; i < Addr.length; i++){
            Debug.out.print(Addr[i]);
        }
        Debug.out.println();*/
        System.arraycopy(Addr, 0, feld, 0, 6);
	return feld;
    }
    
    public byte get(int i){
        return Addr[i];
    }
    /* 
     * erzeugt einen String aus der Ethernet.Adresse im gewohnten Format als 6 Werte zwischen 0 und 255
     * dazu muss jedoch getrickst werden, da Java Byte-Werte vorzeichenbehaftet interpretiert und alle 
     * Werte ab 1xxxxxxx (binr) als negativ angesehen werden
     * deswegen verwende ich die eine Funktion, die die Byte-Werte unsigned in Dezimalzahlen umrechnet
     * es ist somit nicht sonderlich schnell, aber diese Funktion ist sowieso nur fr Kontrollzwecke gedacht
     */

    public String print_Addr() {
	BitPosition bs = new BitPosition();
	return "" + bs.byte_to_unsigned((byte)Addr[5]) + "." + bs.byte_to_unsigned((byte)Addr[4]) + "." 
	    + bs.byte_to_unsigned((byte)Addr[3]) + "." + bs.byte_to_unsigned((byte)Addr[2]) + "." 
	    + bs.byte_to_unsigned((byte)Addr[1]) + "." + bs.byte_to_unsigned((byte)Addr[0]);
    }

    public void writeTo(Memory buf, int i) {
        buf.copyFromByteArray(Addr, 0, i, 6);
    }
}
