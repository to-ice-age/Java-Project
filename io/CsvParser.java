package edu.ccrm.io;

import java.io.*;
import java.nio.file.*;
import java.util.function.Function;
import java.util.stream.Stream;

public class CsvParser<T> {
    private final Path filePath;
    private final Function<String[], T> mapper;
    private final String delimiter;
    private final boolean hasHeader;

    public CsvParser(Path filePath, Function<String[], T> mapper, String delimiter, boolean hasHeader) {
        this.filePath = filePath;
        this.mapper = mapper;
        this.delimiter = delimiter;
        this.hasHeader = hasHeader;
    }

    public Stream<T> parse() throws IOException {
        Stream<String> lines = Files.lines(filePath);
        if (hasHeader) {
            lines = lines.skip(1);
        }
        return lines.map(line -> line.split(delimiter))
                   .map(mapper);
    }

    public void write(Stream<T> data, Function<T, String[]> extractor, String[] headers) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            if (headers != null && headers.length > 0) {
                writer.write(String.join(delimiter, headers));
                writer.newLine();
            }

            data.map(extractor)
                .map(fields -> String.join(delimiter, fields))
                .forEach(line -> {
                    try {
                        writer.write(line);
                        writer.newLine();
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                });
        }
    }

    public static <T> Builder<T> builder(Path filePath) {
        return new Builder<>(filePath);
    }

    public static class Builder<T> {
        private final Path filePath;
        private Function<String[], T> mapper;
        private String delimiter = ",";
        private boolean hasHeader = true;

        private Builder(Path filePath) {
            this.filePath = filePath;
        }

        public Builder<T> withMapper(Function<String[], T> mapper) {
            this.mapper = mapper;
            return this;
        }

        public Builder<T> withDelimiter(String delimiter) {
            this.delimiter = delimiter;
            return this;
        }

        public Builder<T> withHeader(boolean hasHeader) {
            this.hasHeader = hasHeader;
            return this;
        }

        public CsvParser<T> build() {
            if (mapper == null) {
                throw new IllegalStateException("Mapper function must be provided");
            }
            return new CsvParser<>(filePath, mapper, delimiter, hasHeader);
        }
    }
}