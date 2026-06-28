/*****************************  AVLBaum.java  *********************************/

//import AlgoTools.IO;

/** An AVL tree is a search tree where all nodes are balanced.
 *  Ein AVLBaum ist ein SuchBaum, bei dem alle Knoten ausgeglichen
 *  sind. Das heisst, die Hoehe aller Teilbaeume unterscheidet sich
 *  maximal um eins.
 *  This means the height of all subtrees differs by at most one.
 */

package jx.db.avltree;


public class AVLBaum extends SuchBaum {

    private int balance;                 // Balance

    public AVLBaum() {                   // creates an empty AVL tree / erzeugt leeren AVLBaum

        balance = 0;
    }

    private static class Status {        // Inner class for passing status in recursion
        // Innere Klasse zur Uebergabe eines
        boolean unbal;                   // Status in der Rekursion
        // unbal is true when a child has grown during insertion
        Status () {                      // unbal ist true, wenn beim Einfue-
            unbal = false;               // gen ein Sohn groesser geworden ist
        }                                // Constructor of the inner class / Konstruktor der inneren Klasse
    }                                    // Element not yet inserted => no imbalance yet / Element noch nicht eingefuegt => noch keine Unausgeglichenheit

    public String toString() {           // for output: content(balance) / fuer Ausgabe: Inhalt(Balance)

        return inhalt + "(" + balance + ")";
    }

    public boolean insert(Comparable x) throws Exception {// inserts x into the AVL tree: true,
        // fuegt x in den AVLBaum ein: true,
        // if successful, false otherwise / wenn erfolgreich, sonst false.
        // Wraps the insertAVL function / Kapselt die Funktion insertAVL
        return insertAVL(x, new Status());
    }

    private boolean insertAVL(Comparable x, Status s) throws Exception { // Actual insertion method
        // Tatsaechliche Methode zum
        // (recursive) / Einfuegen (rekursiv)
        boolean eingefuegt;

        if (empty()) {                    // Leaf: Can insert here / Blatt: Hier kann eingefuegt werden
            inhalt = x;                  // Set content / Inhalt setzen
            links = new AVLBaum();      // New empty AVL tree on the left / Neuer leerer AVLBaum links
            rechts = new AVLBaum();      // New empty AVL tree on the right / Neuer leerer AVLBaum rechts
            s.unbal = true;              // This subtree has grown / Dieser Teilbaum wurde groesser
            return true;                 // Insertion successful and / Einfuegen erfolgreich und
        }                                // this subtree has grown / dieser Teilbaum groesser

        else if (((Comparable) value()).compareTo(x) == 0)   // Element already in AVL tree / Element schon im AVLBaum
            return false;

        else if (((Comparable) value()).compareTo(x) > 0) {   // Element x is smaller => / Element x ist kleiner =>
            eingefuegt = ((AVLBaum) left()).insertAVL(x, s);  // left subtree / linker Teilbaum

            if (s.unbal) {                // Left subtree has grown / Linker Teilbaum wurde groesser
                switch (balance) {
                    case 1:
                        // Old imbalance balanced / Alte Unausgeglichenheit ausgegl.
                        balance = 0;         // => new balance = 0 / => neue Balance = 0
                        s.unbal = false;     // Imbalance balanced / Unausgeglichenheit ausgeglichen
                        return true;
                    case 0:
                        // No rotation needed here yet / Hier noch kein Rotieren noetig
                        balance = -1;        // Balance is adjusted / Balance wird angeglichen
                        return true;
                    default:
                        // Rotation necessary / Rotieren notwendig

                        if (((AVLBaum) links).balance == -1)
                            rotateLL();
                        else
                            rotateLR();
                        s.unbal = false;     // Imbalance balanced / Unausgeglichenheit ausgeglichen
                        return true;         // => return value / => Rueckgabewert
// angleichen
                }
            }

        } else {                         // Element is greater => / Element ist groesser =>
            eingefuegt = ((AVLBaum) right()).insertAVL(x, s);// right subtree / rechter Teilbaum

            if (s.unbal) {                // Right subtree has grown / Rechter Teilbaum wurde groesser
                switch (balance) {
                    case -1:
                        // Old imbalance balanced / Alte Unausgeglichenheit ausgegl.
                        balance = 0;         // => new balance = 0 / => neue Balance = 0
                        s.unbal = false;     // Imbalance balanced / Unausgeglichenheit ausgeglichen
                        return true;
                    case 0:
                        // No rotation needed here yet / Hier noch kein Rotieren noetig
                        balance = 1;         // Balance is adjusted / Balance wird angeglichen
                        return true;
                    default:
                        // Rotation necessary / Rotieren notwendig

                        if (((AVLBaum) rechts).balance == 1)
                            rotateRR();
                        else
                            rotateRL();
                        s.unbal = false;     // Imbalance balanced / Unausgeglichenheit ausgeglichen
                        return true;         // => return value / => Rueckgabewert
// angleichen
                }
            }
        }
        return eingefuegt;               // No rotation => return result / Keine Rotation => Ergebnis zurueck
    }

