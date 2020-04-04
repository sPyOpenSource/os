package jx.verifier;

public class VerifierOptions {
    static final public String helpString = "Verifier Options:\n" +
				"[+/-typecheck] - enable/disable standard Java Verification (Default: true)\n"+
				"[+/-npa] - enable/disable Null Pointer Analysis (Default: false)\n"+
				"[+/-fla] - enable/disable check for final and leaf methods (Default: false)\n"+
				"[+/-wcet] - enable/disable Worst Case Execution Time Analysis (Default: false)\n"+
				"[+wcet: <Package>/<Class>.<Method> <MaxTime>] - enable WCETime Analysis for one Method\n"+
				"-debug - enable Debug Mode\n" +
				"-silent - enable silent mode (minimal outputs)\n"+
				"[-h/-?/--help] - Display this message";
		
    public boolean doTypecheck=true; //Std. Java Verification
    public boolean doNPA=false; //Null Pointer Analysis
    public boolean doFLA = false; //SystemFinal and Leaf Analysis
    public boolean doWCET = false; //WorstCase Execution Time Analysis
    public int debugMode = 1; //0 - silent(no outputs at all!), 1 normal, 2 verbose

    public String wcetMethodArg=null;
    public int WCETmaxTime = 0;

    public String[] parseArgs(String[] args) {
	String[] tmp = new String[args.length];
	int argscount = 0;
	
	//process every option and take out those that we know.
	for (int i =0; i< args.length; i++) {
            switch (args[i].toLowerCase()) {
                case "-typecheck":
                    doTypecheck = false;
                    break;
                case "+typecheck":
                    doTypecheck = true;
                    break;
                case "-npa":
                    doNPA = false;
                    break;
                case "+npa":
                    doNPA = true;
                    break;
                case "-fla":
                    doFLA = false;
                    break;
                case "+fla":
                    doFLA = true;
                    break;
                case "-wcet":
                    doWCET = false;
                    break;
                case "+wcet":
                    doWCET = true;
                    break;
                case "+wcet:":
                    doWCET = true;
                    i++;
                    wcetMethodArg = args[i];
                    i++;
                    WCETmaxTime = Integer.parseInt(args[i]);
                    break;
                case "-debug":
                    debugMode = 2;
                    break;
                case "-silent":
                    debugMode = 0;
                    break;
                case "-h":
                case "-?":
                case "--help":
                    System.out.println(helpString);
                    System.exit(0);
                default:
                    //unknown option --> leave untouched
                    tmp[argscount]=args[i];
                    argscount++;
                    break;
            }
	}

	//copy leftover arguments into String of appropriate length
	String[]ret = new String[argscount];
        System.arraycopy(tmp, 0, ret, 0, ret.length);
	return ret;	
    }
}
	

