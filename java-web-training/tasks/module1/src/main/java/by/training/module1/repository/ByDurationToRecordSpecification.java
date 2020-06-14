package by.training.module1.repository;

import by.training.module1.model.Record;

public class ByDurationToRecordSpecification implements RecordSpecification {
    private int to;

    public ByDurationToRecordSpecification(int to) {
        this.to = to;
    }

    @Override
    public boolean match(Record entity) {
        return entity.getDuration()<= to;
    }
}
