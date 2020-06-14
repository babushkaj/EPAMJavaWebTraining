package by.trainig.module2.parser;

import by.trainig.module2.model.TextLeaf;

import java.util.List;

public abstract class TextPartParser {

    protected TextPartParser next;

    public void setNext(TextPartParser next) {
        this.next = next;
    }

    abstract List<? extends TextLeaf> parse(String text, long... par);

}
