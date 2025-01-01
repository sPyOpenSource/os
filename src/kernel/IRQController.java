package kernel;

public abstract class IRQController {
  public abstract void init();
  public abstract void ackIRQ(int no);
}
