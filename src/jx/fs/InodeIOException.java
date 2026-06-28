package jx.fs;

/**
 * Internal error during an inode operation (inode data invalid or input/output error).
 * Interner Fehler bei einer Inodeoperation (Inodedaten ung&uuml;tig oder Fehler bei der Ein-/Ausgabe).
 */
public class InodeIOException extends FSException {
    public InodeIOException() {
	super("Internal inode handling error.");
    }

    public InodeIOException(String msg) {
	super(msg);
    }
}
