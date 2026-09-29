package student13;

import java.io.*;
import java.nio.file.*;
import java.util.Locale;
import java.util.regex.*;
import java.util.stream.Stream;

// //fichier d'automatisation de test généré avec l'intelligence générative (ChatGPT) pour l'Exercice 3.
// Base sur le BlackBoxRunner fourni par l'enseignante,

public class BlackBoxRunner {

        public static void main(String[] args) throws IOException {

                Locale.setDefault(Locale.US);

                int[] sizes = {
                                100, 200, 300, 500,
                                1000, 2000, 3000, 5000,
                                10000, 20000, 30000, 50000,
                                100000, 200000, 300000, 500000,
                                1000000, 2000000, 5000000,
                                10000000, 50000000,
                                100000000
                };

                runBenchmark(1, sizes);
                runBenchmark(2, sizes);
                runBenchmark(3, sizes);

                System.out.println("Tous les benchmarks sont termines.");
        }

        private static void runBenchmark(int mapNum, int[] sizes)
                        throws IOException {

                String name = "HashMap" + mapNum;
                String jarName = name + ".jar";

                Path jarPath = findJar(jarName);

                System.out.println(
                                "\n" + name
                                                + " trouve ici : "
                                                + jarPath.toAbsolutePath());

                FileWriter writer = new FileWriter(name + "_results.csv");

                writer.write(
                                "Elements,TableSize,Rehash,TempsNs,FacteurCharge,Collisions\n");

                for (int nums : sizes) {

                        try {

                                ProcessBuilder builder = new ProcessBuilder(
                                                "java",
                                                "-Xmx8G",
                                                "-jar",
                                                jarPath.getFileName().toString());

                                /*
                                 * Lance le .jar depuis le dossier
                                 * dans lequel il se trouve.
                                 *
                                 * Cela evite les problemes de chemin
                                 * si BlackBoxRunner est lance depuis
                                 * le dossier du projet au lieu de student13.
                                 */
                                builder.directory(
                                                jarPath.getParent().toFile());

                                /*
                                 * Combine les erreurs et la sortie normale
                                 * pour pouvoir lire les deux.
                                 */
                                builder.redirectErrorStream(true);

                                Process process = builder.start();

                                /*
                                 * Envoie le nombre d'elements
                                 * au programme .jar.
                                 */
                                try (PrintWriter stdin = new PrintWriter(
                                                process.getOutputStream())) {

                                        stdin.println(nums);
                                        stdin.flush();
                                }

                                /*
                                 * Recupere tout ce que
                                 * le programme .jar affiche.
                                 */
                                StringBuilder sb = new StringBuilder();

                                try (BufferedReader br = new BufferedReader(
                                                new InputStreamReader(
                                                                process.getInputStream()))) {

                                        String line;

                                        while ((line = br.readLine()) != null) {

                                                sb.append(line)
                                                                .append("\n");
                                        }
                                }

                                int exitCode = process.waitFor();

                                String output = sb.toString();

                                /*
                                 * Si le programme manque de memoire,
                                 * on l'indique dans le CSV comme
                                 * Benchmark.java le fait deja.
                                 */
                                if (output.contains(
                                                "OutOfMemoryError")) {

                                        System.out.println(
                                                        name
                                                                        + " - "
                                                                        + nums
                                                                        + " elements: ECHEC "
                                                                        + "(memoire insuffisante).");

                                        writer.write(
                                                        nums
                                                                        + ",OOM,OOM,OOM,OOM,OOM\n");

                                        writer.flush();

                                        continue;
                                }

                                /*
                                 * Si le .jar s'est arrete avec
                                 * une autre erreur.
                                 */
                                if (exitCode != 0) {

                                        throw new RuntimeException(
                                                        "Le .jar s'est termine avec le code "
                                                                        + exitCode
                                                                        + ". Sortie :\n"
                                                                        + output);
                                }

                                /*
                                 * Expressions regulieres fournies
                                 * par l'enseignante.
                                 */

                                int tableLength = Integer.parseInt(
                                                extract(
                                                                output,
                                                                "Taille de la table apres insertion : (\\d+)"));

                                int rehashCount = Integer.parseInt(
                                                extract(
                                                                output,
                                                                "Nombre de rehash : (\\d+)"));

                                long time = Long.parseLong(
                                                extract(
                                                                output,
                                                                "Temps total : (\\d+) ns"));

                                double loadFactor = Double.parseDouble(
                                                extract(
                                                                output,
                                                                "Facteur.*?: ([0-9.Ee+-]+)"));

                                int collisionCount = Integer.parseInt(
                                                extract(
                                                                output,
                                                                "Nombre de collisions : (\\d+)"));

                                /*
                                 * Ecrit les resultats dans le CSV.
                                 */
                                writer.write(
                                                nums + ","
                                                                + tableLength + ","
                                                                + rehashCount + ","
                                                                + time + ","
                                                                + loadFactor + ","
                                                                + collisionCount + "\n");

                                writer.flush();

                                System.out.println(
                                                name
                                                                + " - "
                                                                + nums
                                                                + " elements: termine ("
                                                                + time / 1_000_000
                                                                + " ms).");

                        } catch (InterruptedException e) {

                                Thread.currentThread()
                                                .interrupt();

                                writer.close();

                                throw new IOException(
                                                "Benchmark interrompu.",
                                                e);

                        } catch (Exception e) {

                                System.out.println(
                                                name
                                                                + " - "
                                                                + nums
                                                                + " elements: ECHEC.");

                                System.out.println(
                                                "Raison : "
                                                                + e.getMessage());

                                writer.write(
                                                nums
                                                                + ",ERROR,ERROR,ERROR,ERROR,ERROR\n");

                                writer.flush();
                        }
                }

                writer.close();
        }

