package p8t2;

import p8t1.P8T1;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;

public class P8T2 {

    public static void main(String[] args) {
        try (var in = new Scanner(System.in)) {
            System.out.print("Enter keyword (e.g. volatile): ");
            String keyword = in.nextLine();

            ExecutorService executor = Executors.newCachedThreadPool();

            Path targetDir = Path.of(P8T1.class.getClassLoader().getResource("src/").toURI());
            List<Callable<Path>> tasks = new ArrayList<>();

            // Traverse the directory and create tasks
            try (Stream<Path> files = Files.walk(targetDir)) {
                files.filter(Files::isRegularFile)
                        .filter(p -> p.toString().endsWith(".java"))
                        .forEach(file -> tasks.add(new SearchFileTask(file, keyword)));
            }

            try {
                Path result = executor.invokeAny(tasks);
                System.out.println("Found the first file that contains " + keyword + ": " + result);
            } catch (ExecutionException e) {
                System.out.println("No file found containing the keyword.");
            } finally {
                executor.shutdown();
            }

            if (executor instanceof ThreadPoolExecutor) {
                System.out.println("Largest pool size: " +
                        ((ThreadPoolExecutor) executor).getLargestPoolSize());
            }
        } catch (IOException | InterruptedException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
}

class SearchFileTask implements Callable<Path> {
    private final Path file;
    private final String keyword;

    public SearchFileTask(Path file, String keyword) {
        this.file = file;
        this.keyword = keyword;
    }

    @Override
    public Path call() throws Exception {
        try (Scanner scanner = new Scanner(file, StandardCharsets.UTF_8)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.contains(keyword)) {
                    return file;
                }

                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("Search in " + file + " canceled.");
                    return null;
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        throw new NoSuchElementException();
    }
}