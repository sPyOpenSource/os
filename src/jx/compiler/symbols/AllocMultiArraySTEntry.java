package jx.compiler.symbols;

import jx.zero.Debug;


public class AllocMultiArraySTEntry extends SymbolTableEntryBase {
   
    public AllocMultiArraySTEntry() {
    }
    
    public String getDescription() {
	return super.getDescription()+",AllocMultiArray";
    }
    
    public int getValue() {
	return 0;
    }
    
  public void apply(byte[] code, int codeBase) {
    //Debug.assert(isReadyForApply()); 
    myApplyValue(code, codeBase, getValue()); 
  }

    public String toGASFormat() {
	return "0x"+Integer.toHexString(getValue());
    }
  
}
  
  