    public void rotateLL() {

        //IO.println("LL-Rotation im Teilbaum mit Wurzel "+ inhalt);

        AVLBaum a1 = (AVLBaum) links;     // Remember left / Merke linken
        AVLBaum a2 = (AVLBaum) rechts;    // and right subtree / und rechten Teilbaum

        // Idea: Move content of a1 to the root / Idee: Inhalt von a1 in die Wurzel
        links = a1.links;                // Set new left child / Setze neuen linken Sohn
        rechts = a1;                     // Set new right child / Setze neuen rechten Sohn
        a1.links = a1.rechts;            // Set its left / Setze dessen linken
        a1.rechts = a2;                  // and right child / und rechten Sohn

        Object tmp = a1.inhalt;          // Content of right (==a1) / Inhalt von rechts (==a1)

        a1.inhalt = inhalt;              // is swapped with / wird mit Wurzel
        inhalt = tmp;                    // the root / getauscht

        ((AVLBaum) rechts).balance = 0;   // right subtree balanced / rechter Teilbaum balanciert
        balance = 0;                     // root balanced / Wurzel balanciert
    }

    public void rotateLR() {

        //IO.println("LR-Rotation im Teilbaum mit Wurzel "+ inhalt);

        AVLBaum a1 = (AVLBaum) links;     // Remember left / Merke linken
        AVLBaum a2 = (AVLBaum) a1.rechts; // and its right subtree / und dessen rechten Teilbaum

        // Idea: Move content of a2 to the root / Idee: Inhalt von a2 in die Wurzel
        a1.rechts = a2.links;            // Set children of a2 / Setze Soehne von a2
        a2.links = a2.rechts;
        a2.rechts = rechts;
        rechts = a2;                     // a2 becomes new right child / a2 wird neuer rechter Sohn

        Object tmp = inhalt;             // Content of right (==a2) / Inhalt von rechts (==a2)

        inhalt = rechts.inhalt;          // is swapped with / wird mit Wurzel
        rechts.inhalt = tmp;             // the root / getauscht

        if (a2.balance == 1)             // New balance for left child / Neue Bal. fuer linken Sohn
            ((AVLBaum) links).balance = -1;
        else
            ((AVLBaum) links).balance = 0;

        if (a2.balance == -1)            // New balance for right child / Neue Bal. fuer rechten Sohn
            ((AVLBaum) rechts).balance = 1;
        else
            ((AVLBaum) rechts).balance = 0;
        balance = 0;                     // root balanced / Wurzel balanciert
    }

