package by.training.module1.model;

import java.util.Optional;
import java.util.stream.Stream;

public enum Compression {
    HIGH, MID, LOW;

    public static Optional<Compression> fromString(String type) {
        return Stream.of(Compression.values())
                .filter(t -> t.name().equalsIgnoreCase(type))
                .findFirst();
    }

}
