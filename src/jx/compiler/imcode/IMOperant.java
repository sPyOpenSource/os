
package jx.compiler.imcode; 

import jx.classfile.datatypes.*; 
import jx.compiler.*;

// ***** IMOperant *****

public class IMOperant extends IMNode {

    public IMOperant(CodeContainer container) {
	super(container);
    }

    public boolean isAdd() {return false;}
    
    public boolean isSub() {return false;}

    public boolean isMul() {return false;}
    
    public boolean isDouble() throws CompileException {
	if (datatype == -1) throw new CompileException("wrong or unkown datatype on stack");
	return BCBasicDatatype.isDouble(datatype);
    }

    public IMOperant getLeftOperant() {
	return null;
    }

    public IMOperant getRightOperant() {
	return null;
    }

    @Override
    public String toReadableString() {
	return "<operant>";
    }

    public boolean checkReference() {
	return true;
    }

    public boolean checkArrayRange(int index) {
	return true;
    }

    public boolean checkArrayRange(IMOperant index) {
	return true;
    }
}
