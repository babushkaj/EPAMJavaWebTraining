package by.training.module3.parser;

import java.util.List;

public interface Parser<T> {
    public List<T> parse(String xmlPath) throws ParserException;
}
