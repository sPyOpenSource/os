package devices;

public abstract class TextDriver {
  public int textCols;
  public int textLines;
  public int cursorX, cursorY;

  public abstract void setColor(int fg, int bg);
  public abstract void putChar(int x, int y, char c);
  public abstract void enableCursor(boolean on);
  public abstract void setCursor(int newX, int newY);
  public abstract void scroll();
  public abstract void cls();
}
