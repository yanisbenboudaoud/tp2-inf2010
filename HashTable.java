
/**
 * INF2010 - ASD
 * Classe de base pour Table de dispersement.
 * Ce code est basé sur Chapitre 5 de *Data Structures and Algorithms
 * Analysis in Java* (2e ed.) de Mark Allen Weiss, avec modifications
 * par Susanna Rumsey (2026).
 *
 */
import java.util.Scanner;

abstract class HashTable<AnyType> {
    public static int MATRICULE = 2472221; // Remplacer 1 avec votre matricule d'étudiant (7 chiffres)
    protected static final int DEFAULT_TABLE_SIZE = 11;
    protected int currentSize = 0;
    protected long collisionCounter = 0;
    protected long rehashCounter = 0;

    abstract public int tableLength();

    abstract public boolean contains(AnyType x);

    abstract public void insert(AnyType x);

    abstract public void remove(AnyType x);

    abstract public void makeEmpty();

    // Implémenté : facteur de charge = nombre d'éléments / taille de la table
    public double loadFactor() {
        return (double) size() / tableLength();
    }

    // Implémenté : retourne le compteur de rehash, incrémenté dans rehash()
    public long rehashCount() {
        return rehashCounter;
    }

    // Implémenté : retourne le compteur de collisions, incrémenté dans insert()/findPos()
    public long collisionCount() {
        return collisionCounter;
    }

    public int size() {
        return currentSize;
    }

    /**
     * Fonction de dispersement (Fig 5.7) : utilise hashCode() de
     * l'objet, ramene dans l'intervalle [0, array.length - 1].
     */
    protected int myhash(AnyType x) {
        int hashVal = x.hashCode();
        long length = tableLength();

        hashVal %= length;
        if (hashVal < 0) {
            hashVal += length;
        }

        return hashVal;
    }

    protected static int nextPrime(int n) {
        if (n % 2 == 0) {
            n++;
        }
        for (; !isPrime(n); n += 2) {
            // continue
        }
        return n;
    }

    protected static boolean isPrime(int n) {
        if (n == 2 || n == 3) {
            return true;
        }
        if (n == 1 || n % 2 == 0) {
            return false;
        }
        for (int i = 3; i * i <= n; i += 2) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }

    private void resetCollisionCount() {
        collisionCounter = 0;
    }

    @SuppressWarnings("unchecked")
    public static void test(HashTable table) {
        Scanner myObj = new Scanner(System.in);
        System.out.print("Nombre d'élèments à ajouter : ");
        int NUMS = myObj.nextInt();
        int GAP = 37;

        long startTime = System.nanoTime();

        System.out.println("Insertion de " + NUMS + " elements...");
        int progress = 0;

        int i = MATRICULE % NUMS;
        for (int j = 0; j < NUMS; j++) {
            i = (i + GAP) % NUMS;
            table.insert(i);
            // System.out.println("Insertion : " + i + " (" + j + ")");
            progress++;

            if (progress % (NUMS / 10) == 0) {
                System.out.println("\t" + progress + " inserés...");
            }
        }
        long endTime = System.nanoTime();
        // System.out.println("Taille de la table apres insertion : " +
        // table.tableLength());
        // System.out.println("Nombre de rehash : " + table.rehashCount());
        System.out.println("Temps total : " + (endTime - startTime) + " ns");
        // System.out.println("Facteur de compression final : " + table.loadFactor());
        // System.out.println("Nombre de collisions : " + table.collisionCount());

        i = MATRICULE % NUMS;
        for (int j = 0; j < NUMS; j++) {
            i = (i + GAP) % NUMS;
            if (!table.contains(i)) {
                System.out.println("Erreur, contains ne trouve pas " + i);
            }
        }

        for (int j = 0; j < NUMS; j++) {
            i = (i + GAP) % NUMS;
            table.remove(i);
        }

        for (int j = 0; j < NUMS; j++) {
            i = (i + GAP) % NUMS;
            if (table.contains(i)) {
                System.out.println("Erreur, contains trouve un element retire " + i);
            }
        }

        System.out.println("Test termine, taille finale : " + table.size());
    }
}
