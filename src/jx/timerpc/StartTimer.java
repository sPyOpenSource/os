package jx.timerpc;

import jx.zero.InitialNaming;
import jx.timer.TimerManager;

public class StartTimer {
    public static void main(String[] args) throws Exception {
	final TimerManager timerManager = new TimerManagerImpl();
	InitialNaming.getInitialNaming().registerPortal(timerManager, args[0]);
    }
}
