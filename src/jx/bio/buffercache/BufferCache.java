package jx.bio.buffercache;

import jx.zero.Debug;
import jx.zero.*;
import jx.devices.bio.BlockIO;
import jx.collections.Iterator;


/**
 * BufferCache implementation using a hashtable and an LRU freelist.
 *
 * @author Michael Golm, Andreas Weissel
 */

public class BufferCache implements jx.fs.buffercache.BufferCache {
    private static final boolean statistics = false;
    private static final boolean trace = true;
    private static final boolean debugSync = false;
    
    private final BufferHashtable buffer_hashtable;
    private final BufferFreeList free_list;
    private int nr_buffers;

    private final int initialNumberOfBlocks;
    private final int maximalNumberOfBlocks;
    private final int incrementNumberOfBlocks;
    
    //private Object buffer_wait;
    //private FlushDemon flushd;
    private final BlockIO idedevice;
    
    public int hit, miss; // statistics
    
    private final Clock clock;
    private final CPUManager cpuManager;
    private final int bufferSize;
    private final Profiler profiler;

    private final MemoryManager memMgr = ((MemoryManager)InitialNaming.getInitialNaming().lookup("MemoryManager"));

    /**
     * @param blockDevice block device that contains the blocks 
     * @param clock needed as time base
     * @param initialNumberOfBlocks initial size of buffer cache (in number of blocks)
     * @param maximalNumberOfBlocks maximal size of buffer cache (in number of blocks)
     * @param incrementNumberOfBlocks number of blocks to grow cache (in number of blocks)
     * @param bufferSize size of one buffer (==block size???)
     */
    public BufferCache(BlockIO blockDevice, Clock clock, 
		       int initialNumberOfBlocks,
		       int maximalNumberOfBlocks,
		       int incrementNumberOfBlocks,
		       int bufferSize
		       ) {
	this.idedevice = blockDevice;
	this.clock = clock;
	this.bufferSize = bufferSize;

	cpuManager = (CPUManager)InitialNaming.getInitialNaming().lookup("CPUManager");
	profiler = (Profiler)InitialNaming.getInitialNaming().lookup("Profiler");
	
	int memory_size;
	int order;
	buffer_hashtable = new BufferHashtable(20000);
	free_list = new BufferFreeList();
	this.maximalNumberOfBlocks = maximalNumberOfBlocks;
	this.initialNumberOfBlocks = initialNumberOfBlocks;
	this.incrementNumberOfBlocks = incrementNumberOfBlocks;
	if (maximalNumberOfBlocks < initialNumberOfBlocks) throw new Error("maximalNumberOfBlocks < initialNumberOfBlocks");
        
	//buffer_wait = new SleepObject();
	//buffer_wait = new Object();
	
	//flushd = new FlushDemon();
	//flushd.start();
    }


    /**
     * Stop the thread that flushes buffers to disk.
     */
    public  void stopDemon() {
	//flushd.stop();
    }

    /**
     * Writes a buffer to the block device.
     *
     * @param bh          the buffer that should be written
     * @param synchronous wait until the block is written
     */
    final void writeBlock(jx.fs.buffercache.BufferHead bh, boolean synchronous) {
	if (trace) Debug.out.println("rwBlock(" + bh.getBlock() + ")  size=" + bh.getSize());
	// map logical block number to device sector number
	try {
	    idedevice.writeSectors(bh.getBlock() * (1024 / 512), 1024 / 512, bh.getData(), true);
	} catch(Error e) {
	    bh.endIo(true, true);
	    return;
	}
	bh.endIo(false, true);
    }

    /**
     * Reads a buffer from the block device.
     *
     * @param bh          the buffer that should be read
     * @param synchronous wait until the block is read
     */
    final void readBlock(jx.fs.buffer.BufferHead bh, boolean synchronous) {
	if (trace) Debug.out.println("rwBlock(" + bh.getBlock() + ")  size=" + bh.getSize());
	try {
	    idedevice.readSectors(bh.getBlock() * (1024 / 512), (1024 / 512), bh.getData(), true);
	} catch(Error e) {
	    bh.endIo(true, true);
	    return;
	}
	bh.endIo(false, true);
    }

