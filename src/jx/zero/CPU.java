package jx.zero;

public interface CPU extends Portal {
    /*
    public CPUInfo identify();

    public void disableCaching();

    public void cacheDisable(boolean cd);

    public void notWriteThrough(boolean nw);
    */

    /**
     * set the Memory Type Range Register (MTRR)
     * to make page uncachable
     */
    //  public void setUncachablePage(int address, int numPages);

    /**
     * Read Time Stamp Counter
     */
    //public long rdtsc();

    /**
     * Save current state into oldState object
     * and load state from newState object
     * @param oldState
     * @param newState
     */
    void load(CPUState oldState, CPUState newState);

    /**
     * Save current state into state object 
     * @param state
     */
    void save(CPUState state);
    void dump(CPUState state);

    /** Returns the ID of the current CPU-Objec
     * @return t*/
    int getID();
    @Override
    String toString();
}
