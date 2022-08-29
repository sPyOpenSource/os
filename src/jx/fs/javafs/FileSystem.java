package jx.fs.javafs;

import jx.zero.Debug;
import jx.zero.Service;
import jx.zero.Clock;
import jx.devices.bio.BlockIO;

import jx.fs.FSException;
import jx.fs.InodeIOException;
import jx.fs.Node;
import jx.fs.NotExistException;
import jx.fs.buffer.BufferCache;

/**
 * Instances of this class are used by VFS to operate on a jafafs as a whole.
 * This includes creating, repairing, mounting, and unmounting the file system. 
 */
public class FileSystem implements jx.fs.FileSystem, Service {
    private Super       root;
    private InodeImpl   rootInode;
    private boolean     inited = false;
    private BufferCache bufferCache;
    private final InodeCache  inodeCache;
    private Tools       tools;
    private Clock       clock;
    private final int   deviceID;

    public FileSystem() {
	inodeCache = new InodeCache();
	deviceID = 1;
    }

    @Override
    public void init(BlockIO blockDevice, BufferCache bufferCache, Clock clock) {
	if (inited) throw new Error("FS already initialized");
	this.clock = clock;
	this.bufferCache = bufferCache;
	tools = new Tools(blockDevice, bufferCache, inodeCache, clock);
    }

    @Override
    public void init(boolean read_only) {
	if (inited)
	    return;
	
	
	inited = true;
	Debug.out.println("INIT JavaFS");
	root = new Super(this, read_only, bufferCache, inodeCache, clock);
	root.dump();
	rootInode = root.getRootInode();
    }

    @Override
    public String name() {
	return "JavaFS";
    }

    @Override
    public Node getRootNode() {
	if (!inited) throw new Error("FS not initialized");
	rootInode.incUseCount();
	return (Node)rootInode;
    }

    @Override
    public void release() {
	if (! inited) throw new Error("FS not initialized");
	Debug.out.println("releasing filesystem ");
	rootInode.decUseCount();
	root.writeSuper();
	root.putSuper();
	// direntrycache.invalidateEntries(device); -> jetzt im VFS!
	/*
	inodeCache.syncInodes();
	bufferCache.syncDevice(false);
	*/
	inodeCache.syncInodes();
	bufferCache.syncDevice(true); //wait
	inodeCache.invalidateInodes();
	bufferCache.flushCache();
	//bufferCache.stopDemon();
    }

    @Override
    public void build(String name, int blocksize) {
	inited = false;
	tools.makeFS(name, blocksize);
	init(false);
    }

    @Override
    public void check() {
	if (! inited) throw new Error("FS not initialized");
	tools.checkFS(null); /* pass answermachine */
    }

    public void printStatistics() {
	if (! inited) throw new Error("FS not initialized");
	inodeCache.showInodes();
	bufferCache.showBuffers();
    }

    @Override
    public Node getNode(int inodeNumber) throws FSException  {
	DirEntryData de_data;
	InodeImpl inode;
	
	//	System.out.println("getInode aufgerufen!");
	inode = (InodeImpl)inodeCache.iget(inodeNumber);
	if (inode != null) {
	    //bufferCache.brelse(de_data.bh);
	    //System.out.println("im Cache");
	    return inode;
	}
	//System.out.println("nicht im Cache");
	
	InodeData i_data = root.getInodeData(inodeNumber);
	
	// Falls die Inodenummer nicht existiert, wird auch kein InodeData-Objekt 
	// zurckgeliefert.
	if (i_data == null) throw new NotExistException();

	if (i_data.i_size() < 0) { // overflow
	    Debug.out.println("getInode(): Inode zu gross (overflow)");
	    throw new InodeIOException();
	}

	
	if ((i_data.i_mode() & InodeImpl.S_IFMT) == InodeImpl.S_IFREG) {
	    //Debug.out.println("getInode(): erzeuge FileInode");
	    inode = new FileInode(this, root, inodeNumber, i_data, bufferCache, inodeCache, clock);
	}
	if ((i_data.i_mode() & InodeImpl.S_IFMT) == InodeImpl.S_IFDIR) {
	    //Debug.out.println("getInode(): erzeuge DirInode");
	    inode = new DirInode(this, root, inodeNumber, i_data, bufferCache, inodeCache, clock);
	}
	if ((i_data.i_mode() & InodeImpl.S_IFMT) == InodeImpl.S_IFLNK) {
	    //Debug.out.println("getInode(): erzeuge SymlinkInode");
	    inode = new SymlinkInode(this, root, inodeNumber, i_data, bufferCache, inodeCache, clock);
	}
	//bufferCache.brelse(de_data.bh);
	
	if (inode == null) {
	    /*System.out*/Debug.out.println("getInode(): invalid inode type: " + (i_data.i_mode() & InodeImpl.S_IFMT));
	    throw new InodeIOException();
	}
	//inode.setParent(this);
	inodeCache.addInode(inode);
	return inode;
    }
    
    @Override
    public int getDeviceID() {
	return deviceID; /* FIXME */
    }

    public Tools getTools() {
	return tools;
    }

    @Override
    public String read(String aitxt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
