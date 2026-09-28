/**
 * INF2010 - ASD
 * Super-classe pour tables de dispersement avec
 * resolution des collisions par sondage (Probing Hash Tables).
 * Ce code est basé sur Chapitre 5 de *Data Structures and Algorithms
 * Analysis in Java* (2e ed.) de Mark Allen Weiss, avec modifications
 * par Susanna Rumsey (2026).
 *
**/

abstract class ProbingHashTable<AnyType> extends HashTable<AnyType>{
  protected HashEntry<AnyType>[] array; // Le tableau d'elements
  
  abstract protected int findPos(AnyType x); //Type de sondage particulier (lineaire, quadratique, etc.)
  
  protected static class HashEntry<AnyType> {
      public AnyType element;   // l'element
      public boolean isActive;  // false si marque comme supprime

      public HashEntry(AnyType e) {
          this(e, true);
      }

      public HashEntry(AnyType e, boolean i) {
          element = e;
          isActive = i;
      }
  }
  
  public ProbingHashTable(int size) {
    allocateArray(size);
    makeEmpty();
  }
    
  public ProbingHashTable() {
    this(DEFAULT_TABLE_SIZE);
  }
  
  public int tableLength() {
    return array.length;
  }
  
  public boolean contains(AnyType x) {
      int currentPos = findPos(x);
      return isActive(currentPos);
  }
  

  public void makeEmpty() {
      currentSize = 0;
      for (int i = 0; i < array.length; i++) {
          array[i] = null;
      }
  }
  
  public void insert(AnyType x) {
      int currentPos = findPos(x);
      if (isActive(currentPos)) {
          return;
      }

      array[currentPos] = new HashEntry<>(x, true);

      // Rehash si le taux d'occupation depasse 50%
      if (++currentSize > array.length / 2) {
          rehash();
      }
  }
  
  @SuppressWarnings("unchecked")
  protected void allocateArray(int arraySize) {
      array = new HashEntry[nextPrime(arraySize)];
  }
  
  /**
   * Double (au moins) la taille de la table. Voir section 5.5.
   */
  protected void rehash() {
      rehashCounter++;
      HashEntry<AnyType>[] oldArray = array;

      allocateArray(2 * oldArray.length);
      currentSize = 0;

      for (HashEntry<AnyType> entry : oldArray) {
          if (entry != null && entry.isActive) {
              insert(entry.element);
      }
  }
  
  private boolean isActive(int currentPos) {
        return array[currentPos] != null && array[currentPos].isActive;
  }
  
  public void remove(AnyType x) {
      int currentPos = findPos(x);
      if (isActive(currentPos)) {
          array[currentPos].isActive = false;
      }
  }
}
