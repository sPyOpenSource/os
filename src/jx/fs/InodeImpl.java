package jx.fs;

import java.util.*;
import jx.zero.Memory;
import jx.zero.ReadOnlyMemory;

/**
 * A default implementation of the Inode.
 */
public abstract class InodeImpl implements Node {
    protected Node      parent;
    protected Vector    overlayNames;
    protected Vector    overlayInodes;
    protected boolean   i_dirty, i_released;

    /** Maske f&uuml;r den Typ der Inode (Datei, Verzeichnis oder symbolischer Link) */
    public static final int S_IFMT    = 00170000;  // Bits 16, 15, 14, 13
    /** Inode repr&auml;sentiert einen symbolischen Link */
    public static final int S_IFLNK   =  0120000;  // Bit 16 + Bit 14
    /** Inode repr&auml;sentiert eine regul&auml;re Datei */
    public static final int S_IFREG   =  0100000;  // Bit 16
    /** Inode repr&auml;sentiert ein Verzeichnis */
    public static final int S_IFDIR   =  0040000;  // Bit 15
    /**
     * Dateien mit "Set UID"-Bit erhalten die effektive User-ID bei der Ausf&uuml;hrung,
     * bei Verzeichnissen hat "Set UID" keinen Effekt.
     */
    public static final int S_ISUID   =  0004000;  // Bit 12
    /**
     * Dateien, die in einem Verzeichnis mit gesetztem "Set GID"-Bit erzeugt werden,
     * erhalten die Gruppen-ID dieses Verzeichnisses und ebenfalls das GID-Bit.
     * Dateien mit "Set GID"-Bit erhalten die effektive Gruppen-ID bei der Ausf&uuml;hrung.
     */
    public static final int S_ISGID   =  0002000;  // Bit 11
    /**
     * Bei einem Verzeichnis bedeutet das "sticky"-Bit, dass darin enthaltene Dateien
     * nur von ihrem Besitzer, vom Besitzer des Verzeichnisses und von Root umbenannt
     * oder gel&ouml;scht werden d&uuml;rfen.
     */
    public static final int S_ISVTX   =  0001000;  // Bit 10

    /** die Zugriffsrechte f&uuml;r den Besitzer der Inode */
    public static final int S_IRWXU   =    00700;  // Bits 9, 8, 7
    /**
     * Datei:       der Besitzer darf auf den Dateiinhalt lesend zugreifen;
     * Verzeichnis: der Besitzer darf die Verzeichniseintr&auml;ge auslesen.
     */
    public static final int S_IRUSR   =     0400;  // Bit 9
    /**
     * Datei:       der Besitzer darf auf den Dateiinhalt schreibend zugreifen;
     * Verzeichnis: der Besitzer darf Dateien im Verzeichnis erzeugen und l&ouml;schen.
     */
    public static final int S_IWUSR   =     0200;  // Bit 8
    /**
     * Datei:       der Besitzer darf die Datei ausf&uuml;hren;
     * Verzeichnis: der Besitzer darf das Verzeichnis nach einem Dateinamen durchsuchen.
     */
    public static final int S_IXUSR   =     0100;  // Bit 7

    /** die Zugriffsrechte f&uuml;r die der Inode zugeordnete Gruppe */
    public static final int S_IRWXG   =     0070;  // Bits 6, 5, 4
    /**
     * Datei:       die Gruppe darf auf den Dateiinhalt lesend zugreifen;
     * Verzeichnis: die Gruppe darf die Verzeichniseintr&auml;ge auslesen.
     */
    public static final int S_IRGRP   =      040;  // Bit 6
    /**
     * Datei:       die Gruppe darf auf den Dateiinhalt schreibend zugreifen;
     * Verzeichnis: die Gruppe Dateien im Verzeichnis erzeugen und l&ouml;schen.
     */
    public static final int S_IWGRP   =      020;  // Bit 5
    /**
     * Datei:       die Gruppe darf die Datei ausf&uuml;hren;
     * Verzeichnis: die Gruppe darf das Verzeichnis nach einem Dateinamen durchsuchen.
     */
    public static final int S_IXGRP   =      010;  // Bit 4

