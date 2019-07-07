package jx.emulation;

import jx.zero.*;

public class ComponentManagerImpl implements ComponentManager {
    @Override
    public void registerLib(String name,Memory libcode) {
	throw new Error("registerLib not emulated");
    }
    @Override
    public int load(String name){throw new Error();}

    @Override
    public void setInheritThread(String classname) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
