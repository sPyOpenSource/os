package jx.compiler.persistent;

import jx.zero.Debug;

import jx.compiler.symbols.SymbolTableEntryBase; 

public class AllocObjectSTEntry extends SymbolTableEntryBase {   
    public AllocObjectSTEntry() {
    }
    
    public String getDescription() {
	return super.getDescription()+",ExceptionHandler";
    }
    
    public void apply(byte[] code, int codeBase) {
	//Debug.assert(isReadyForApply()); 
	myApplyValue(code, codeBase, getValue()); 
    }
    
    public String toGASFormat() {
	return "0x"+Integer.toHexString(getValue());
    }
}
  
  
