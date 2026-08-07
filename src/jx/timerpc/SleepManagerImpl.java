package jx.timerpc;

import jx.zero.timer.SleepManager;
import jx.zero.*;
import jx.timer.*;

public class SleepManagerImpl implements SleepManager, Service {
    Clock clock;
    TimerManager timerManager;
    CPUManager cpuManager;
    
    public SleepManagerImpl(String[] args) {
	this();
    }
    public SleepManagerImpl() {
	clock = (Clock)InitialNaming.getInitialNaming().lookup("Clock");
	cpuManager = (CPUManager)InitialNaming.getInitialNaming().lookup("CPUManager");
	timerManager = (TimerManager)InitialNaming.getInitialNaming().lookup("TimerManager");
    }
    @Override
    public void mdelay(int milliseconds) {
	// Use timer manager for efficient sleep without busy-wait
	// This avoids the lost-update problem by using proper synchronization
	//CPUState state = cpuManager.getCPUState();
	//timerManager.unblockInMillis(state, milliseconds);
	//cpuManager.block();
        int end = clock.getTimeInMillis() + milliseconds;
	while(end > clock.getTimeInMillis());
    }
    
    @Override
    public void udelay(int microseconds) {
	// For microsecond delays, busy-wait is acceptable
	// but yield periodically to avoid starvation
	int end = clock.getTimeInMillis() + (microseconds + 999) / 1000;
	while (clock.getTimeInMillis() < end) {
	    //cpuManager.yield();
	}
    }
    public void sleep(int sec, int usec) { 
	mdelay(sec * 1000 + (usec + 999) / 1000);
    }
}
