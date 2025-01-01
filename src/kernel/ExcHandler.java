package kernel;

public abstract class ExcHandler {
  public abstract void handle(int no, boolean withError, int ec);
}
