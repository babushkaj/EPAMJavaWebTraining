package by.training.module1.repository;

import by.training.module1.model.Record;

public class ByDurationFromRecordSpecification implements RecordSpecification {

    private int from;

    public ByDurationFromRecordSpecification(int from) {
        this.from = from;
    }

    @Override
    public boolean match(Record entity) {
        return entity.getDuration()>= from;
    }
}
