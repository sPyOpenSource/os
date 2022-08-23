package java.io;

import jx.InitialNaming;
import jx.fs.RegularFile;
import jx.fs.Directory;
import jx.fs.FS;
import jx.fs.Node;
import jx.fs.InodeIOException;
import jx.fs.InodeNotFoundException;
import jx.fs.NoDirectoryInodeException;
import jx.fs.NotExistException;
import jx.fs.PermissionException;

/**
  * Implementation of the File class accessing the JX file system
  */
public final class File implements Comparable {
    private String name;
    private String pathName;
    private String fileName;
    
    private FS fs;
    private Node fsobj;
    
    public static final String separator = "/";
    public static final char separatorChar = '/';
    public static final String pathSeparator = "/";
    public static final char pathSeparatorChar = '/';

    /**
     * Creates a new File instance by converting the given pathname string into an abstract pathname.
     * @param path
     */

    public File(String path) {  
	int n = path.lastIndexOf(separatorChar);	

	if (n < 0) {
	    fileName =  path;
	    pathName = "";
	} else {
	    fileName = path.substring(n + 1);
	    if (n == 0)
		pathName = pathSeparator;
	    else
		pathName = path.substring(0, n);
	}
	name = path;

	//Naming ns = InitialNaming.getInitialNaming();
	fs    = (FS)InitialNaming.lookup("FS");

	
	try {
	    fsobj = fs.lookup("/index.html");
	} catch (InodeIOException | InodeNotFoundException | NoDirectoryInodeException | NotExistException | PermissionException ex) {
	    fsobj = null;
	}
    }

    /**
     * Creates a new File instance from a parent pathname string and a child pathname string.
     * @param path
     * @param name
     */

    public File(String path, String name) {
	this(new File(path), name); 
    }

    /**
     * Creates a new File instance from a parent abstract pathname and a child pathname string.
     * @param dir
     * @param name
     */

    public File(File dir, String name) { 
	this.fileName = name;
	this.name = this.fileName;
	this.pathName = dir.getPath() + separator + name;
    }

    public String getName() { 
	return fileName;
    }

    public String getPath() {
	return pathName; 
    }

    public String getCanonicalPath() throws IOException {
	return getAbsolutePath(); 
    }
    
    public String getParent() {
	return pathName;
    }

    public boolean exists() {
	return fsobj != null;
    }

    /**
     * Tests whether the application can modify to the file denoted by this abstract pathname.
     * @return 
     */

    public boolean canWrite() {	
	throw new Error("not implemented");
	//return fsobj.isWriteable();
    }

    /**
     * Tests whether the application can read the file denoted by this abstract pathname.
     * @return 
     */

    public boolean canRead() { 
	throw new Error("not implemented");
	//return fsobj.isReadable();
    }

    public boolean isFile() {
	try {
	    return fsobj.isFile();
	} catch (NotExistException ex) {
	    return false;
	}
    }

    public boolean isDirectory() { 
	try {
	    return fsobj.isDirectory();
	} catch (NotExistException ex) {
	    return false;
	}
    }
  
    public boolean isAbsolute() {
	return (name.charAt(0) == separatorChar) ||
	    (Character.isLetter(name.charAt(0)) &&
	     (name.charAt(1) == ':') &&
	     (name.charAt(2) == '\\'));
    }
  
    public String getAbsolutePath()
    {
	return isAbsolute()
	    ? name
	    : System.getProperty("user.dir") + separator + name;
    }

    public long lastModified() {
	return 0;
    }

    /**
     * Returns the length of the file denoted by this abstract pathname.
     * @return 
     */
    public int length() {
	try {
	    return (int)((RegularFile)fsobj).length();
	} catch (Exception e) {
	    return -1;
	}
    }

    public boolean mkdir() { 
	/*
	try {
	    Directory dir = ((Directory)fs.lookup(pathName));
	    dir.mkdir(fs.getDefaultPermission(),fileName);
	} catch(Exception e) { 
	    return false;
	}
	return true;
	*/
	return false;
    }

    public boolean renameTo(File dest) { return false; }

    public boolean mkdirs() { return false; }

    /**
     * Returns an array of strings naming the files and directories in the directory denoted by this abstract pathname.
     * @return 
     */    
    public String[] list() {
	try {
	    Directory dir = ((Directory)fsobj);
	    return dir.list();
	} catch (Exception e) { 
	    return null; 
	}
    }

    /**
     * Returns an array of strings naming the files and directories in the directory denoted
     * by this abstract pathname that satisfy the specified filter.
     * @param filter
     * @return 
     */

    public String[] list(FilenameFilter filter) { return new String[] {""}; }

    public boolean delete() { return false; }

    @Override
    public int hashCode() {
	return 0; 
    }

    @Override
    public boolean equals(Object obj) { return false; }

    @Override
    public String toString() { 
	return name; 
    }
  
    /*
     * Additional Methods for Java 2
     */

    /**
     * Compares two abstract pathnames lexicographically.
     * @param pathname
     * @return 
     */
    public int compareTo(File pathname) { return 0; }

    /**
     * Compares this abstract pathname to another object.
     * @param o
     * @return 
     */
    @Override
    public int compareTo(Object o) {return 0; }

    //  Atomically creates a new, empty file named by this abstract pathname if
    // and only if a file with this name does not yet exist.
    boolean createNewFile() { return false; }
    
    // Creates an empty file in the default temporary-file directory, using the given prefix and suffix to generate its name.
    static File createTempFile(String prefix, String suffix) { return null; }
    
    // Creates a new empty file in the specified directory, using the given prefix and suffix strings to generate its name.
    static File createTempFile(String prefix, String suffix, File directory) { return null; }
    
    // Requests that the file or directory denoted by this abstract pathname be deleted when the virtual machine terminates.
    void deleteOnExit() {}
    
    // Returns the absolute form of this abstract pathname.
    File getAbsoluteFile() { return null; }
    
    // Returns the canonical form of this abstract pathname.
    File getCanonicalFile() { return null; }
    
    // Returns the abstract pathname of this abstract pathname's parent, or null if this pathname does not name a parent directory.
    File getParentFile() { return null; }
    
    // Tests whether the file named by this abstract pathname is a hidden file.
    boolean  isHidden() { return false; }
    
    // Returns an array of abstract pathnames denoting the files in the directory denoted by this abstract pathname.
    File[] listFiles() { return null; }
    
    // Returns an array of abstract pathnames denoting the files and directories in the directory denoted by this abstract pathname that satisfy the specified filter.
    // File[] listFiles(FileFilter filter) { return null; }    
    
    //Returns an array of abstract pathnames denoting the files and directories in the directory denotedby this abstract pathname that satisfy the specified filter.
    File[] listFiles(FilenameFilter filter) { return null; }
    
    //List the available filesystem roots.
    static File[] listRoots() { return null; }
    
    //Sets the last-modified time of the file or directory named by this abstract pathname.
    boolean setLastModified(long time) { return false; }
    
    //Marks the file or directory named by this abstract pathname so that only read operations are allowed.
    boolean setReadOnly() { return false; }
    
    //    java.net.URL toURL() { return null; }
}
