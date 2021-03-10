package jx.fs;

import jx.zero.Memory;
import jx.zero.Debug;

public class ReadOnlyRegularFileImpl extends FSObjectImpl implements jx.fs.ReadOnlyRegularFile {

    final static boolean debug = false;

    public ReadOnlyRegularFileImpl(FilesystemImpl impl, FileSystem fs, FSObjectImpl parent, Inode inode) {
	super(impl, fs, parent, inode);
    }

    /**
     * Reads up to b.length bytes of data from this file into an array of bytes.
     * @param pos
     * @param mem
     * @param off
     * @param len
     * @return 
     * @throws java.lang.Exception
     */

    public int read(int pos, Memory mem, int off, int len) throws Exception {	
	if (debug) Debug.message(" F: read "+pos+" "+len); 
	try {
	    return inode.read(pos,mem,off,len);
	} catch (InodeIOException | NoFileInodeException | NotExistException | PermissionException ex) {
	    throw new Error(ex.toString());
	}
    }

    @Override
    public int length() throws Exception {
	if (debug) Debug.message(" F: length");
	try {
	    return inode.getLength();
	} catch (NotExistException ex) {
	    if (debug) Debug.message("Exception in RegularFileImpl.length");
	    return -1;
	}
    }

    @Override
    protected void finalize() throws Throwable {
	inode.decUseCount();
    }
}