    private  void putFirstFree(BufferHead bh) {
	//if (trace) Debug.out.println("putFirstFree(" + bh.b_block+ ")");
	//if (free_list.contains(bh))
	//   Debug.out.println("FEHLER (putFirstFree): " + bh.b_block);
	// hang to the beginning of the free_list / An den Anfang der free_list haengen
	free_list.prependElement(bh); 
    }

    private  void putLastFree(BufferHead bh) {
	//if (trace) Debug.out.println("putLastFree(" + bh.b_block+ ")");
	//if (free_list.contains(bh)) Debug.out.println("FEHLER (putLastFree): " + bh.b_block);
	// hang to the end of the free_list / Ans Ende der free_list haengen
	free_list.appendElement(bh); 
    }

    @Override
    final public jx.fs.buffercache.BufferHead findBuffer(int block) {
	jx.fs.buffercache.BufferHead bh = buffer_hashtable.get(block); //new BufferHeadHashKey(block, size);
	if (bh != null) {
	    //Debug.out.println("buffer found: " + bh.b_block);
	    if (statistics) hit++;
	    bh.ref();
	    if (bh.dirty() == false)
		bh.b_flushtime = 0;

	    if (bh.isInFreeList()) {
		free_list.removeElement(bh);
		// buffer may be in hashtable AND in freelist
	    }
	} else {
	    if (trace) Debug.out.println("MISS: " + block);
	    if (statistics) miss++;
	}
	return bh;
    }

    @Override
    final public jx.fs.buffercache.BufferHead getblk(int block) {
	BufferHead bh;
	jx.fs.buffercache.BufferHead bh0;

	if (trace) Debug.out.println("getblk(" + block + " )");

	if ((bh0 = findBuffer(block)) != null)
	    return bh0;
	
	if (free_list.size() == 0) {
	    if (trace) Debug.out.println("GROW buffer cache");
	    growBuffers();
	}
	bh = (BufferHead)free_list.undockFirstElement();
	//if (trace) Debug.out.println("UNDOCK  "+bh.id);
	if (bh.isDirty()) {
	    writeBlock(bh, true);
	}
	if (bh.hashtable_hashkey >= 0) {
	    if (trace) Debug.out.println("REMOVE from Hashtable: "+bh.getBlock()+":"+bh.id);
	    buffer_hashtable.remove(bh);
	}
	bh.init(block);
	buffer_hashtable.put(bh);

	if (trace) Debug.out.println("getblk out with " + bh.getBlock());

	return bh;
    }

    /*
     * Set bufferhead free. Tell the Cache that the Bufferhead won't be used and can be inserted to LRU-List. 
     * Gibt den BufferHead frei. Dem Cache wird dadurch mitgeteilt, dass der 
     * BufferHead nicht mehr verwendet wird und in die LRU-Liste eingefuegt werden kann.
     * if it is marked as "dirty", The time is set at the earliest time when it can be written 
     * (in order to enable other access on Puffer without interrupting slow hard driver access) 
     * Falls er als "dirty" markiert ist, wird der Zeitpunkt
     * festgesetzt, zur dem er fr&uuml;hestens geschrieben werden darf
     * (um weitere Zugriffe auf den Puffer ohne die Unterbrechung
     * eines langsamen Festplattenzugriffs zu erm&ouml;glichen). 
     * The time spane is 30sec. If the access counter of Bufferhead 
     * is null, he will be attached at the end of the LRU-List, otherwise on the beginning.
     * Diese Zeitspanne betr&auml;gt 30 Sekunden. Ist der
     * Zugriffsz&auml;hler des <code>BufferHead</code>s (<code>b_count</code>) 
     * gleich 0, wird er, falls sein Inhalt g&uuml;ltig
     * ist, ans Ende der LRU-Liste geh&auml;ngt, ansonsten an den Anfang.
     *
     * @param bh the <code>BufferHead</code>, that should be released
     *
     * ****** Translated by George *****
     * Set bufferhead free. Tell the Cache that the Bufferhead won't be used and can be inserted to LRU-List. 
     * if it is marked as "dirty", The time is set at the earliest time when it can be written (in order to enable other access
     * on Puffer without interrupting slow hard driver access) The time spane is 30sec. If the access counter of Bufferhead 
     * is null, he will be attached at the end of the LRU-List, otherwise on the beginning.
     */
    @Override
    final public void brelse(jx.fs.buffer.BufferHead bh0) {
	BufferHead bh = (BufferHead)bh0;
	int newtime;

	if (bh == null)  return;

	if (trace) Debug.out.println("brelse(" + bh.getBlock() +")");
	
	//	if (bh.dirty()) writeBlock( bh, false);
	
	//Debug.out.println("brelse(): " + bh.b_block + ", dirty: " + bh.dirty());
	// If dirty, set time when the buffer should be written / Falls dirty, Zeit festlegen, zu der der Buffer geschrieben werden soll
	if (bh.isDirty()) {
	    newtime = clock.getTimeInMillis() + 30000; // 30 seconds / 30 Sekunden
	    if (bh.b_flushtime == 0 || bh.b_flushtime > newtime){
		bh.b_flushtime = newtime;
            }
	} else {
	    bh.b_flushtime = 0;
        }
	release(bh);
    }


