/**
 * INF2010 - ASD
 * Table de dispersement avec resolution des collisions par
 * sondage linéaire (Linear Probing Hash Table).
 * Ce code est basé sur Chapitre 5 de *Data Structures and Algorithms
 * Analysis in Java* (2e ed.) de Mark Allen Weiss, avec modifications
 * par Susanna Rumsey (2026).
 *
**/

public class LinearProbingHashTable<AnyType> extends ProbingHashTable<AnyType>{
    /**
     * TODO: À remplir en utilisant sondage linéaire.  Astuce : examinez le code pour la
     * methode findPos dans QuadraticProbingHashTable pour commencer.
     */
    protected int findPos(AnyType x) {
            int currentPos = myhash(x);

            while (array[currentPos] != null &&
                    !array[currentPos].element.equals(x)) {
                currentPos++;
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
        LinearProbingHashTable<Integer> table = new LinearProbingHashTable<>();
        test(table);
    }
}
