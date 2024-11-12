package io.github.octcarp.sustech.cs209a.practice.p2t2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class P2T2 {
    public static void main(String[] args) {
        HashMap<String, Integer> freq = new HashMap<>();
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        try {
            File alice = new File(Objects.requireNonNull(classLoader.getResource("p2t2/alice.txt")).getFile());
            Scanner scanner = new Scanner(alice);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] words = line.split("\\W+");

                for (String word : words) {
                    word = word.toLowerCase();
                    freq.merge(word, 1, Integer::sum);
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.err.println(e);
            return;
        }

        List<Map.Entry<String, Integer>> tempList = new ArrayList<>(freq.entrySet());
        tempList.sort((o1, o2) -> o2.getValue() - o1.getValue());
        System.out.printf("%-7s : %7s\n", "Word", "count");

        Pattern pattern = Pattern.compile("\\w+");
        int iterationNum = 0;
        for (Map.Entry<String, Integer> entry : tempList) {
            Matcher matcher = pattern.matcher(entry.getKey());

            if (matcher.find()) {
                System.out.printf("%-7s : %7s\n", entry.getKey(), entry.getValue());
                if (++iterationNum >= 5) {
                    break;
                }
            }
        }
    }
}