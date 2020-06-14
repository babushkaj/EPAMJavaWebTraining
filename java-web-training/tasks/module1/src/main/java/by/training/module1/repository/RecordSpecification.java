package by.training.module1.repository;

import by.training.module1.model.Record;

public interface RecordSpecification{

    boolean match(Record entity);

    default RecordSpecification and(RecordSpecification other) {
        return entity -> match(entity) && other.match(entity);
    }
}
