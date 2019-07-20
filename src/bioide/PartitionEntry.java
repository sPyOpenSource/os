package bioide;

import jx.zero.*;

/**
 * Entry in partition table
 * @author Michael Golm
 * @author Andreas Weissel
 */
public class PartitionEntry implements Partition, Service {

    /** number of first sector of this partition */
    int start;

    /** number of sectors of this partition */
    int size;

    /** Gibt an, ob auf die Partition zugegriffen werden darf (bei primaeren und logischen der Fall) */
    boolean accessable;

    private Drive drive;

    int os;

    private PartitionEntry() { }

    public PartitionEntry(Drive drive, int start, int size, boolean accessable, int os) {
	this.drive = drive;
	this.start = start;
	this.size = size;
	this.accessable = accessable;
	this.os = os;
    }
    
    @Override
    public int getCapacity() {
	return size;
    }

    @Override
    public int getSectorSize() { return 512; }

    @Override
    public void readSectors(int startSector, int numberOfSectors, Memory buf, boolean synchronous) { 
	drive.readSectors(start+startSector,numberOfSectors,buf,synchronous);
    }

    @Override
    public void writeSectors(int startSector, int numberOfSectors, Memory buf, boolean synchronous ) { 
	drive.writeSectors(start+startSector,numberOfSectors,buf,synchronous);
    }
    
    
    /** Vertauscht zwei Partitionseintraege (um sie zu sortieren) */
    void swapWithPartition(PartitionEntry one) {
	PartitionEntry sort_tmp = new PartitionEntry();
	if (one.start > start) {
	    sort_tmp.start = one.start; sort_tmp.size = one.size;
	    sort_tmp.accessable = one.accessable;
	    one.start = start; 
	    one.size = size;
	    one.accessable = accessable;
	    start = sort_tmp.start; size = sort_tmp.size;
	    accessable = sort_tmp.accessable;
	}
    }
    
}
