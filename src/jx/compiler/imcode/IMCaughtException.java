
package jx.compiler.imcode; 

import jx.classfile.datatypes.*; 
import jx.compiler.*;
import jx.compiler.nativecode.*;

final public class IMCaughtException extends IMOperant {

    public IMCaughtException(CodeContainer container) {
	super(container);
	datatype = BCBasicDatatype.REFERENCE;
    }

    @Override
    public IMNode processStack(VirtualOperantenStack stack,IMBasicBlock basicBlock) {
	stack.push(this);
	return null;
    }

    @Override
    public String toReadableString() {
	return "<caught exception>";
    }

    @Override
    public int getNrRegs() { return 1; }

    // IMCaughtException
    @Override
    public void translate(Reg result) throws CompileException {
	regs.allocIntRegister(result,datatype);
	code.popl(result);
    }
}
