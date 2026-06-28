package jx.fs;

/**
 * The requested inode cannot be found.
 * Die angesprochene Inode kann nicht gefunden werden
 */
public class InodeNotFoundException extends FSException {
    public InodeNotFoundException() {
	super("Inode not found.");
    }

    public InodeNotFoundException(String msg) {
	super(msg);
    }
}
