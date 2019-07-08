package jx.compiler.vtable;

import jx.zero.Memory;
import jx.zero.Debug;

import jx.classfile.ClassSource;
import jx.classfile.MethodSource;

import jx.compiler.persistent.ExtendedDataOutputStream;
import jx.compiler.persistent.ExtendedDataInputStream;
import jx.compiler.ZipClasses;

import java.io.IOException;

import java.util.Hashtable;
import java.util.Vector;
import java.util.Enumeration;

/**
 * Experimental VTable generator
 */

class Main {
    static final boolean dumpAll = false;
    public static final int MAGIC_NUMBER = 0xaddedade;

    Hashtable classPool = new Hashtable();
    Hashtable classFinder = new Hashtable();
    Vector all = new Vector();
    InterfaceMethodsTable itable;
    Hashtable mtables = new Hashtable();
    Vector classList = new Vector();
    int originalClasses; // number of classes *not* imported from lib

    Main(Memory zipfilecontents, String[] args, ClassInfo objectClass) throws Exception {
	itable = new InterfaceMethodsTable(objectClass);    
	ZipClasses zip = new ZipClasses(zipfilecontents, true);

	Enumeration elements = zip.elements();
	while(elements.hasMoreElements()) {
	    ClassSource source = (ClassSource)elements.nextElement();
	    ClassInfo info = new ClassInfo();
	    info.data = source;
	    info.isInterface = source.isInterface();
	    info.className = source.getClassName();
	    System.out.println(info.className);
	    MethodSource [] m =  source.getMethods();
	    info.methods = new Method[m.length];
	    for(int i=0; i<m.length; i++) {
		info.methods[i] = new Method(info, m[i]);
	    }
	    info.indexInAll = all.size();
	    classFinder.put(info.className, info);
	    all.addElement(info);
	}
    }

    Main(Hashtable classFinder, Vector all, ClassInfo[] predefinedClasses, ExtendedDataInputStream[] oldTables, ClassInfo objectClass) throws Exception {
	this.classFinder = classFinder;
	this.all = all;
	itable = new InterfaceMethodsTable(objectClass);    
        for (ClassInfo predefinedClasse : predefinedClasses)
            addOldClass(predefinedClasse);
        for (ClassInfo predefinedClasse : predefinedClasses)
            predefinedClasse.adjustSuperClass(classFinder);
        for (ExtendedDataInputStream oldTable : oldTables) {
            //Debug.out.println("Reading table "+i);
            deserialize(oldTable);
        }
	
    }

    /**
     * deserialize method tables 
     */

    void process() throws Exception {
	ClassInfo info;
	//info = (ClassInfo)classFinder.get("java/lang/Object");
	///putIn(info, true, true);

	for(int i = 0; i < all.size(); i++) {
	    info = (ClassInfo)all.elementAt(i);
	    if (info == null) continue;
	    putIn(info, true, true, false);
	}

	for(int i = 0; i < all.size(); i++) {
	    info = (ClassInfo)all.elementAt(i);
	    if (info == null) continue;
	    putIn(info, true, false, false);
	}

	for(int i = 0; i < all.size(); i++) {
	    info = (ClassInfo)all.elementAt(i);
	    if (info == null) continue;
	    putIn(info, false, false, false);
	}
	
    }


    boolean putIn(ClassInfo info, boolean rejectInterfaces, boolean rejectImplements, boolean enforcePutin) throws Exception {
	if (!enforcePutin && all.elementAt(info.indexInAll) == null) return true; // already there
	if (rejectInterfaces && info.isInterface()) return false;
	String[] ifs = info.data.getInterfaceNames();
	if (rejectImplements && ifs.length > 0) return false;
	if (! info.className.equals("java/lang/Object")) {
	    String s = info.data.getSuperClassName();
	    //Debug.out.println("FINDING SUPERCLASS: "+s);
	    info.superClass = (ClassInfo)classFinder.get(s);
	    if (info.superClass == null)
		throw new Exception("no superclass " + s + " found for class " + info.className);
	    if (classPool.get(info.superClass.className) == null)
		if (! putIn(info.superClass, rejectInterfaces, rejectImplements,enforcePutin)) return false;
	}
        // now put in interfaces
        for (String if1 : ifs) {
            // override rejectInterfaces
            if (classPool.get(if1) == null) {
                ClassInfo inf = (ClassInfo) classFinder.get(if1);
                if (inf == null)
                    throw new Error("Interface needed but not found " + if1);
                if (! putIn(inf, false, rejectImplements,enforcePutin)) return false;
            }
        }
	/*Debug.out.println("putin: "+info.className);*/
	createMTable(info);
	addMTable(info);
	all.setElementAt(null, info.indexInAll);
	return true;
    }

    void addMTable(ClassInfo info) {
	mtables.put(info.className, info.mtable);

	classList.addElement(info);
	originalClasses++;
	classPool.put(info.className, info);
    }