    private void release(BufferHead bh) {
	if (bh.isUnused()) {
	    Debug.out.println("!!! BufferCache.brelse(): attamt to release an unused block: " + bh.b_block);
	    showBuffers();
	    cpuManager.printStackTrace();	  
	} else {
	    bh.unref();
	    if (bh.isUnused()) {
		if (bh.isUptodate())
		    putLastFree(bh);
		else
		    putFirstFree(bh);
	    }
	}
    }  

    /**
     * Hang the Bufferhead to the end of the LRU-List.
     *
     * @param bh0 the <code>BufferHead</code>, that should be set free ("forgotten")
     */
    @Override
    final public void bforget(jx.fs.buffercache.BufferHead bh0) {
	BufferHead bh = (BufferHead)bh0;
	if (bh == null)
	    return;
	    
	if (! bh.isUsedOnlyByMe() || bh.isLocked()) {
	    brelse(bh);
	    return;
	}
	bh.unref();
	putLastFree(bh);
    }
    
    /**
     * Read the specified block from hard disk. The corresponding <code>BufferHead</code> is
     * requested via <code>getblk</code> and the content of the block is read by the disk driver.
     * Liest den angegebenen Block von Festplatte. Der entsprechende <code>BufferHead</code> wird mittels <code>getblk</code>
     * angefordert und der Inhalt des Blocks vom Festplattentreiber gelesen.
     *
     * @param  block  the number of the block that should be read
     * @return the <code>BufferHead</code> with the content of the specified block
     * 
     *      ****** Translated by George *****
     *      Read the block of hard disk. The Bufferhead will be required with <code>getblk</code> so  
     *      the content of the block can be read 
     *      
     *      block the number of the block that should be read
     *      return Bufferhead with the content of the specified block
     *      
     */
    @Override
    final public jx.fs.buffercache.BufferHead bread(int block) {
	jx.fs.buffercache.BufferHead bh;

	if (trace) Debug.out.println("bread(" + block +")");

	bh = getblk(block);
	if (bh.isUptodate())
	    return bh;
	readBlock(bh, true);
	if (bh.isUptodate())
	    return bh;
	throw new Error("IO Error");
	/*
	  brelse(bh);
	  Debug.out.println("bread(" + block + ", " + size + ") failed");
	  return null;
	*/
    }

    private  void growBuffers() {
	BufferHead bh;
	int count;

	if (trace) Debug.out.println("growBuffers");

	if (free_list.size() == 0) 
	    count = initialNumberOfBlocks;
	else
	    count = incrementNumberOfBlocks;

	if (free_list.size() + count > maximalNumberOfBlocks)
	    throw new Error("nr_buffers + count > maximalNumberOfBlocks");

	for (int i = 0; i < count; i++) {
	    bh = new BufferHead(memMgr, -1, bufferSize);
	    free_list.appendElement(bh);
	    nr_buffers++;
	}
	if (trace) Debug.out.println("BufferCache: created " + count + " buffers");
	Debug.out.println("buffercache.BufferCache: size now: " + (nr_buffers * bufferSize) + " bytes");
	//buffer_wait.wakeUp();
	//buffer_wait.notify();

    }

