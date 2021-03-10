package timerpc;

import jx.timer.TimerManager;

public class StartTimer {
    public static void main(String[] args) throws Exception {
	final TimerManager timerManager = new TimerManagerImpl();
	jx.InitialNaming.registerPortal(timerManager, args[0]);
    }
}
