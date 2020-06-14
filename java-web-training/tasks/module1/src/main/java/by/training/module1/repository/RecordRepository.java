package by.training.module1.repository;

import by.training.module1.model.Record;

import java.util.ArrayList;
import java.util.List;

public class RecordRepository{

    private List<Record> records;

    public RecordRepository() {
        this.records = new ArrayList<>();
    }

    public void add(Record entity) {
        this.records.add(entity);
    }

    public List<Record> find(RecordSpecification spec) {
        List<Record> chosen = new ArrayList<>();
        for (Record r: this.records){
            if(spec.match(r)){
                chosen.add(r);
            }
        }
        return chosen;
    }

    public List<Record> getAllRecords() {
        return new ArrayList<>(this.records);
    }

}