    /**
     * Print one statistic of BufferHead in the Cache and the LRU-List. The number of blocked and as "dirty" marked BufferHead 
     * can be determined.
     * Gibt eine Statistik der <code>BufferHead</code>s im Cache und der LRU-Liste aus. Die Zahl der gesperrten
     * und als "dirty" markierten <code>BufferHead</code>s wird ermittelt.
     *      
     *      ****** Translated by George *****
     *      
     *      print one statistic of BufferHead in the Cache and the LRU-List. The number of blocked and as "dirty" marked BufferHead 
     *      can be determined. 
     *      
     */
    @Override
    public void showBuffers() {
	BufferHead bh;
	int found = 0, locked = 0, dirty = 0, used = 0;
	String buf_types[] = new String[] {"CLEAN", "LOCKED", "DIRTY"};

	Debug.out.print("BufferHeads: " + nr_buffers);
	Debug.out.println(", free_list: " + free_list.size());

	Iterator e = buffer_hashtable.iterator();
	while (e.hasNext()) {
	    bh = (BufferHead)e.next();

	    if (bh == null) {
		Debug.out.println("bh == null !!!!!!!");
		continue;
	    }

	    found++;
	    if (bh.isLocked()) {
		/*System.out*/Debug.out.println("Block: " + bh.b_block + " = locked");
		locked++;
	    }
	    if (bh.dirty()) {
		/*System.out*/Debug.out.println("Block: " + bh.b_block + "  = dirty");
		dirty++;
	    }
	    if (bh.isUsed()) {
		/*System.out*/Debug.out.println("Block: " + bh.b_block + " = used");
		used++;
	    }

	    ///*System.out*/Debug.out.print("Block: " + bh.b_block + " (usecount = " + bh.b_count + ")");
	    //if (bh.dirty()) /*System.out*/Debug.out.print(", dirty");
	    //if (bh.isLocked()) /*System.out*/Debug.out.print(", gesperrt"); 
	    ///*System.out*/Debug.out.println(" ");
	}
	Debug.out.println("Hashtable: " + found + " buffers, " + used + " benutzt, " +
			  locked + " gesperrt, " + dirty + " dirty");
    }

    /**
     * Write the Bufferhead of specified file system, that was marked as "dirty" on the partition
     * Schreibt die <code>BufferHead</code>s des angegebenen Dateisystems (Partition), die als "dirty" markiert sind, auf
     * die angegebene Partition.
     *
     *      ****** Translated by George ***** 
     *      
     *      Write the Bufferhead of specified file system, that was marked as "dirty" on the partition
     */
    @Override
    public void syncDevice(boolean wait) {
	if (debugSync) { 
	    Debug.out.println("Buffers before sync");
	    showBuffers();
	}
	syncBuffers(wait, true);
	if (debugSync) { 
	    Debug.out.println("Buffers after sync");
	    showBuffers();
	}
    }

    private  void syncBuffers(boolean wait, boolean signalError) {
	BufferHead bh;
	    
	Iterator e = buffer_hashtable.iterator();
	while (e.hasNext()) {
	    bh = (BufferHead)e.next();

	    if (bh == null) {
		Debug.out.println("bh == null !!!!!!!");
		continue;
	    }

	    if (bh.isLocked()) {  // Buffer is locked, if wait==false, ignore / Buffer ist gesperrt, falls wait==false, ignorieren
		if (signalError) throw new Error("cannot sync");
		if (wait == false)
		    continue;
		bh.waitOn();
		continue;
	    }

	    // If a buffer is not locked and not uptodate, ignore (IO error)
	    // Falls ein Buffer nicht gesperrt und nicht uptodate ist, ignorieren (IO-Fehler)
	    if ((wait == true) && (bh.isLocked() == false) &&
		(bh.dirty() == false) && (bh.isUptodate() == false)) {
		continue;
	    }

	    // Only write dirty buffers / Nur dirty Buffer schreiben
	    if (bh.dirty() == false)
		continue;

	    bh.ref();
	    if (debugSync) Debug.out.println("writing " + bh.b_block);
	    bh.b_flushtime = 0;
	    writeBlock( bh, true);
	    bh.unref();
	    if (bh.isUnused())
		bh.markClean();
	}
    }

