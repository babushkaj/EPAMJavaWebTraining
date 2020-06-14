package by.training.module1.model;

import java.util.Optional;
import java.util.stream.Stream;

public enum Style {
    POP, ROCK, RAP;

    public static Optional<Style> fromString(String type) {
        return Stream.of(Style.values())
                .filter(t -> t.name().equalsIgnoreCase(type))
                .findFirst();
    }

}
