import java.io.FileWriter;
import java.io.IOException;

public class Benchmark {

    public static void main(String[] args) throws IOException {
        int[] sizes = {
                100, 200, 300, 500,
                1000, 2000, 3000, 5000,
                10000, 20000, 30000, 50000,
                100000, 200000, 300000, 500000,
                1000000, 2000000, 5000000,
                10000000, 50000000,
                100000000
        };

        runBenchmark("SeparateChaining", sizes);
        runBenchmark("LinearProbing", sizes);
        runBenchmark("QuadraticProbing", sizes);
        runBenchmark("DoubleHashing", sizes);

        System.out.println("Tous les benchmarks sont termines.");
    }

    private static void runBenchmark(String name, int[] sizes) throws IOException {
        FileWriter writer = new FileWriter(name + "_results.csv");
        writer.write("Elements,TableSize,Rehash,TempsNs,FacteurCharge,Collisions\n");

        for (int nums : sizes) {
            try {
                HashTable<Integer> table = createTable(name);

                long startTime = System.nanoTime();

                int GAP = 37;
                int i = HashTable.MATRICULE % nums;
                for (int j = 0; j < nums; j++) {
                    i = (i + GAP) % nums;
                    table.insert(i);
                }

                long endTime = System.nanoTime();

                writer.write(nums + "," +
                        table.tableLength() + "," +
                        table.rehashCount() + "," +
                        (endTime - startTime) + "," +
                        table.loadFactor() + "," +
                        table.collisionCount() + "\n");
                writer.flush();

                System.out.println(name + " - " + nums + " elements: termine ("
                        + (endTime - startTime) / 1_000_000 + " ms).");

            } catch (OutOfMemoryError e) {
                System.out.println(name + " - " + nums + " elements: ECHEC (memoire insuffisante).");
                writer.write(nums + ",OOM,OOM,OOM,OOM,OOM\n");
                writer.flush();
                System.gc(); // essaie de liberer de la memoire avant de continuer
            }
        }

        writer.close();
    }

    private static HashTable<Integer> createTable(String name) {
        switch (name) {
            case "SeparateChaining": return new SeparateChainingHashTable<>();
            case "LinearProbing":    return new LinearProbingHashTable<>();
            case "QuadraticProbing": return new QuadraticProbingHashTable<>();
            case "DoubleHashing":    return new DoubleHashingHashTable<>();
            default: throw new IllegalArgumentException("Type inconnu: " + name);
        }
    }
}