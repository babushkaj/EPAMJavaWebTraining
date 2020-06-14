package by.training.module1.model;

import java.util.Optional;
import java.util.stream.Stream;

public enum Format {
    MP3, CDDA;

    public static Optional<Format> fromString(String type) {
        return Stream.of(Format.values())
                .filter(t -> t.name().equalsIgnoreCase(type))
                .findFirst();
    }

}
