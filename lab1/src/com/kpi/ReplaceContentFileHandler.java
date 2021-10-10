package com.kpi;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReplaceContentFileHandler implements FileHandler {

    private final Pattern pattern;

    public ReplaceContentFileHandler(Pattern pattern) {
        this.pattern = pattern;
    }

    @Override
    public void handle(Path path) {
        Charset charset = StandardCharsets.UTF_8;
        String content = null;

        try {
            content = Files.readString(path, charset);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Matcher matcher = pattern.matcher(Objects.requireNonNull(content));

        StringBuilder replacedContent = new StringBuilder();

        while (matcher.find()) {
            matcher.appendReplacement(replacedContent, matcher.group(0).replaceFirst(Pattern.quote(matcher.group(1)), "protected"));
        }

        matcher.appendTail(replacedContent);

        try {
            Files.writeString(path, replacedContent, charset);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