    void createMTable(ClassInfo info) {
	if (dumpAll) Debug.out.println("  Creating mtable for "+info.className);
	if (info.isInterface()) { 
	    itable.add(info);
	    info.mtable = new InterfaceMethodTable(info, itable, classPool);
	    //Debug.out.println("IF: "+info.className);
	    //info.mtable.print();
	    return;
	}

	Vector mt = new Vector();
	Hashtable inherited = new Hashtable();
	// copy superclass mtable
	if (info.superClass != null) {
	    for(int i=0; i<info.superClass.mtable.length(); i++) {
		mt.addElement(info.superClass.mtable.getAt(i));
		if (info.superClass.mtable.getAt(i) != null) // mtable may have holes!
		    inherited.put(info.superClass.mtable.getAt(i).nameAndType, info.superClass.mtable.getAt(i));
	    }
	}
        /*
        for(int i=0; i<info.methods.length; i++) {
        if (itable.contains(info.methods[i])) {
        Debug.out.println("Method "+info.methods[i].nameAndType+" clashes.");
        }
        }
         */
        // override method or append method
        for (Method method : info.methods) {
            if (method.data.isStatic() || method.name.equals("<init>")) {
                continue;
            }
            Method im = (Method) inherited.get(method.nameAndType);
            inherited.put(method.nameAndType, method); // override/append in method finder
            if (im != null) {
                //Debug.out.println("Override "+info.methods[i].nameAndType);
                method.indices = (Vector)im.indices.clone();
                for (int j = 0; j<im.indices.size(); j++) {
                    // method may appear more than once in mtable
                    int index = ((Integer)im.indices.elementAt(j));
                    if (mt.size() <= index) mt.setSize(index+1);
                    mt.setElementAt(method, index);
                }
            } else {
                int index = mt.size();
                for(index = mt.size(); ! itable.isFree(index); index++);
                method.indices.addElement(index);
                mt.setSize(index+1);
                mt.setElementAt(method, index);
                itable.markOccupied(index);
            }
        }
	// add new interface methods
	String [] ifs = info.data.getInterfaceNames();
        for (String if1 : ifs) {
            ClassInfo ii = (ClassInfo) classPool.get(if1);
            if (dumpAll) Debug.out.println("   implements INTERFACE: " + ii.className);
            //if (ii.methods == null) {
            for(int j = 0; j < ii.mtable.length(); j++) {
                if(ii.mtable.getAt(j) == null) continue;
                if (dumpAll) Debug.out.println("METHOD " + j + ":" + ii.mtable.getAt(j).ifMethodIndex + ii.mtable.getAt(j).nameAndType);
                Method realM = (Method)inherited.get(ii.mtable.getAt(j).nameAndType);
                if(realM == null) {
                    System.out.println("METHOD NOT IMPLEMENTED: " + ii.mtable.getAt(j).nameAndType);
                    System.out.println("   IF: " + ii.className);
                    System.out.println("   CL: " + info.className);
                }
                /*
                if(ii.mtable.getAt(j).ifMethodIndex == 0) {
                System.out.println("ERROR: interface method index = 0");
                System.out.println("       at "+j);
                ii.mtable.print();
                throw new Error();
                }
                */
                if (mt.size() <= ii.mtable.getAt(j).ifMethodIndex)
                    mt.setSize(ii.mtable.getAt(j).ifMethodIndex+1);
                if (dumpAll) Debug.out.println("  INTERFACEMETHOD " + ii.mtable.getAt(j).nameAndType + " at " + ii.mtable.getAt(j).ifMethodIndex);
                mt.setElementAt(realM, ii.mtable.getAt(j).ifMethodIndex);
                realM.indices.addElement(ii.mtable.getAt(j).ifMethodIndex);
            }
            /* } else {
            for(int j=0; j<ii.methods.length; j++) {
            if (ii.methods[j].name.equals("<clinit>")) continue;
            Method realM = (Method)inherited.get(ii.methods[j].nameAndType);
            if(realM == null) {
            System.out.println("METHOD NOT IMPLEMENTED: "+ii.methods[j].nameAndType);
            System.out.println("   IF: "+ii.className);
            System.out.println("   CL: "+info.className);
            }
            if (mt.size() <= ii.methods[j].ifMethodIndex)
            mt.setSize(ii.methods[j].ifMethodIndex+1);
            mt.setElementAt(realM, ii.methods[j].ifMethodIndex);
            realM.indices.addElement(new Integer(ii.methods[j].ifMethodIndex));
            }
            }*/
        }

	info.mtable = new MethodTable(mt, info.className);
    }

    void printMTable(ClassInfo info) {
	info.mtable.print();
    }

    public MethodTable getMethodTable(String className) {
	return (MethodTable) mtables.get(className);
    }

    public void serialize(ExtendedDataOutputStream out) throws IOException {
	out.writeInt(MAGIC_NUMBER);
	out.writeInt(originalClasses);
	for(int i = 0; i < classList.size(); i++) {
	    ClassInfo info = (ClassInfo)classList.elementAt(i);
	    if (info.isOld) continue;
	    info.serialize(out);
	}
    }

    public void addOldClass(ClassInfo info) throws Exception {
	info.isOld = true;
	classFinder.put(info.className, info);
	//Debug.out.println("ADDCF: "+info.className);
	addMTable(info);	    
	//printMTable(info);
	originalClasses--; // this is an imported class, correct counter
	itable.addOld(info);
    }
    
    public void deserialize(ExtendedDataInputStream in) throws Exception {
	int magic = in.readInt();
	if (magic != MAGIC_NUMBER) {
	    throw new IOException("This is not a method table  " + magic);
	}
	int ntables = in.readInt();
	for(int i = 0; i < ntables; i++) {
	    ClassInfo info = new ClassInfo(in, classPool);
	    addOldClass(info);
	}
    }
}