    public void rotateRR() {

        //IO.println("RR-Rotation im Teilbaum mit Wurzel "+ inhalt);

        AVLBaum a1 = (AVLBaum) rechts;    // Remember right / Merke rechten
        AVLBaum a2 = (AVLBaum) links;     // and left subtree / und linken Teilbaum

        // Idea: Move content of a1 to the root / Idee: Inhalt von a1 in die Wurzel
        rechts = a1.rechts;              // Set new right child / Setze neuen rechten Sohn
        links = a1;                      // Set new left child / Setze neuen linken Sohn
        a1.rechts = a1.links;            // Set its right / Setze dessen rechten
        a1.links = a2;                  // and left child / und linken Sohn

        Object tmp = a1.inhalt;          // Content of left (==a1) / Inhalt von links (==a1)

        a1.inhalt = inhalt;              // is swapped with / wird mit Wurzel
        inhalt = tmp;                    // the root / getauscht

        ((AVLBaum) links).balance = 0;    // left subtree balanced / linker Teilbaum balanciert
        balance = 0;                     // root balanced / Wurzel balanciert
    }

    public void rotateRL() {

        //IO.println("RL-Rotation im Teilbaum mit Wurzel "+ inhalt);

        AVLBaum a1 = (AVLBaum) rechts;    // Remember right child / Merke rechten Sohn
        AVLBaum a2 = (AVLBaum) a1.links;  // and its left subtree / und dessen linken Teilbaum

        // Idea: Move content of a2 to the root / Idee: Inhalt von a2 in die Wurzel
        a1.links = a2.rechts;
        a2.rechts = a2.links;            // Set children of a2 / Setze Soehne von a2
        a2.links = links;
        links = a2;                      // a2 becomes new left child / a2 wird neuer linker Sohn

        Object tmp = inhalt;             // Content of left (==a2) / Inhalt von links (==a2)

        inhalt = links.inhalt;           // is swapped with / wird mit Wurzel
        links.inhalt = tmp;              // the root / getauscht

        if (a2.balance == -1)            // New balance for right child / Neue Bal. fuer rechten Sohn
            ((AVLBaum) rechts).balance = 1;
        else
            ((AVLBaum) rechts).balance = 0;

        if (a2.balance == 1)             // New balance for left child / Neue Bal. fuer linken Sohn
            ((AVLBaum) links).balance = -1;
        else
            ((AVLBaum) links).balance = 0;
        balance = 0;                     // root balanced / Wurzel balanciert
    }

    @Override
    public boolean delete(Comparable x) throws Exception {// deletes x from AVL tree: true,
        // loescht x im AVLBaum: true,
        // if successful, false otherwise / wenn erfolgreich, sonst false.
        // Wraps the deleteAVL function / Kapselt die Funktion deleteAVL
        return deleteAVL(x, new Status());
    }

    private boolean deleteAVL(Comparable x, Status s) throws Exception { // Actual deletion method
        // Tatsaechliche Methode
        // (recursive); true if successful / zum Loeschen (rekursiv); true, wenn erfolgreich
        boolean geloescht;               // true if deletion occurred / true, wenn geloescht wurde

        if (empty()) {                    // Leaf: Element not found / Blatt: Element nicht gefunden
            return false;                // => insertion unsuccessful / => Einfuegen erfolglos
        } else if (((Comparable) value()).compareTo(x) < 0) {     // Element x is greater => / Element x ist groesser =>
            // Continue searching right / Suche rechts weiter
            geloescht = ((AVLBaum) rechts).deleteAVL(x, s);
            if (s.unbal == true) balance2(s);       // Balance out if needed / Gleiche ggf. aus
            return geloescht;
        } else if (((Comparable) value()).compareTo(x) > 0) {     // Element x is smaller => / Element x ist kleiner =>
            // Continue searching left / Suche links weiter
            geloescht = ((AVLBaum) links).deleteAVL(x, s);
            if (s.unbal == true) balance1(s);       // Balance out if needed / Gleiche ggf. aus
            return geloescht;
        } else {                           // Element found / Element gefunden
            if (rechts.empty()) {        // No right child / Kein rechter Sohn
                inhalt = links.inhalt;   // replace node with left child / ersetze Knoten durch linken Sohn
                links = links.links;     // No left child anymore / Kein linker Sohn mehr
                balance = 0;             // Node is a leaf / Knoten ist Blatt
                s.unbal = true;          // Height has changed / Hoehe hat sich geaendert
            } else if (links.empty()) {  // No left child / Kein linker Sohn
                inhalt = rechts.inhalt;  // replace node with right child / ersetze Knoten durch rechten Sohn
                rechts = rechts.rechts;  // No right child anymore / Kein rechter Sohn mehr
                balance = 0;             // Node is a leaf / Knoten ist Blatt
                s.unbal = true;          // Height has changed / Hoehe hat sich geaendert
            } else {                     // Both children present / Beide Soehne vorhanden
                inhalt = ((AVLBaum) links).del(s);   // Call del() / Rufe del() auf
                if (s.unbal) {           // Balance out the imbalance / Gleiche Unbalance aus
                    balance1(s);
                }
            }
            return true;                 // Deletion successful / Loeschen erfolgreich
        }
    }

