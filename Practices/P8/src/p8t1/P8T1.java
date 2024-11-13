package p8t1;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.time.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Stream;

public class P8T1 {

    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);

            System.out.println("Enter keyword (e.g. volatile): ");
            String keyword = scanner.nextLine();

            Path targetDir = Path.of(P8T1.class.getClassLoader().getResource("src/").toURI());

            List<Callable<Long>> tasks = new ArrayList<>();
            try (Stream<Path> paths = Files.walk(targetDir)) {
                paths.filter(p -> p.toString().endsWith(".java")).forEach(p -> tasks.add(new WordCountTask(p, keyword)));
            }

            int type = 0;
            ExecutorService executor = switch (type) {
                case 0 -> Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
                case 1 -> Executors.newCachedThreadPool();
                case 2 -> Executors.newSingleThreadExecutor();
                default -> null;
            };

            Instant startTime = Instant.now();

            try {
                List<Future<Long>> results = executor.invokeAll(tasks);
                long totalOccurrences = results.stream().mapToLong(future -> {
                    try {
                        return future.get();
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                        return 0;
                    }
                }).sum();

                Instant endTime = Instant.now();
                System.out.printf("Occurrences of '%s': %d%n", keyword, totalOccurrences);
                System.out.println("Time elapsed: " + Duration.between(startTime, endTime).toMillis() + " ms\n");

            } finally {
                executor.shutdown();
            }
        } catch (IOException | InterruptedException | URISyntaxException e) {
            e.printStackTrace();
        }

    }
}


class WordCountTask implements Callable<Long> {
    private final Path file;
    private final String keyword;

    public WordCountTask(Path file, String keyword) {
        this.file = file;
        this.keyword = keyword;
    }

    @Override
    public Long call() throws Exception {
        long count = 0;
        try (Scanner in = new Scanner(file, StandardCharsets.UTF_8)) {
            while (in.hasNextLine()) {
                String line = in.nextLine();
                if (line.contains(keyword)) {
                    count += countOccurrences(line, keyword);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return count;
    }

    private long countOccurrences(String line, String keyword) {
        long count = 0;
        int index = 0;
        while ((index = line.indexOf(keyword, index)) != -1) {
            count++;
            index += keyword.length();
        }
        return count;
    }
}