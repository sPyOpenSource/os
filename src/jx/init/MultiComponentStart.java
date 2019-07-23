package jx.init;

import jx.zero.*;
import jx.zero.debug.*;
import jx.bootrc.*;

import java.io.*;
import java.util.Vector;
import jx.devices.pci.PCIAccess;
import jx.devices.pci.PCIGod;
import jx.net.StartNetDevice;
import jx.net.protocols.StartNetworkProtocols;
import jx.netmanager.NetInit;
import jx.timer.TimerManager;
import timerpc.StartTimer;

public class MultiComponentStart {

    static class StartInfo {
	VMMethod method;
	String name;
	Object [] args;
    }

    static class MyOutputStream extends java.io.OutputStream {	
        @Override
	public void write(int b) throws IOException {
	    Debug.out.print("" + (char)b);
	}	
    }
    
    public static void init (final Naming naming, String []args, Object[] objectArgs) throws Exception {
	jx.zero.debug.DebugOutputStream out = new jx.zero.debug.DebugOutputStream((DebugChannel) naming.lookup("DebugChannel0"));
	Debug.out = new jx.zero.debug.DebugPrintStream(out);
	//System.out = new java.io.PrintStream(out);
	//System.err = System.out;
        Debug.out.println("init");
        PCIAccess bus = (PCIAccess) PCIGod.main(new String[]{}, naming);
        
        Debug.out.println("lookup PCI Access Point...");
	//PCIAccess bus = (PCIAccess)LookupHelper.waitUntilPortalAvailable(naming, "PCIAccess");
	//SleepManager sleepManager = new SleepManagerImpl();
	TimerManager timerManager = null;//(TimerManager)LookupHelper.waitUntilPortalAvailable(naming, "TimerManager");
        try {
            timerManager = (TimerManager) StartTimer.main(new String[]{"TimerManager"}, naming);
        } catch (Exception ex) {
            //Logger.getLogger(StartNetDevice.class.getName()).log(Level.SEVERE, null, ex);
        }
        NetInit.init(naming, new String[]{"NIC", "eth0", "8:0:6:28:63:40"}, timerManager, bus);
        //NetInit.init(new String[]{"NIC", "eth0", "8:0:6:28:63:40"});
        bioide.Main.main(new String[]{"TimerManager", "BIOFS_RW", "0", "1"});
        //StartNetDevice.main(new String[]{"NIC", "eth0", "8:0:6:28:63:40"});
        //StartNetworkProtocols.main(new String[]{"NIC", "TimerManager", "NET"});
	/*final CPUManager cpuManager = (CPUManager) naming.lookup("CPUManager");
	final ComponentManager componentManager = (ComponentManager) naming.lookup("ComponentManager");

	ComponentSpec[] componentSpec = (ComponentSpec[]) objectArgs[0];
	Vector start = new Vector();

        for (ComponentSpec componentSpec1 : componentSpec) {
            String initLib = componentSpec1.getString("InitLib");
            String startClass = componentSpec1.getString("StartClass");
            // optional parameters
            String[] argv = new String[]{};
            String schedulerClass = null;
            try {
                argv = componentSpec1.getStringArray("Args");
            } catch(NameNotFoundException e) {}
            StartInfo info = new StartInfo();
            info.name = startClass;
            info.args = new Object[]{argv};
            //int componentID = componentManager.load(initLib);
            try {
                String[] cname = componentSpec1.getStringArray("InheritThread");
                for (String cname1 : cname) {
                    componentManager.setInheritThread(cname1);
                }
            } catch(NameNotFoundException e) {}
            //cpuManager.executeClassConstructors(componentID);
            VMClass cl = cpuManager.getClass(startClass);
            if (cl == null) {
                throw new Error("Class " + startClass + " not found.");
            }
            final VMMethod[] methods = cl.getMethods();
            String name = "main";
            String signature = "([Ljava/lang/String;)V";
            for (VMMethod method : methods) {
                //Debug.out.println("M: "+methods[i].getName() + ", S: "+ methods[k].getSignature());
                if (name.equals(method.getName()) && signature.equals(method.getSignature())) {
                    info.method = method;
                    break;
                }
            }
            if (info.method == null) throw new Error("Method " + name + signature + " not found in class " + startClass);
            start.addElement(info);
        }

        
	for(int i = 0; i < start.size(); i++) {
	    final StartInfo info = (StartInfo)start.elementAt(i);
	    cpuManager.start(cpuManager.createCPUState(new ThreadEntry() {
                    @Override
		    public void run() {
			//Debug.out.println("START : " + info.name);
			cpuManager.setThreadName(info.name);
			info.method.invoke(null, info.args);
		    }
		}));
	}*/
    }
}
