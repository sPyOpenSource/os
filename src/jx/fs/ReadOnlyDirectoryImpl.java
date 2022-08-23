package jx.fs;

final public class ReadOnlyDirectoryImpl extends FSObjectImpl implements ReadOnlyDirectory {

    public ReadOnlyDirectoryImpl(FilesystemImpl impl, FileSystem fs, FSObjectImpl parent, Node inode) {
	super(impl, fs, parent, inode);
    }
    
    public Directory  getParent() { return (Directory)parent; }

    @Override
    public String[] list() throws Exception { 
	String[] dirList = inode.readdirNames();
	return inode.readdirNames();
    }

    @Override
    public void close() throws Exception {
	inode.decUseCount();
	inode=null;
    }

    @Override
    public int length() throws Exception {return 0;}

    @Override
    public FSObject openRO(String filename) throws Exception {
	//try {
	    Node nInode = inode.lookup(filename);
	    if (nInode.isDirectory()) {
		return fs_impl.registerFSObj(new ReadOnlyDirectoryImpl(fs_impl, fs, this, nInode));
	    } else if (nInode.isFile()) {
		return fs_impl.registerFSObj(new ReadOnlyRegularFileImpl(fs_impl, fs, this, nInode));
	    } else {
		return null;
	    }
	//} catch (InodeIOException | InodeNotFoundException | NoDirectoryInodeException | NotExistException | PermissionException ex) {
	    //Debug.verbose("exception caught (lookup)");
	    //return null;
	//}
    }
    
    @Override
    public FSObject openRW(String filename) throws Exception {
	//try {
	    Node nInode = inode.lookup(filename);
	    if (nInode.isDirectory()) {
		return new DirectoryImpl(fs_impl,fs,this,nInode);
	    } else if (nInode.isFile()) {
		return new RegularFileImpl(fs_impl,fs,this,nInode);
	    } else {
		return null;
	    }
	//} catch (InodeIOException | InodeNotFoundException | NoDirectoryInodeException | NotExistException | PermissionException ex) {
	    //Debug.verbose("exception caught (lookup)");
	    //return null;
	//}
    }

    @Override
    protected void finalize() throws Throwable {
	inode.decUseCount();
    }
}
