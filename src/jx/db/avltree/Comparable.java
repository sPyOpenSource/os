/***************************  Comparable.java  *******************************/


package jx.db.avltree;


/**   The interface declares a method by which the called object compares itself
 *    with the passed object.
 *    Das Interface deklariert eine Methode, anhand der sich das
 *    aufgerufene Objekt mit dem uebergebenen vergleicht.
 *    For every data type derived from Object, such a comparison class must be implemented.
 *    Fuer jeden von Object abgeleiteten Datentyp muss eine solche
 *    Vergleichsklasse implementiert werden.
 *    The method generates an error message if a is an object of a different
 *    class than this object.
 *    Die Methode erzeugt eine Fehlermeldung, wenn a ein Objekt einer anderen
 *    Klasse als dieses Objekt ist.
 *
 *    int compareTo(Comparable a)
 *         returns 0 if this == a / liefert  0, wenn this == a
 *         returns <0 if this <  a / liefert <0, wenn this <  a
 *         returns >0 if this >  a / liefert >0, wenn this >  a
 */
public interface Comparable {

    public int compareTo(Comparable a);
}