    /** die Zugriffsrechte f&uuml;r alle anderen */
    public static final int S_IRWXO   =      007;  // Bits 3, 2, 1
    /**
     * Datei:       Alle anderen d&uuml;rfen auf den Dateiinhalt lesend zugreifen;
     * Verzeichnis: Alle anderen d&uuml;rfen die Verzeichniseintr&auml;ge auslesen.
     */
    public static final int S_IROTH   =       04;  // Bit 3
    /**
     * Datei:       Alle anderen d&uuml;rfen auf den Dateiinhalt schreibend zugreifen;
     * Verzeichnis: Alle anderen d&uuml;rfen Dateien im Verzeichnis erzeugen und l&ouml;schen.
     */
    public static final int S_IWOTH   =       02;  // Bit 2
    /**
     * Datei:       Alle anderen d&uuml;rfen die Datei ausf&uuml;hren;
     * Verzeichnis: Alle anderen d&uuml;rfen das Verzeichnis nach einem Dateinamen durchsuchen.
     */
    public static final int S_IXOTH   =       01;  // Bit 1

    /** die Zugriffsrechte der Inode (Besitzer, Gruppe, Andere) */
    public static final int S_IRWXUGO = (S_IRWXU|S_IRWXG|S_IRWXO);
    /** alle Bits, die Zugriffsrechte beschreiben */
    public static final int S_IALLUGO = (S_ISUID|S_ISGID|S_ISVTX|S_IRWXUGO);
    /**
     * Datei:       Lesezugriff;
     * Verzeichnis: Auslesen der Verzeichniseintr&auml;ge.
     */
    public static final int S_IRUGO   = (S_IRUSR|S_IRGRP|S_IROTH);
    /**
     * Datei:       Schreibzugriff;
     * Verzeichnis: Dateien erzeugen und l&ouml;schen.
     */
    public static final int S_IWUGO   = (S_IWUSR|S_IWGRP|S_IWOTH);
    /**
     * Datei:       Datei darf ausgef&uuml;hrt werden;
     * Verzeichnis: Suche nach Dateinamen.
     */
    public static final int S_IXUGO   = (S_IXUSR|S_IXGRP|S_IXOTH);

    protected InodeImpl(Node parent) {
	this.parent = (InodeImpl)parent;
	overlayNames = new Vector();
	overlayInodes = new Vector();
	i_dirty = false;
    }
    protected InodeImpl() {
	this(null);
    }
    
    @Override
    public Node getParent() { return parent; }

    @Override
    public void setParent(jx.fs.Node parent) {
	this.parent = (InodeImpl)parent;
    }

    @Override
    public boolean isDirty() {
	return i_dirty;
    }

    @Override
    public abstract void setDirty(boolean value);

    @Override
    public abstract void incUseCount();

    @Override
    public abstract void decUseCount();

    @Override
    public void finalize() {
	if (i_released == false)
	    decUseCount();
    }

    @Override
    public abstract int   i_nlinks();// throws NotExistException;

    @Override
    public abstract void  deleteNode();// throws InodeIOException, NotExistException;

    @Override
    public abstract void  writeNode();// throws InodeIOException, NotExistException;

    @Override
    public abstract void  putNode();// throws NotExistException;

    @Override
    public void overlay(Node newChild, String name) throws InodeIOException, InodeNotFoundException, NoDirectoryInodeException, NotExistException, PermissionException 
    {
	    if (i_released){
		throw new NotExistException();
            }
	    if (!isDirectory()){
		throw new NoDirectoryInodeException();
            }
	    
	    Node inodeToOverlay = lookup(name); // wirft InodeNotFoundException
	    inodeToOverlay.decUseCount();
	    
	    overlayNames.addElement(name);
	    overlayInodes.addElement(newChild);
    }

    @Override
    public void removeOverlay(Node child)// throws InodeNotFoundException, NoDirectoryInodeException, NotExistException 
    {
	    if (i_released){
		//throw new NotExistException();
                return;
            }
	    if (!isDirectory()){
		//throw new NoDirectoryInodeException();
                return;
            }
	    
	    int index = overlayInodes.indexOf(child);
	    if (index == -1){
		//throw new InodeNotFoundException();
                return;
            }
	    
	    overlayNames.removeElementAt(index);
	    overlayInodes.removeElementAt(index);
	    child.decUseCount();
    }

    @Override
    public void removeAllOverlays()// throws NoDirectoryInodeException, NotExistException 
    {
	    /*if (i_released)
		throw new NotExistException();
	    if (! isDirectory())
		throw new NoDirectoryInodeException();*/
	    
	    for (int i = 0; i < overlayInodes.size(); i++) {
		Node inode = (Node)overlayInodes.elementAt(i);
		inode.decUseCount();
	    }
	    overlayNames.removeAllElements();
	    overlayInodes.removeAllElements();
    }

