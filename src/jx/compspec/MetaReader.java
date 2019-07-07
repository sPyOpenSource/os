package jx.compspec;


import java.io.*;
import java.util.Vector;


public class MetaReader {
    String[] compdirs;

    public MetaReader(String[] compdirs) {
	this.compdirs = new String[compdirs.length];
	for(int i = 0; i < compdirs.length; i++) {
	    this.compdirs[i] = compdirs[i].trim();
	}
	
    }

    public void addMeta(Vector v, String l) throws IOException {
        for (String compdir : compdirs) {
            String filename = compdir + "/" + l.trim();
            RandomAccessFile file;
            try {
                file = new RandomAccessFile(filename + "/META", "r");
            } catch (Exception ex) {
                continue;
            }
            byte [] data = new byte[(int)file.length()];
            file.readFully(data);
            MetaInfo s = new MetaInfo(filename, data);
            v.addElement(s);
            return;
        }
    }
}