        /*
         * Cherche le fichier .jar.
         *
         * Cela corrige le probleme que tu avais
         * ou Java ne trouvait pas les HashMapX.jar.
         */
        private static Path findJar(String jarName)
                        throws IOException {

                Path currentDirectory = Paths.get("")
                                .toAbsolutePath()
                                .normalize();

                /*
                 * 1. Cherche dans le dossier actuel.
                 */
                Path direct = currentDirectory.resolve(jarName);

                if (Files.isRegularFile(direct)) {

                        return direct;
                }

                /*
                 * 2. Cherche directement dans student13.
                 */
                Path student13 = currentDirectory
                                .resolve("student13")
                                .resolve(jarName);

                if (Files.isRegularFile(student13)) {

                        return student13;
                }

                /*
                 * 3. Cherche dans les sous-dossiers
                 * proches du projet.
                 */
                try (Stream<Path> paths = Files.walk(
                                currentDirectory,
                                3)) {

                        Path found = paths
                                        .filter(
                                                        Files::isRegularFile)
                                        .filter(
                                                        path -> path.getFileName()
                                                                        .toString()
                                                                        .equals(jarName))
                                        .findFirst()
                                        .orElse(null);

                        if (found != null) {

                                return found
                                                .toAbsolutePath()
                                                .normalize();
                        }
                }

                throw new FileNotFoundException(
                                "Impossible de trouver "
                                                + jarName
                                                + " a partir de "
                                                + currentDirectory
                                                + ". Place BlackBoxRunner.java "
                                                + "dans student13 avec les 3 .jar, "
                                                + "ou lance le programme depuis "
                                                + "le dossier qui contient student13.");
        }

        /*
         * Fonction fournie par l'enseignante.
         */
        private static String extract(
                        String text,
                        String regex) {

                Matcher m = Pattern.compile(regex)
                                .matcher(text);

                if (!m.find()) {

                        throw new RuntimeException(
                                        "Valeur introuvable : "
                                                        + regex
                                                        + "\nSortie recue :\n"
                                                        + text);
                }

                return m.group(1);
        }
}