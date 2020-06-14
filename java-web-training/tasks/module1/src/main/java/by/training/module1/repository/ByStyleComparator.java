package by.training.module1.repository;

import by.training.module1.model.Record;

import java.util.Comparator;

public class ByStyleComparator implements Comparator<Record> {
    @Override
    public int compare(Record o1, Record o2) {
        return o1.getStyle().name().compareTo(o2.getStyle().name());
    }
}
