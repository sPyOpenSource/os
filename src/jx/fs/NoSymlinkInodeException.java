package jx.fs;

/**
 * An attempt is made to perform an operation on a file or directory that only makes sense or is allowed on a symbolic link (e.g., <code>setSymlink</code>, <code>getSymlink</code>).
 * Es wird versucht, eine Operation auf eine Datei oder ein Verzeichnis auszuf&uuml;hren, die nur bei einem symbolischen Link
 * Sinn macht bzw. erlaubt ist (z.B. <code>setSymlink</code>, <code>getSymlink</code>).
 */
public class NoSymlinkInodeException extends FSException {
    public NoSymlinkInodeException() {
	super();
    }

    public NoSymlinkInodeException(String msg) {
	super(msg);
    }
}
