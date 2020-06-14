package by.training.module1.controller;

import by.training.module1.model.Format;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class RecordBuilderFactory {

    private static final Logger LOGGER = LogManager.getLogger(RecordBuilderFactory.class);

    public RecordBuilder getRecordBuilder(Format format){
        switch (format){
            case CDDA: {
                return new CDDARecordBuilder();
            }
            case MP3: {
                return new MP3RecordBuilder();
            }
            default: {
                LOGGER.fatal("Unknown format in RecordBuilderFactory");
                throw new RuntimeException("Unknown format in RecordBuilderFactory");
            }
        }
    }

}
