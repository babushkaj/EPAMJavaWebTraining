package by.trainig.module2.service;

import by.trainig.module2.model.SentenceComposite;

import java.util.Comparator;

public class SentencesByWordsCountComparator implements Comparator<SentenceComposite> {
    boolean reverseOrder;

    public SentencesByWordsCountComparator(boolean reverseOrder) {
        this.reverseOrder = reverseOrder;
    }

    @Override
    public int compare(SentenceComposite o1, SentenceComposite o2) {
        return reverseOrder ? -(Integer.compare(o1.getTextLeaves().size(), o2.getTextLeaves().size())) :
                Integer.compare(o1.getTextLeaves().size(), o2.getTextLeaves().size());
    }
}
