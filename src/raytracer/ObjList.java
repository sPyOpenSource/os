package raytracer;

public class ObjList {
  public Object elem;
  public ObjList next;

  public ObjList() {
    elem = null;
    next = null;
  }

  public ObjList(Object obj) {
    elem = obj;
    next = null;
  }

  public void addElement(Object obj) {
    ObjList myList;

    if (elem == null) {
      elem = obj;
      // next=null; //is already initialized
    }
    else {
      myList = this;
      while (myList.next != null)
        myList = myList.next;
      myList.next = new ObjList(obj);
    }
  }
}