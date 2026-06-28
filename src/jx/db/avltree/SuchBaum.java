/*****************************  SuchBaum.java  ********************************/

//import AlgoTools.IO;

/** Implementation of a binary search tree over Comparable objects.
 * Implementation eines binaeren Suchbaums ueber Comparable-Objekten.
 */

package jx.db.avltree;


public class SuchBaum extends Baum {

    // searches for x in the search tree: returns the search tree with x at the root, or empty if not found
    // sucht x im SuchBaum: liefert den SuchBaum mit x in der Wurzel, ggf. leer
    private SuchBaum find(jx.db.avltree.Comparable x) throws Exception {
        if (empty())                                 return this;
        if (((jx.db.avltree.Comparable) value()).compareTo(x) == 0)  return this;
        if (((jx.db.avltree.Comparable) value()).compareTo(x) > 0)  return
                ((SuchBaum) left()).find(x);
        else                                         return
                ((SuchBaum) right()).find(x);
    }

    /** Searches for x in the search tree: returns null if x was not found,
     * otherwise the Comparable object x.
     * Sucht x im SuchBaum: liefert null, wenn x nicht gefunden wurde,
     * sonst Comparable-Objekt x */
    public jx.db.avltree.Comparable lookup(jx.db.avltree.Comparable x) throws Exception {
        return (jx.db.avltree.Comparable) find(x).inhalt;
    }

    /** Inserts x into the search tree: returns true if successful, otherwise false.
     * fuegt x in SuchBaum ein: liefert true, wenn erfolgreich, sonst false.*/
    public boolean insert(jx.db.avltree.Comparable x) throws Exception {
        SuchBaum s = find(x);         // Search tree with x at the root or empty / SuchBaum mit x in der Wurzel oder leer

        if (s.empty()) { // if empty, i.e. x is not yet contained in the search tree:
            // wenn leer, d.h. x noch nicht im SuchBaum enthalten:
            s.inhalt = x;                  // set content to x / setzte Inhalt auf x
            s.links = new SuchBaum();     // new empty search tree on the left / neuer leerer SuchBaum links
            s.rechts = new SuchBaum();     // new empty search tree on the right / neuer leerer SuchBaum rechts
            return true;
        } else return false;
    }

    /** Deletes x from the search tree: returns true if successfully deleted,
     * otherwise false.
     * loescht x aus SuchBaum: liefert true, wenn erfolgreich geloescht,
     * sonst false */
    public boolean delete(jx.db.avltree.Comparable x) throws Exception {
        SuchBaum s = find(x);         // Search tree with x at the root or empty / SuchBaum mit x in der Wurzel oder leer
        SuchBaum ersatz;              // Replacement node / Ersatzknoten

        if (s.empty()) return false;  // if x not found: false / wenn x nicht gefunden: false
        else {                        // if x found / wenn x gefunden
            if (s.left().empty())  ersatz = (SuchBaum) s.right();
            else if (s.right().empty()) ersatz = (SuchBaum) s.left();
            else {                    // Node with x has two children / Knoten mit x hat zwei Soehne
                ersatz = ((SuchBaum) s.left()).findMax(); // Maximum in the left / Maximum im linken
                s.inhalt = ersatz.inhalt;                // replace content / ersetze Inhalt
                s = ersatz;                              // to be replaced / zu ersetzen
                ersatz = (SuchBaum) ersatz.left();        // Replacement: left / Ersatz: linker
            }
            s.inhalt = ersatz.inhalt; // replace components / ersetze die Komponenten
            s.links = ersatz.links;
            s.rechts = ersatz.rechts;
            return true;
        }
    }

    // finds the maximum in a non-empty search tree:
    // returns the search tree with the maximum at the root
    // findet im nichtleeren SuchBaum das Maximum:
    // liefert den SuchBaum mit dem Maximum in der Wurzel
    private SuchBaum findMax() throws Exception {
        SuchBaum hilf = this;

        while (!hilf.right().empty()) hilf = (SuchBaum) hilf.right();
        return hilf;     // the rightmost descendant of this / der rechteste Nachfahr von this
    }
}
