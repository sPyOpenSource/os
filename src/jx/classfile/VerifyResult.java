package jx.classfile;


abstract public class VerifyResult extends jx.zero.classfile.VerifyResult {

    public static final int NPA_RESULT = 1;
    public static final int FLA_RESULT = 2;
    public static final int WCET_RESULT = 3;
    public static final int CINSTR_RESULT = 4;

    public VerifyResult(int type) {
	super(type);
    }
    
}
