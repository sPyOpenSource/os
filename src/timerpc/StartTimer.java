package timerpc;

import jx.init.InitNaming;
import jx.zero.*;
import jx.timer.TimerManager;

public class StartTimer {
    public static void main(String[] args) throws Exception {
	final TimerManager timerManager = new TimerManagerImpl();
	Naming naming = InitialNaming.getInitialNaming();
	InitNaming.registerPortal(timerManager, args[0]);
    }
}
