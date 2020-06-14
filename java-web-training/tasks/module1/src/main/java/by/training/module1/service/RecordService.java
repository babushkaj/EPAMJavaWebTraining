package by.training.module1.service;

import by.training.module1.model.Record;
import by.training.module1.repository.RecordRepository;
import by.training.module1.repository.RecordSpecification;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RecordService {

    private RecordRepository recordsRepository;

    private static final Logger LOGGER = LogManager.getLogger(RecordService.class);

    public RecordService(RecordRepository recordsRepository) {
        this.recordsRepository = recordsRepository;
    }

    public void save(Record record){
        recordsRepository.add(record);
    }

    public List<Record> sortBy(Comparator<Record> comparator){
        List<Record> copyRecords = new ArrayList<>(recordsRepository.getAllRecords());
        copyRecords.sort(comparator);

        LOGGER.info("Records have been sorted by style.");
        return copyRecords;
    }

    public int getDuration(){
        int duration = 0;

        for (Record record: this.recordsRepository.getAllRecords()) {
            duration += record.getDuration();
        }

        LOGGER.info("Duration of all records is " + duration + "s.");
        return duration;
    }

    public List<Record> find(RecordSpecification specification){
        List<Record> chosenRecords = recordsRepository.find(specification);

        LOGGER.info(chosenRecords.size() + " records have been found by duration.");
        return chosenRecords;
    }
}
