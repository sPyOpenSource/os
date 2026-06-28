package jx.fs;

/**
 * An attempt is made to perform an operation on a directory or symbolic link that only makes sense or is allowed on a file (e.g., <code>read</code> and <code>write</code>).
 * Es wird versucht, eine Operation auf ein Verzeichnis oder einen symbolischen Link auszuf&uuml;hren, die nur bei einer Datei
 * Sinn macht bzw. erlaubt ist (z.B. <code>read</code> und <code>write</code>).
 */
public class NoFileInodeException extends FSException {
    public NoFileInodeException() {
	super();
    }

    public NoFileInodeException(String msg) {
	super(msg);
    }
}
