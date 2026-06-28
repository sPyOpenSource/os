package jx.fs;

/**
 * The specified directory is not empty (e.g., when deleting).
 * Das angesprochene Verzeichnis ist nicht leer (z.B. beim L&ouml;schen).
 */
public class DirNotEmptyException extends FSException {
    public DirNotEmptyException() {
	super();
    }

    public DirNotEmptyException(String msg) {
	super(msg);
    }
}
