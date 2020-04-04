package jx.verifier.typecheck;

import jx.classfile.*;
import jx.classfile.constantpool.*;
import jx.classstore.ClassFinder;
import jx.verifier.*;

public class TypeCheck {
    static public void init(ClassFinder cFinder) {
	TCObjectTypes.setClassFinder(cFinder);
	TCInterfaceTypes.setClassFinder(cFinder);
    }

    
    static public void verifyMethod(MethodSource method, 
				    String className, 
				    ConstantPool cPool) throws VerifyException {
	
	MethodVerifier m = new MethodVerifier(method,
					      className,
					      cPool);
	m.setInitialState(new TCState(m.getCode().getFirst(), 
				      m.getMethod().getNumLocalVariables(),
				      m.getMethod().getNumStackSlots(),
				      m));

	m.runChecks();
    }

    static public void verify(ClassData classData,
			      ClassTree classTree) throws VerifyException {
	//FEHLER schner machen: lieber gleich von hier aus verifyMethod aufrufen.
	MethodData[] methods = classData.getMethodData();
	ClassTreeElement cte = classTree.findClassTreeElement(classData.getClassName());
        for (MethodData method : methods) {
            if (method.isFinal() && !cte.isSystemFinalMethod(method.getMethodName(), method.getMethodType())) {
                throw new VerifyException("Verify Error: final method " +
                        cte.getClassName() + "." + method.getMethodName() + " (" + method.getMethodType() + ") overridden!");
            }
        }
    }
}
