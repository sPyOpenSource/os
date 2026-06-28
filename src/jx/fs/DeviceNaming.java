package jx.fs;

public class DeviceNaming {

    /**
     * Returns the name of the partition with the specified identifier.
     * Gibt den Namen der Partition mit der angegebenen Kennung zur&uuml;ck.
     *
     * @param  device the identifier of the partition
     * @return the name of the partition with this identifier ("hda", "hdb", "hdc" or "hdd" + partition number)
     */
    public static String deviceToName(int device) {
	String retval = null;
	int part;
	if (device == 99) return "hdemul";
	if ((device & 3) == 0) retval = "hda";
	if ((device & 3) == 1) retval = "hdc";
	if ((device & 3) == 2) retval = "hdb";
	if ((device & 3) == 3) retval = "hdd";
	part = (device >> 4) & 15;
	if (part > 0)
	    retval += String.valueOf(part); // ohne +1
	return retval;
    }


    /**
     * Returns the identifier of the partition with the specified name.
     * Gibt die Kennung der Partition mit dem angegebenen Namen zur&uuml;ck.
     *
     * @param  name the name of the partition (must be "hda", "hdb", "hdc" or "hdd", followed by a number)
     * @return the identifier of the partition with this name
     */
    public static int nameToDevice(String name) {
	int device = -1, part = 0;

	if (name.equals("hdemul")) return 99;
	if (name.startsWith("hda")) device = 0;
	if (name.startsWith("hdb")) device = 2;
	if (name.startsWith("hdc")) device = 1;
	if (name.startsWith("hdd")) device = 3;
	if (device == -1)
	    return -1;
	if (name.length() > 3)
	    part = Integer.parseInt(name.substring(3,4)); // ohne -1, besser nur substring(3) ?
	return ((part << 4) | device);
    }
}
