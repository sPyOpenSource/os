package jx.console;

import java.io.InputStream;
import java.io.IOException;

class ConsoleInputStream extends InputStream {
    VirtualConsoleImpl console;;
    ConsoleInputStream(VirtualConsoleImpl c) { console = c; }
    @Override
    public int read() throws IOException {
	return console.getc();
    }
}
