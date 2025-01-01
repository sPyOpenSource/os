package kernel.memory;

import rte.SClassDesc;

public abstract class MemIntf {
  public abstract Object allocate(int scalarSize, int relocEntries, SClassDesc type);
}
