package jx.compiler;

import jx.emulation.MemoryManagerImpl;
import jx.zero.MemoryManager;
import jx.zero.Memory;
import jx.zero.Debug;

import jx.compiler.persistent.*;
import jx.compiler.execenv.IOSystem;

import java.io.*;
import java.util.Vector;
import java.util.StringTokenizer;
import jx.compspec.MetaInfo;
import jx.compspec.MetaReader;
import static jx.compspec.StartBuilder.getCompilerOptions;

public class CompileNative {

    static MemoryManager memMgr = new MemoryManagerImpl();

    public static void readZipFile(String filename) {
	try {
            byte[] data;
            try (RandomAccessFile file = new RandomAccessFile(filename, "r")) {
                data = new byte[(int)file.length()];
                file.readFully(data);
            }
            Memory m = memMgr.alloc(data.length);
            m.copyFromByteArray(data, 0, 0, data.length);
	} catch(IOException e) {
	    e.printStackTrace();
	}
    }

    public static Memory getZIP(String filename) {
        if (filename.equals("/home/spy/OS/jx/libs/jdk0.zip")) return null;
	try {
	    RandomAccessFile file = new RandomAccessFile(filename, "r");
	    byte [] data = new byte[(int)file.length()];
	    file.readFully(data);
	    file.close();
	    Memory m = memMgr.alloc(data.length);
	    m.copyFromByteArray(data, 0, 0, data.length);
	    return m;
	} catch(IOException e) {
	    Debug.throwError("could not read classes.zip file: " + filename);
	    return null;
	}
    }   

    static Vector parsePath(String path) {
	Vector paths = new Vector();
	StringTokenizer tk = new StringTokenizer(path, ":");
	while (tk.hasMoreTokens()) {
	    paths.addElement(tk.nextToken());
	}
	return paths;
    }

    public static void main(String[] args) throws Exception {
	jx.emulation.Init.init();
        String[] a = {"/home/spy/OS/jx/libs/"};
        MetaReader metaReader = new MetaReader(a);
        Vector metas = new Vector();
        metaReader.addMeta(metas, "init2");
        //metaReader.addMeta(metas, "jdk0");
        MetaInfo s = (MetaInfo)metas.elementAt(0); // process this component
	String libdir = "/home/spy/OS/jx/libs/";
	if (!libdir.endsWith("/")) libdir = libdir + "/";

	String zipname = libdir + s.getComponentName() + ".zip";
	String jllname = libdir + s.getComponentName() + ".jll";

	Vector libs = new Vector();
	Vector jlns = new Vector();

	String[] neededLibs = "zero jdk0 zero_misc".split(" ");
        //String[] neededLibs = "zero".split(" ");
        for (String neededLib : neededLibs) {
            libs.addElement(libdir + neededLib + ".zip");
            jlns.addElement(libdir + neededLib + ".jln");
        }

	String jlnname = libdir + s.getComponentName() + ".jln";
	//CompilerOptionsNative opts = new CompilerOptionsNative(args);
        CompilerOptions opts = getCompilerOptions(libs, jlns, zipname, jlnname, jllname, libdir + "../JC_CONFIG");
	compile(opts);
    }

    final public static void compile(CompilerOptions opts) throws Exception {
	compile(null, opts);
    }

    public static void compile(String path, CompilerOptions opts) throws Exception {

	if (Debug.out == null) jx.emulation.Init.init();

	System.out.println("Native code compiler version 0.7.10-" + StaticCompiler.version());

	ExtendedDataOutputStream codeFile = null;
	ExtendedDataOutputStream tableOut = null;       
	if (opts.doDebug()) Debug.out.println("Compiling domain to " + opts.getOutputFile());       
	if (opts.doDebug()) Debug.out.println("Writing linker output to " + opts.getLinkerOutputFile());	    
	codeFile = new ExtendedDataOutputStream(new BufferedOutputStream(new FileOutputStream(opts.getOutputFile())));
	tableOut = new ExtendedDataOutputStream(new BufferedOutputStream(new FileOutputStream(opts.getLinkerOutputFile())));
	
	if (opts.doDebug()) Debug.out.println("Reading domain classes from " + opts.getClassFile());


	Memory domClasses = getZIP(opts.getClassFile());
        System.out.println(opts.getLibs());
	Memory[] libClasses = getZIPs(opts.getLibs());

	ExtendedDataInputStream[] tableIn = null;
	Vector links = opts.getLibsLinkerInfo();
	if (links != null) {
	   tableIn = new ExtendedDataInputStream[links.size()];
	   for(int i = 0; i < links.size(); i++) {
	       if (opts.doDebug()) Debug.out.println("Reading lib linkerinfo from " + (String)links.elementAt(i));
	       tableIn[i] = new ExtendedDataInputStream(new BufferedInputStream(new FileInputStream((String)links.elementAt(i))));
	   }
	} else {
	    tableIn = new ExtendedDataInputStream[0];
	}
	
	IOSystem io = new IOSystem() {
		private String path;
                @Override
		public OutputStream getOutputStream(String filename) throws IOException {
		    return new FileOutputStream(path + "/" + filename);
		}
                @Override
		public void set(String path) {this.path = path;}
	    };

	//io.set(path);
	io.set("/home/spy/OS/jx/libs/jdk0");
	StaticCompiler compiler = new StaticCompiler(null, codeFile, tableOut,
						     domClasses,
						     libClasses, tableIn,
						     opts, io);

        // release resources
        for (ExtendedDataInputStream tableIn1 : tableIn)
            tableIn1.close();
	codeFile.close();
	tableOut.close();
	
    }

    public static Memory[] getZIPs(Vector libs) {
	Memory[] libClasses = null;
	if (libs != null) {
	    libClasses = new Memory[libs.size()];
	    for(int i = 0; i < libs.size(); i++) {
		//if (opts.doDebug()) Debug.out.println("Reading lib classes from "+(String)libs.elementAt(i));
		libClasses[i] = getZIP((String)libs.elementAt(i));
	    }
	} else {
	    libClasses = new Memory[0];
	}
	return libClasses;
    }
}
