package timerpc;

import jx.zero.*;
import jx.timer.TimerManager;

public class StartTimer {
    public static Portal main(String[] args, Naming naming) throws Exception {
	final TimerManager timerManager = new TimerManagerImpl();
	//Naming naming = InitialNaming.getInitialNaming();
	naming.registerPortal(timerManager, args[0]);
        return timerManager;
    }
}
