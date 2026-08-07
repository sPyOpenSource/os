package jx.bio.buffercache;

import jx.zero.Debug;
import jx.zero.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

public class BufferHead extends jx.fs.buffercache.BufferHead {
    static int sequence = 1;
    int id;

    private final static boolean paranoid = false;

    /** used by the BufferHashtable */
    int hashtable_hashkey;
    BufferHead hashtable_nextInChain;

    /** used by BufferCache */
    int    b_flushtime;
    private  int     b_count, b_list;
    private volatile boolean dirty, locked, uptodate;  // BufferCache
    private final Object waitLock = new Object();
    private int waitCount = 0;

    /** the block number within the file system */
    int     b_block;
    /** the block size (1024, 2048 or 4096 bytes) */
    int     b_size;


    public BufferHead(MemoryManager memMgr, int block, int size) {
	super(memMgr.allocAligned(size, 4));
	this.b_block  = block;
	this.b_size   = size;
	b_count = 1;
	hashtable_hashkey = this.b_block;
	id = sequence++;
	if (size != 1024) throw new Error("only size 1024 supported");
    }

    @Override
    final public int getBlock() { return b_block; }
    
    @Override
    final public int getSize() { return b_size; }

    /**
     * Marks the BufferHead as "dirty", i.e. its content has changed and no longer matches its image
     * on the disk (the content of the corresponding block); it must still be written.
     * Markiert den BufferHead als "dirty", d.h. sein Inhalt hat sich ge&auml;ndert und stimmt nicht mehr mit seinem Abbild
     * auf der Festplatte &uuml;berein (dem Inhalt des entsprechenden Blocks); er muss noch geschrieben werden.
     */
    @Override
    final public void markDirty() { dirty = true; }
    
    @Override
    final public void markClean() { dirty = false; }

    /**
     * Indicates whether the content of the BufferHead has been changed and not yet written to disk.
     * Gibt an, ob der Inhalt des BufferHeads ge&auml;ndert und noch nicht auf die Festplatte geschrieben wurde.
     *
     * @return <code>true</code>, if the content of the BufferHead does not match that on the disk
     */
    @Override
    public final boolean dirty() { return dirty; }
    @Override
    final public boolean isDirty() { return dirty; }

    /**
     * Locks the BufferHead or releases the lock of the BufferHead. For the duration of the lock, no other
     * threads can access it. To wait for the end of the lock, the method <code>waitOn</code> is used; upon
     * release of the lock, other threads/processes can be reactivated using <code>notifyAll</code>.
     * Sperrt den BufferHead bzw. gibt die Sperre des BufferHeads frei. F&uuml;r die Dauer der Sperre k&ouml;nnen keine anderen
     * Threads darauf zugreifen. Um auf das Ende der Sperre zu warten, dient die Methode <code>waitOn</code>; bei einer Freigabe
     * der Sperre lassen sich andere Threads/Prozesse mittels <code>notifyAll</code> wieder aktivieren.
     */
    @Override
    final public void lock() {
	synchronized (waitLock) {
	    locked = true;
	}
    }
    @Override
    final public void unlock() {
	synchronized (waitLock) {
	    locked = false;
	    if (waitCount > 0) {
		waitLock.notifyAll();
	    }
	}
    }

    /**
     * Returns the state of the lock.
     * Liefert den Zustand der Sperre zur&uuml;ck.
     *
     * @return <code>true</code>, if the BufferHead is locked
     */
    @Override
    final public boolean isLocked()   { return locked; }


    @Override
    public void waitUntilUnlocked() {
	// Atomic wait for unlock using synchronized block
	synchronized (waitLock) {
	    while (locked) {
		waitCount++;
		try {
		    waitLock.wait();
		} catch (InterruptedException e) {
		    Thread.currentThread().interrupt();
		    break;
		} finally {
		    waitCount--;
		}
	    }
	}
    }

    /**
     * Sets the state of the BufferHead content. "Uptodate" means that the read operation was successful and the
     * content is valid.
     * Legt den Zustand des BufferHead-Inhalts fest. "Uptodate" bedeutet, dass die Leseoperation erfolgreich war und der
     * Inhalt g&uuml;ltig ist.
     */
    @Override
    final public void markUptodate() { uptodate = true; }

    /**
     * Returns the state of the BufferHead content.
     * Liefert den Zustand des BufferHead-Inhalts zur&uuml;ck.
     *
     * @return <code>false</code> means that an error occurred during the operation and the content is no longer
     *         valid
     */
    @Override
    final public boolean isUptodate() { return uptodate; }

    /**
     * Waits until the lock of the BufferHead is released.
     * Wartet, bis die Sperre des BufferHeads aufgehoben wird.
     */
    @Override
    public void waitOn() {
	if (locked) {
	    b_count++;
	    synchronized (waitLock) {
		waitCount++;
		try {
		    while (locked) {
			waitLock.wait();
		    }
		} catch (InterruptedException e) {
		    Thread.currentThread().interrupt();
		} finally {
		    waitCount--;
		}
	    }
	    b_count--;
	}
    }

    /**
     * Ends the read or write operation. This method is called by the driver.
     * Beendet den Lese- bzw. Schreibevorgang. Diese Methode wird vom Treiber aufgerufen.
     *
     * @param error indicates whether an error occurred during the operation (<code>true</code>)
     * @param synchronous if <code>true</code>, waits for the end of the operation (using <code>sleep</code>)
     */
    @Override
    public void endIo(boolean error, boolean synchronous) {
	if (error)
	    throw new Error("IO Error");
	uptodate = true;
	locked = false;
	//if (synchronous)
	//    notifyAll();
    }

    /**
     * Clears the buffer.
     */
    @Override
    final public void clear() {
	data.clear();
	//data.fill32((short)0,0,b_size>>2);
	//data.fill16((short)0, 0, b_size>>1);
	//for (int i = 0; i < b_size; i++)
	//  data.set8(i, (byte)0);
    }

    /**
     * Deletes a range of the BufferHead content. The range is set to 0.
     * L&ouml;scht einen Bereich des BufferHeadinhalts. Der Bereich wird auf 0 gesetzt.
     *
     * @param from the first byte of the content to be deleted
     * @param to   the first byte of the range that should no longer be deleted
     */
    @Override
    public void clear(int from, int to) {
	if ((from < 0) || (from >= b_size) || (to < 0) || (to > b_size))
	    return;
	for (int i = from; i < to; i++)
	    data.set8(i, (byte)0);
}

    void init(int block) {
        b_count = 1;
        b_flushtime = 0;
        b_block = block;
	hashtable_hashkey = block;
	//clear(); // not necessary to clear buffer because it is set NOT up-to-date
	dirty = false;
	uptodate = false;
	locked = false;
    }

    @Override
    public final void ref() { b_count++; }
    final void unref() { 
	if (paranoid) {
	    if (b_count <= 0) throw new Error(); 
	}
	b_count--;
    }
    final boolean isUnused() { return b_count==0; }
    @Override
    final public boolean isUsed() { return b_count>0; }
    @Override
    final public boolean isUsedOnlyByMe() { return b_count==1; }


    @Override
    public final boolean isInFreeList() { return inlist; }
}
