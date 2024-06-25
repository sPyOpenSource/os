package jx.fs;

import jx.zero.Memory;

final public class RegularFileImpl extends FSObjectImpl implements RegularFile {
        
    public RegularFileImpl(FilesystemImpl impl, FileSystem fs, FSObjectImpl parent, Node inode) {
	super(impl, fs, parent,inode);
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

    @Override
    public int read(int pos, Memory mem, int off, int len) throws Exception {
	return inode.read(pos,mem,off,len);
    }

    /**
     * Writes len bytes from the specified byte array starting at offset off to this file.
     * @param pos
     * @param mem
     * @param off
     * @param len
     * @return 
     * @throws java.lang.Exception
     */

    @Override
    public int write(int pos, Memory mem, int off, int len) throws Exception {
	return inode.write(pos,mem,off,len);
    }
    
    /**
     * Appends len bytes from the specified byte array starting at offset off to this file.
     * @param mem
     * @param off
     * @param len
     * @return 
     * @throws java.lang.Exception
     */

    @Override
    public int append(Memory mem, int off, int len) throws Exception {	
	return inode.write((int)getLength(),mem,off,len);
    }

    /**
     * Sets the length of this file.
     * @param newLength
     * @throws java.lang.Exception
     */

    @Override
    public void setLength(long newLength) throws Exception {
	throw new Error("not implemented yet");
    }

    @Override
    public int getLength() throws Exception {
	return inode.getLength();
    }

    @Override
    protected void finalize() throws Throwable {
	inode.decUseCount();
    }

    @Override
    public boolean isValid() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
