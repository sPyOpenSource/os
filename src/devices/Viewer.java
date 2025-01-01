package devices;

public class Viewer {
  private TextDriver disp;

  public Viewer(TextDriver output) {
    disp=output;
  }
  
  public void cls() {
    setColor(0x07, 0x00);
    disp.cls();
    setCursor(0, 0);
  }
  
  public void setColor(int fg, int bg) {
    disp.setColor(fg, bg);
  }
  
  public void setCursor(int x, int y) {
    disp.setCursor(x, y);
  }
  
  public void print(char c) {  
    if (c=='\n') {
      println();
      return;
    }
    if (c=='\u0008') {
      if (disp.cursorX<=0) return;
      disp.setCursor(disp.cursorX-1, disp.cursorY);
      disp.putChar(disp.cursorX, disp.cursorY, ' ');
      return;
    }
    if (c=='\t') {
      do { print(' '); } while ((disp.cursorX&1)!=0);
      return;
    }
    disp.putChar(disp.cursorX, disp.cursorY, c);
    disp.cursorX++;
    
    if (disp.cursorX>=disp.textCols) {
      disp.cursorY++;
      disp.cursorX=0;
    }
    if (disp.cursorY>=disp.textLines) println();
    disp.setCursor(disp.cursorX, disp.cursorY);
  }

  private void doWriteInt(int x) {
    if (x>0) {
      doWriteInt(x/10);
      print((char)(48+x%10));
    }
  }

  private void doWriteLong(long x) {
    if (x>0l) {
      doWriteLong(x/10l);
      print((char)(48+(int)(x%10l)));
    }
  }
  
  public void print(int x) {
    if (x==0) {
      print('0');
      return;
    }
    if (x<0) {
      print('-');
      x=(-x);
    }
    doWriteInt(x);
  }
  
  public void print(long x) {
    if (x==0l) {
      print('0');
      return;
    }
    if (x<0l) {
      print('-');
      x=(-x);
    }
    doWriteLong(x);
  }

  public void print(String str) {
    int i;
    if (str==null) { print("<null>"); return; }
    for (i=0; i<str.count; i++) print(str.value[i]);
  }
  
  public void println() {
    disp.cursorX=0;
    if (++disp.cursorY>=disp.textLines) {
      disp.cursorY=disp.textLines-1;
      disp.scroll();
    }
    disp.setCursor(disp.cursorX, disp.cursorY);
  }

  public void println(char c) {
    print(c);
    println();
  }

  public void println(int i) {
    print(i);
    println();
  }

  public void println(long l) {
    print(l);
    println();
  }

  public void println(String str) {
    print(str);
    println();
  }
}