    private Object del(Status s) {       // Finds replacement for deleted object / Sucht Ersatz fuer gel. Objekt
        Object ersatz;                   // The replacement object / Das Ersatz-Objekt

        if (!rechts.empty()) {           // Find largest child in subtree / Suche groessten Sohn im Teilbaum
            ersatz = ((AVLBaum) rechts).del(s);
            if (s.unbal)                 // Balances out imbalance if needed / Gleicht ggf. Unbalance aus
                balance2(s);
        } else {                         // Swap with deleted node / Tausche mit geloeschtem Knoten
            ersatz = inhalt;             // Remember replacement and / Merke Ersatz und
            inhalt = links.inhalt;       // replace node with left child. / ersetze Knoten durch linken Sohn.
            links = links.links;         // No left child anymore / Kein linker Sohn mehr
            balance = 0;                 // Node is a leaf / Knoten ist Blatt
            s.unbal = true;              // Subtree has become shorter / Teilbaum wurde kuerzer
        }
        return ersatz;                   // Return replacement object / Gib Ersatz-Objekt zurueck
    }

    private void balance1(Status s) {    // Imbalance because left branch is shorter / Unbalance, weil linker Ast kuerzer
        switch (balance) {
            case -1:
                balance = 0;                 // Balance changed, not balanced / Balance geaendert, nicht ausgegl.
                break;
            case 0:
                balance = 1;                 // Balanced / Ausgeglichen
                s.unbal = false;
                break;
            default:
                // Balancing (rotation) necessary / Ausgleichen (Rotation) notwendig
                int b = ((AVLBaum) rechts).balance; //Remember balance of right child / Merke Balance des rechten Sohns
                if (b >= 0) {
                    rotateRR();
                    if (b == 0) {            // Adjust new balances / Gleiche neue Balancen an
                        balance = -1;
                        ((AVLBaum) links).balance = 1;
                        s.unbal = false;
                    }
                } else
                    rotateRL();
                break;
        }
    }

    private void balance2(Status s) {    // Imbalance because right branch is shorter / Unbalance, weil recht. Ast kuerzer
        switch (balance) {
            case 1:
                balance = 0;                 // Balance changed, not balanced / Balance geaendert, nicht ausgegl.
                break;
            case 0:
                balance = -1;                // Balanced / Ausgeglichen
                s.unbal = false;
                break;
            default:
                // Balancing (rotation) necessary / Ausgleichen (Rotation) notwendig
                int b = ((AVLBaum) links).balance; // Remember balance of left child / Merke Balance des linken Sohns
                if (b <= 0) {
                    rotateLL();
                    if (b == 0) {            // Adjust new balances / Gleiche neue Balancen an
                        balance = 1;
                        ((AVLBaum) rechts).balance = -1;
                        s.unbal = false;
                    }
                } else
                    rotateLR();
                break;
        }
    }
}
