/**
 * INF2010 - ASD
 * Table de dispersement avec resolution des collisions par
 * sondage linéaire (Linear Probing Hash Table).
 * Ce code est basé sur Chapitre 5 de *Data Structures and Algorithms
 * Analysis in Java* (2e ed.) de Mark Allen Weiss, avec modifications
 * par Susanna Rumsey (2026).
 *
 */
public class DoubleHashingHashTable<AnyType> extends ProbingHashTable<AnyType>{
    /**
     * TODO: À remplir en utilisant hashage double ou f(i) = i*myhash(x).  Astuce : examinez le code pour la
     * methode findPos dans QuadraticProbingHashTable pour commencer.
     */
    protected int findPos(AnyType x) {
        int offset = myhash(x);
        int currentPos = super.myhash(x);
        while (array[currentPos] != null &&
                !array[currentPos].element.equals(x)) {
            collisionCounter++;
            currentPos += offset; // saute par "offset" à chaque fois
            if (currentPos >= array.length) {
                currentPos -= array.length;
            }
        }
        return currentPos;
    }
    
    @Override
    protected int myhash(AnyType x) {
      if (MATRICULE == 0) {
        throw new ArithmeticException("Entrez votre matricule dans DoubleHashingHashTable.java avant de proceder.");
      }
      int hashVal = x.hashCode();
      int length = this.tableLength();
      int R = nextPrime(MATRICULE % length);
      while (R >= length){
        R -= length;
        R = nextPrime(R);
      }
      return R - (hashVal % R);
    }

    public static void main(String[] args) {
        DoubleHashingHashTable<Integer> table = new DoubleHashingHashTable<>();
        test(table);
    }
}
