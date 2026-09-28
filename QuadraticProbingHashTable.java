/**
 * INF2010 - ASD
 * Table de dispersement avec resolution des collisions par
 * sondage quadratique (Quadratic Probing Hash Table).
 * Ce code est basé sur Chapitre 5 de *Data Structures and Algorithms
 * Analysis in Java* (2e ed.) de Mark Allen Weiss, avec modifications
 * par Susanna Rumsey (2026).
 *
**/

public class QuadraticProbingHashTable<AnyType> extends ProbingHashTable<AnyType>{
    /**
     * Trouve la position de x dans la table (sondage quadratique).
     * Si x n'est pas present, retourne la position ou il devrait
     * etre insere (premiere cellule libre ou marquee supprimee).
     */
    protected int findPos(AnyType x) {
        int offset = 1;
        int currentPos = myhash(x);

        while (array[currentPos] != null &&
                !array[currentPos].element.equals(x)) {
            collisionCounter++;
            currentPos += offset; // ieme sondage : +1, +3, +5, ...
            offset += 2;
            if (currentPos >= array.length) {
                currentPos -= array.length;
            }
        }
        return currentPos;
    }

    /**
     * Petit programme de test / demonstration.
     */
    public static void main(String[] args) {
        QuadraticProbingHashTable<Integer> table = new QuadraticProbingHashTable<>();
        test(table);
    }
}
