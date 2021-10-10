package com.kpi;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

class FileVisitor {

    private final FileHandler handler;
    private final String fileExt;

    FileVisitor(FileHandler handler, String fileExt) {
        this.handler = handler;
        this.fileExt = fileExt;
    }

    public void bypassAndProceedFile(Path path) {
        if (path.toFile().isDirectory()) {
            try (DirectoryStream<Path> paths = Files.newDirectoryStream(path)) {

                for (Path p : paths) {
                    bypassAndProceedFile(p);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            if (path.toString().toLowerCase().endsWith(fileExt)) {

                System.out.println(path.getFileName());

                handler.handle(path);
            }
        }
    }
}
