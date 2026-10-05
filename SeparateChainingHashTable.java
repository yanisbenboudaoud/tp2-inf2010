
/**
 * INF2010 - ASD
 * Table de dispersement avec resolution des collisions par
 * chainage (Separate Chaining Hash Table).
 * Ce code est basé sur Chapitre 5 de *Data Structures and Algorithms
 * Analysis in Java* (2e ed.) de Mark Allen Weiss, avec modifications
 * par Susanna Rumsey (2026).
 *
**/

import java.util.LinkedList;
import java.util.List;

public class SeparateChainingHashTable<AnyType> extends HashTable<AnyType> {
    protected List<AnyType>[] array;

    @SuppressWarnings("unchecked")
    public SeparateChainingHashTable(int size) {
        array = new LinkedList[nextPrime(size)];
        for (int i = 0; i < array.length; i++) {
            array[i] = new LinkedList<AnyType>();
        }
    }

    public SeparateChainingHashTable() {
        this(DEFAULT_TABLE_SIZE);
    }

    public int tableLength() {
        return array.length;
    }

    /**
     * Verifie si x est present dans la table.
     */
    public boolean contains(AnyType x) {
        List<AnyType> whichList = array[myhash(x)];
        return whichList.contains(x);
    }

    /**
     * Insere x dans la table s'il n'y est pas deja.
     */
    public void insert(AnyType x) {
        List<AnyType> whichList = array[myhash(x)];
        if (!whichList.contains(x)) {
            // Ajouté : une collision est comptée quand x atterrit dans une liste non vide
            if (!whichList.isEmpty()) {
                collisionCounter++;
            }
            whichList.add(x);

            // Rehash si le facteur de compression depasse 1
            if (++currentSize > array.length) {
                rehash();
            }
        }
    }

    /**
     * Retire x de la table s'il y est present.
     */
    public void remove(AnyType x) {
        List<AnyType> whichList = array[myhash(x)];
        if (whichList.contains(x)) {
            whichList.remove(x);
            currentSize--;
        }
    }

    /**
     * Vide completement la table.
     */
    public void makeEmpty() {
        for (int i = 0; i < array.length; i++) {
            array[i].clear();
        }
        currentSize = 0;
    }

    /**
     * Double (au moins) la taille de la table et reinsere tous les
     * elements actifs. Voir section 5.5 des diapositives (Fig 5.22).
     */
    @SuppressWarnings("unchecked")
    protected void rehash() {
        rehashCounter++; // Ajouté : comptabilise ce rehash
        List<AnyType>[] oldLists = array;

        // Nouvelle table de taille (au moins) le double, premiere
        array = new LinkedList[nextPrime(2 * oldLists.length)];
        for (int i = 0; i < array.length; i++) {
            array[i] = new LinkedList<AnyType>();
        }

        currentSize = 0;
        for (List<AnyType> list : oldLists) {
            for (AnyType item : list) {
                insert(item);
            }
        }
    }

    /**
     * Petit programme de test / demonstration.
     */
    public static void main(String[] args) {
        SeparateChainingHashTable<Integer> table = new SeparateChainingHashTable<>();
        test(table);
    }
}
