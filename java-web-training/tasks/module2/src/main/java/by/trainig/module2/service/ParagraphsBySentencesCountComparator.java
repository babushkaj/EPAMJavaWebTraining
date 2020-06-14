package by.trainig.module2.service;

import by.trainig.module2.model.ParagraphComposite;

import java.util.Comparator;

public class ParagraphsBySentencesCountComparator implements Comparator<ParagraphComposite> {
    private boolean reverseOrder;

    public ParagraphsBySentencesCountComparator(boolean reverseOrder) {
        this.reverseOrder = reverseOrder;
    }

    @Override
    public int compare(ParagraphComposite o1, ParagraphComposite o2) {
        return reverseOrder ? -(Integer.compare(o1.getTextLeaves().size(), o2.getTextLeaves().size())) :
                Integer.compare(o1.getTextLeaves().size(), o2.getTextLeaves().size());
    }
}
