package com.kpi;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        FileHandler handler = new ReplaceContentFileHandler(Pattern.compile("[^\"](public).+[;|(]", Pattern.MULTILINE));

        FileVisitor visitor = new FileVisitor(handler, ".java");

        System.out.println("Enter directory absolute path: ");
        Path dir = Paths.get(scanner.nextLine());

        try (DirectoryStream<Path> paths = Files.newDirectoryStream(dir)) {
            ExecutorService executorService = Executors.newFixedThreadPool(10);
            CountDownLatch latch = new CountDownLatch(Objects.requireNonNull(dir.toFile().list()).length);
            for (Path p : paths) {
                executorService.submit(() -> {
                    visitor.bypassAndProceedFile(p);
                    latch.countDown();
                });
            }

            latch.await();
            executorService.shutdown();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}

