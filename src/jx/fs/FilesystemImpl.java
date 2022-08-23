package jx.fs;

import jx.zero.Service;

import java.util.Vector;

public class FilesystemImpl implements FileSystemInterface, Service {
    private final FileSystem     fs;
    private final EXT2Permission defaultPermission = new EXT2Permission(EXT2Permission.RWX, 0, 0);
    private Node          rInode;
    private final Vector  fsobjList = new Vector();
    private boolean       umounted = true;

    public FilesystemImpl(FileSystem fs) {
	this.fs = fs;
	mount();
    }

    @Override
    public String getName() {
	return fs.name();
    }

    @Override
    public FSObject openRootDirectoryRO() {
	rInode.incUseCount();
	return registerFSObj(new ReadOnlyDirectoryImpl(this, fs, null, rInode));
    }

    @Override
    public FSObject openRootDirectoryRW() {
	rInode.incUseCount();
	return registerFSObj(new DirectoryImpl(this, fs, null, rInode));
    }

    @Override
    public Permission getDefaultPermission() {
	return defaultPermission;
    }

    @Override
    public void mount() {
	if (!umounted) return;
	fs.init(true);
	rInode = fs.getRootNode();
	umounted = false;
    }

    @Override
    public void unmount() {
	umounted = true;
	try {
	    for (int i = 0; i < fsobjList.size(); i++) {
		try {
		    FSObjectImpl fsobj = (FSObjectImpl)fsobjList.elementAt(i);
		    fsobj.close();
		} catch (Exception ex) {ex.printStackTrace();}
	    }
	    fsobjList.removeAllElements();
	    rInode.decUseCount();
	} catch (Exception ex) {ex.printStackTrace();}
	rInode = null;
	fs.release();
    }

    FSObjectImpl registerFSObj(FSObjectImpl fsobj) {
	if (umounted) {
	    try {fsobj.close();} catch (Exception ex) {ex.printStackTrace();}
	    return null;
	}
	fsobjList.addElement(fsobj);
	return fsobj;
    }

    @Override
    protected void finalize() throws Throwable {
	if (!umounted) unmount();
    }
}
