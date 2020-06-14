package by.training.module1.controller;

import by.training.module1.model.Record;

import java.util.Map;

public interface RecordBuilder {
    Record build(Map<String, String> oneRecordString);
}
