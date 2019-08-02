package java.lang;

import java.lang.invoke.MethodHandle;
import jx.zero.VMClass;
import jx.zero.VMMethod;

import jx.zero.CPUManager;
import jx.zero.InitialNaming;

import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public final class Class<T>
{
    static CPUManager cpuManager = (CPUManager) InitialNaming.getInitialNaming().lookup("CPUManager");

    VMClass vmclass;
    VMMethod methods[];

    private Class(VMClass cl) {
	//System.out.println("");
	this.vmclass = cl;
        classLoader = null;
    }
    
    public static Class forName(String className) throws ClassNotFoundException {
	VMClass cl = cpuManager.getClass(className);
	if (cl == null) throw new ClassNotFoundException();
	return new Class(cl);
    }
    
    @Override
    public String toString()
    {
	return (isInterface() ? "interface " : "class ") + getName();
    }

    public Class getComponentType() { throw new Error(); }
    
    public String getName() { return vmclass.getName(); }
    
    public boolean isInterface() { return false; }

    public boolean isInstance(Object o) { throw new Error("NOT IMPLEMENTED"); }
    public Method[] getDeclaredMethods()  { throw new Error("NOT IMPLEMENTED"); }

    public Class getSuperclass() { return null; }
    
    public Class[] getInterfaces() { return null; }
    
    /**
     * Returns the class loader for the class.  Some implementations may use
     * null to represent the bootstrap class loader. This method will return
     * null in such implementations if this class was loaded by the bootstrap
     * class loader.
     *
     * <p> If a security manager is present, and the caller's class loader is
     * not null and the caller's class loader is not the same as or an ancestor of
     * the class loader for the class whose class loader is requested, then
     * this method calls the security manager's {@code checkPermission}
     * method with a {@code RuntimePermission("getClassLoader")}
     * permission to ensure it's ok to access the class loader for the class.
     *
     * <p>If this object
     * represents a primitive type or void, null is returned.
     *
     * @return  the class loader that loaded the class or interface
     *          represented by this object.
     * @throws SecurityException
     *    if a security manager exists and its
     *    {@code checkPermission} method denies
     *    access to the class loader for the class.
     * @see java.lang.ClassLoader
     * @see SecurityManager#checkPermission
     * @see java.lang.RuntimePermission
     */
    //@CallerSensitive
    public ClassLoader getClassLoader() { 
        /*ClassLoader cl = getClassLoader0();
        if (cl == null)
            return null;
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            //ClassLoader.checkClassLoaderPermission(cl, Reflection.getCallerClass());
        }
        return cl;*/
        return classLoader;
    }
    
    // Initialized in JVM not by private constructor
    // This field is filtered from reflection access, i.e. getDeclaredField
    // will throw NoSuchFieldException
    private final ClassLoader classLoader;
    
    public Object newInstance() throws InstantiationException, IllegalAccessException{
	return vmclass.newInstance(); 
    }
    
    public static Class getPrimitiveClass(String cname) {
	return null ; 
    }

    public boolean isAssignableFrom(Class c) {
	throw new Error("NOT IMPLEMENTED");
    }

    public Constructor getConstructor(Class[] c) throws NoSuchMethodException {
	//	throw new Error("NOT IMPLEMENTED");
	return new Constructor(this, c);
    }

    /*
    public Method getMethod(String name, Class parameterTypes[]) throws NoSuchMethodException, SecurityException {
	if (methods == null) methods = vmclass.getMethods();
	for(int i=0; i<methods.length; i++) {
	    if (name.equals(methods[i].getName())) {
	    }
	}
	throw new NoSuchMethodException();
    }
    */

    public Method getDeclaredMethod(String writeReplace, Class[] classArgs) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean isArray() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean isPrimitive() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Method getMethod(String main, Class[] aClass) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Field[] getFields() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Field getField(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public boolean desiredAssertionStatus() {
        /*ClassLoader loader = getClassLoader();
        // If the loader is null this is a system class, so ask the VM
        if (loader == null)
            return desiredAssertionStatus0(this);

        // If the classloader has been initialized with the assertion
        // directives, ask it. Otherwise, ask the VM.
        synchronized(loader.assertionLock) {
            if (loader.classAssertionStatus != null) {
                return loader.desiredAssertionStatus(getName());
            }
        }
        return desiredAssertionStatus0(this);*/
        return true;
    }

    public Object cast(Object value) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public int getModifiers() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Field getDeclaredField(String rtype) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Iterable<Class<?>> getDeclaredClasses() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @SuppressWarnings("unchecked")
    public <U> Class<? extends U> asSubclass(Class<U> clazz) {
        if (clazz.isAssignableFrom(this))
            return (Class<? extends U>) this;
        else
            throw new ClassCastException(this.toString());
    }

    public MethodHandle getDeclaredMethod(String checkSpreadArgument, Class<Object> aClass, Class<Integer> aClass0) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public MethodHandle getDeclaredMethod(String guardWithCatch, Class<MethodHandle> aClass, Class<Class> aClass0, Class<MethodHandle> aClass1, Class<Object[]> aClass2) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String getSimpleName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    Object enumConstantDirectory() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    String getCanonicalName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
