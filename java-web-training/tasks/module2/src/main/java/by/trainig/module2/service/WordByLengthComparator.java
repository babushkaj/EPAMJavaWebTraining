package by.trainig.module2.service;

import by.trainig.module2.model.WordLeaf;

import java.util.Comparator;

public class WordByLengthComparator implements Comparator<WordLeaf> {

    private boolean reverseOrder;

    public WordByLengthComparator(boolean reverseOrder) {
        this.reverseOrder = reverseOrder;
    }

    @Override
    public int compare(WordLeaf o1, WordLeaf o2) {
        return reverseOrder ? -(Integer.compare(o1.getWord().length(), o2.getWord().length())) :
                Integer.compare(o1.getWord().length(), o2.getWord().length());
    }
}
