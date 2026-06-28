package jx.fs;

/**
 * An attempt is made to perform an operation on a file or symbolic link that only makes sense or is allowed on a directory (e.g., <code>mknode</code>, <code>mkdir</code>).
 * Es wird versucht, eine Operation auf eine Datei oder einen symbolischen Link auszuf&uuml;hren, die nur bei einem Verzeichnis
 * Sinn macht bzw. erlaubt ist (z.B. <code>mknode</code>, <code>mkdir</code>).
 */
public class NoDirectoryInodeException extends FSException {
    public NoDirectoryInodeException() {
	super("Not a directory");
    }

    public NoDirectoryInodeException(String msg) {
	super(msg);
    }
}
