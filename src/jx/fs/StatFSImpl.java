package jx.fs;

public class StatFSImpl implements StatFS {
    public int tsize;
    public int bsize; 
    public int blocks; // total blocks
    public int bfree;  // free blocks
    public int bavail; // available blocks
}
