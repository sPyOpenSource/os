package java.io;

public class FilterOutputStream extends OutputStream {
  protected OutputStream out;

  public FilterOutputStream(OutputStream o) {
    this.out = o;
  }

  @Override
  public void write(int a) throws IOException {out.write(a);}
}
