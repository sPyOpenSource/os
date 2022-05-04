package jx.fs.javafs;

class InodeHashKey {
    public  int i_ino;
    private final int hashkey;

    public InodeHashKey(int i_ino) {
	this.i_ino = i_ino;
	hashkey = i_ino;
    }

    public int hashCode() {
	return hashkey;
    }

    public boolean equals(Object obj) {
	InodeHashKey ihk = (InodeHashKey)obj;
        return i_ino == ihk.i_ino;
    }
}
