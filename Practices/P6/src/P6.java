import java.io.File;
import java.io.IOException;

import java.net.URL;
import java.util.*;

import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import java.util.stream.Stream;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class P6 {
    public static void main(String[] args) {
        JavaCounter javaCounter = new JavaCounter();

        Scanner input = new Scanner(System.in);
        int op = 0;
        do {
            System.out.println("""
                    \nPlease select operation:
                    1. Print Java Files;
                    2. Print Class Files;
                    3. Print .java & .class compare;
                    0. Exit;
                    """);
            try {
                op = input.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Exception:" + e);
                input.nextLine();
            }

            switch (op) {
                case 1 -> javaCounter.printJavaFiles();
                case 2 -> javaCounter.printClassFiles();
                case 3 -> javaCounter.printInnerClasses();
                case 0 -> System.out.println("Exiting...");
                default -> System.out.println("Illegal option input!");
            }
        } while (op != 0);

        input.close();
    }
}

class JavaCounter {
    private List<String> javaFileList;
    private List<String> classFileList;
    private List<String> innerClassesList;
    private List<String> topClassesList;
    private List<String> javaWithoutClassList;
    private List<String> classWithoutJavaList;

    public JavaCounter() {
        readZipFile();
        readJarFile();
        partitionClasses();
        getJavaFilesWithoutClassFiles();
        getClassFilesWithoutJavaFiles();
    }

    void printJavaFiles() {
        System.out.println("In .zip: # of .java files in java.io/java.nio packages: " + javaFileList.size());
        javaFileList.forEach(System.out::println);
    }

    void printClassFiles() {
        System.out.println("In .jar: # of .class files in java.io/java.nio packages: " + classFileList.size());
        classFileList.forEach(System.out::println);
    }

    void printInnerClasses() {
        System.out.println("# of .class files for inner classes: " + innerClassesList.size());
        System.out.println("# of .java files with corresponding .class: "
                + (topClassesList.size() - classWithoutJavaList.size()));
        System.out.println();

        System.out.println("# of .java without its .class: " + javaWithoutClassList.size());
        javaWithoutClassList.forEach(System.out::println);
        System.out.println();

        System.out.println("# of .class without its .java: " + classWithoutJavaList.size());
        classWithoutJavaList.forEach(System.out::println);
    }


    private void partitionClasses() {
        Map<Boolean, List<String>> classifiedClasses = classFileList.parallelStream()
                .collect(Collectors.partitioningBy(classFile -> !classFile.contains("$")));
        innerClassesList = classifiedClasses.get(false);
        topClassesList = classifiedClasses.get(true);
    }

    private void getJavaFilesWithoutClassFiles() {
        javaWithoutClassList = javaFileList.stream()
                .filter(javaFile -> topClassesList.stream()
                        .noneMatch(classFile -> classFile.equals(javaFile.replace(".java", ".class"))))
                .toList();
    }

    private void getClassFilesWithoutJavaFiles() {
        classWithoutJavaList = topClassesList.stream()
                .filter(topClassFile -> javaFileList.stream()
                        .noneMatch(javaFile -> javaFile.equals(topClassFile.replace(".class", ".java"))))
                .toList();
    }

    private void readZipFile() {
        URL srcZipUrl = P6.class.getClassLoader().getResource("src.zip");
        File srcZipFile = new File(srcZipUrl.getFile());

        if (!srcZipFile.exists()) {
            System.out.println("src.zip file not found in " + srcZipUrl);
            return;
        }

        try (ZipFile zipFile = new ZipFile(srcZipFile)) {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            Stream<ZipEntry> entryStream = StreamSupport.stream(
                    ((Iterable<ZipEntry>) () -> (Iterator<ZipEntry>) entries).spliterator(), true);

            this.javaFileList = entryStream
                    .parallel()
                    .filter(entry -> !entry.isDirectory())
                    .map(ZipEntry::getName)
                    .filter(name -> (name.startsWith("java/io/") || name.startsWith("java/nio/"))
                            && name.endsWith(".java"))
                    .toList();

        } catch (IOException e) {
            System.err.println("Error reading src.zip file: " + e.getMessage());
        }
    }

    private void readJarFile() {
        URL rtJarUrl = P6.class.getClassLoader().getResource("rt.jar");
        File rtJarFile = new File(rtJarUrl.getFile());

        if (!rtJarFile.exists()) {
            System.out.println("rt.jar file not found in " + rtJarUrl);
            return;
        }

        try (JarFile jarFile = new JarFile(rtJarFile)) {
            Enumeration<JarEntry> entries = jarFile.entries();
            Stream<JarEntry> entryStream = StreamSupport.stream(
                    ((Iterable<JarEntry>) () -> (Iterator<JarEntry>) entries).spliterator(), true);

            this.classFileList = entryStream
                    .parallel()
                    .filter(entry -> !entry.isDirectory())
                    .map(ZipEntry::getName)
                    .filter(name -> (name.startsWith("java/io/") || name.startsWith("java/nio/"))
                            && name.endsWith(".class"))
                    .toList();
        } catch (IOException e) {
            System.err.println("Error reading rt.jar file: " + e.getMessage());
        }
    }
}