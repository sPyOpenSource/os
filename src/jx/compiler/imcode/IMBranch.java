
package jx.compiler.imcode;

import jx.compiler.*;
// ***** IMBranch ***** 

public class IMBranch extends IMOperant {

    protected IMBasicBlock[] targets;

    public IMBranch(CodeContainer container) {
	super(container);
	tag    = tag | IMNode.BRANCH | IMNode.BB_END;
	targets = null;
    }

    public IMNode inlineCode(CodeVector iCode,int depth, boolean forceInline) throws CompileException {	
	return this;
    }


    public IMNode assignNewVars(CodeContainer newContainer,int slots[],IMOperant opr[],int retval,int bcPos) throws CompileException {
	bcPosition = bcPos;
	init(newContainer);

	return this;
    }

    public IMBasicBlock[] getTargets() {
	return targets;
    }
}