    @Override
    public  boolean isOverlayed(String name)// throws NoDirectoryInodeException, NotExistException 
    {
	    /*if (i_released)
		throw new NotExistException();
	    if (! isDirectory())
		throw new NoDirectoryInodeException();*/
	    
	    return overlayNames.contains(name);
    }

    @Override
    public Node lookup(String name) throws InodeIOException, InodeNotFoundException, NoDirectoryInodeException, NotExistException, PermissionException 
    {
	    if (i_released)
		throw new NotExistException();
	    if (! isDirectory())
		throw new NoDirectoryInodeException();
	    
	    if (name.equals(".")) {
		incUseCount();
		return this;
	    }
	    if (name.equals("..")) {
		parent.incUseCount();
		return parent;
	    }
	    for (int i = 0; i < overlayNames.size(); i++) {
		if (name.equals((String)overlayNames.elementAt(i))) {
		    Node inode = (Node)overlayInodes.elementAt(i);
		    inode.incUseCount();
		    return inode;
		}
	    }
	    
	    return getNode(name);
    }
	
    @Override
    public abstract boolean isSymlink();// throws NotExistException;

    @Override
    public abstract boolean isFile();// throws NotExistException;

    @Override
    public abstract boolean isDirectory();// throws NotExistException;

    @Override
    public abstract boolean isWritable();// throws NotExistException;

    @Override
    public abstract boolean isReadable();// throws NotExistException;

    @Override
    public abstract boolean isExecutable();// throws NotExistException;

    @Override
    public abstract int    lastModified();// throws NotExistException;
    @Override
    public abstract int    lastAccessed();// throws NotExistException;
    @Override
    public abstract int    lastChanged();// throws NotExistException;

    @Override
    public abstract void setLastModified(int time);// throws NotExistException;
    @Override
    public abstract void setLastAccessed(int time);// throws NotExistException;

    @Override
    public abstract String[]  readdirNames();// throws NoDirectoryInodeException, NotExistException;

    @Override
    public abstract Node   getNode(String name) throws InodeIOException, InodeNotFoundException, NoDirectoryInodeException, NotExistException, PermissionException;

    @Override
    public abstract Node   mkdir(String name, int mode) throws FileExistsException, InodeIOException, NoDirectoryInodeException, NotExistException, PermissionException;

    @Override
    public abstract void    rmdir(String name) throws DirNotEmptyException, InodeIOException, InodeNotFoundException, NoDirectoryInodeException, NotExistException,PermissionException;

    @Override
    public abstract Node   create(String name, int mode) throws FileExistsException, InodeIOException, NoDirectoryInodeException, NotExistException, PermissionException;

    @Override
    public abstract void    unlink(String name) throws InodeIOException, InodeNotFoundException, NoDirectoryInodeException, NoFileInodeException, NotExistException,PermissionException;

    @Override
    public abstract Node   symlink(String symname, String newname) throws FileExistsException, InodeIOException, NoDirectoryInodeException, NotExistException, NotSupportedException, PermissionException;

    @Override
    public abstract String  getSymlink() throws InodeIOException, NoSymlinkInodeException, NotExistException, NotSupportedException, PermissionException;

    @Override
    public abstract void    rename(String oldname, Node new_dir, String newname) throws InodeIOException, InodeNotFoundException, NoDirectoryInodeException, NotExistException, PermissionException;
    
    @Override
    public abstract int     read(Memory m, int off, int len) throws InodeIOException, NoFileInodeException, NotExistException, PermissionException;
    @Override
    public abstract int     read(int pos, Memory m, int bufoff, int len) throws InodeIOException, NoFileInodeException, NotExistException, PermissionException;
    @Override
    public ReadOnlyMemory readWeak(int off, int len) throws InodeIOException, NoFileInodeException, NotExistException, PermissionException 
    { throw new Error("not applicable");}
    
    @Override
    public abstract int     write(Memory m, int off, int len) throws InodeIOException, NoFileInodeException, NotExistException, PermissionException;
    @Override
    public abstract int     write(int pos, Memory m, int bufoff, int len) throws InodeIOException, NoFileInodeException, NotExistException, PermissionException;

    @Override
    public abstract int available();// throws NotExistException;

    @Override
    public abstract int getLength();// throws NotExistException;

    @Override
    public int getIdentifier()//  throws NotExistException 
    { 
	throw new Error("No identifier available");
    }
    @Override
    public abstract int getVersion();// throws NotExistException;

    @Override
    public FileSystem getFileSystem()//  throws NotExistException 
    { 
	throw new Error("No file system available");
    }

    @Override
    public abstract StatFS getStatFS();

}
