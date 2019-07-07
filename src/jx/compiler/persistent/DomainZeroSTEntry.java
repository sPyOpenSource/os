package jx.compiler.persistent;


import jx.zero.Debug;

import jx.compiler.symbols.SymbolTableEntryBase; 

public class DomainZeroSTEntry extends SymbolTableEntryBase {
   
    public DomainZeroSTEntry() {}

    public String getDescription() {
	return super.getDescription()+",DomainZero";
    }

    public boolean isResolved() {return false;} 

    public void apply(byte[] code, int codeBase) {
	//Debug.assert(isReadyForApply()); 
	applyValue(code, codeBase, getValue()); 
    }
    
    public String toGASFormat() {
	return "$0x"+Integer.toHexString(getValue());
    }    
}
  
  