    /**
     * All BufferHead, that was marked as dirty (means that still need to be written) will rewritten with ewBlock 
     * to the hard disk - if they are neither blocked nor too old.
     * Alle <code>BufferHead</code>s, die als "dirty" markiert sind, d.h. noch geschrieben werden m&uuml;ssen, werden mittels
     * <code>rwBlock</code> auf die Festplatte zur&uuml;ckgeschrieben, sofern sie nicht gesperrt sind bzw. noch nicht alt genug
     * sind.
     *
     *      ****** Translated by George ***** 
     *      All BufferHead, that was marked as dirty (means that still need to be written) will rewritten with ewBlock 
     *      to the hard disk - if they are neither blocked nor too old.
     */
    @Override
    public  void flushCache() {
	int ndirty = 0, nwritten = 0, ncount = 0;
	BufferHead bh;

	Debug.out.println("flushing ...");

	Iterator e = buffer_hashtable.iterator();
	while (e.hasNext()) {
	    bh = (BufferHead)e.next();

	    if (bh == null) {
		Debug.out.println("bh == null !!!!!!!");
		continue;
	    }
		    
	    if (bh.isLocked())
		Debug.out.println("Buffer " + bh.b_block + " locked");
	    
	    if (bh.isLocked() || (bh.dirty() == false))
		continue;
	    ndirty++;
	    if (clock.getTimeInMillis() < bh.b_flushtime) {
		///*System.out*/Debug.out.println("not old enough " + bh.b_block);
		///*System.out*/Debug.out.println("nicht alt genug " + bh.b_block);
		continue;
	    }
	    nwritten++;
	    bh.ref();
	    bh.b_flushtime = 0;
	    //Debug.out.println("writing " + bh.b_block);
	    writeBlock( bh, false);
	    ncount++;
	    bh.unref();
	    if (bh.isUnused())
		bh.markClean();
	}
	//if (ncount > 0)
	///*System.out*/Debug.out.println("BufferCache.flush(): "+ncount+" dirty BufferHeads in der dirty-Liste");
	///*System.out*/Debug.out.println("Wrote "+nwritten+"/"+ndirty+" buffers");
	//showBuffers();

    }

    @Override
    final public void updateBuffer(jx.fs.buffercache.BufferHead bh) {
	if (! bh.isUptodate()) {
	    /*if (profiler.isSampling()) {
		Debug.out.println("update buffer block="+bh.getBlock());
		printStatistics();
		}*/
	    readBlock(bh, true);
	    if (! bh.isUptodate()) {
		throw new Error();
	    }
	}
    }

    @Override
    final public void bdwrite(jx.fs.buffer.BufferHead bh) {
	bh.markDirty();
	brelse(bh);
    }

    @Override
    final public void bdirty(jx.fs.buffercache.BufferHead bh) {
	bh.markDirty();
    }

    @Override
    final public void bawrite(jx.fs.buffercache.BufferHead bh) {
	bwrite(bh);
    }

    @Override
    final public void bwrite(jx.fs.buffercache.BufferHead bh) {
	bh.markDirty();
	writeBlock(bh, true);
	release((BufferHead)bh);
    }

    @Override
    public void breadn(int startBlock, int nBlocks) {
	throw new Error();
    }

    final public void readAhead(jx.fs.buffercache.BufferHead bh) {
	readBlock(bh, true);
	brelse(bh);
	throw new Error();
    }

    public void printStatistics() {
	Debug.out.println("Buffercache:");
	Debug.out.println("hits:"+hit+", missed:"+miss);
	Debug.out.println("Hashtable:");
	buffer_hashtable.printStatistics();
    }
}
