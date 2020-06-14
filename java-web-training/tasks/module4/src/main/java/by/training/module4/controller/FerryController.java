package by.training.module4.controller;

import by.training.module4.builder.FerryBuilder;
import by.training.module4.model.Ferry;
import by.training.module4.validator.FileValidator;
import by.training.module4.validator.ValidationResult;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;

public class FerryController {

    private static Logger LOGGER = Logger.getLogger(FerryController.class);

    private FileValidator fileValidator;
    private DataFileReader reader;
    private LineParser parser;
    private FerryBuilder ferryBuilder;

    public FerryController(FileValidator fileValidator, DataFileReader reader, LineParser parser, FerryBuilder ferryBuilder) {
        this.fileValidator = fileValidator;
        this.reader = reader;
        this.parser = parser;
        this.ferryBuilder = ferryBuilder;
    }

    public Ferry readFerry(String path) {
        ValidationResult vr = fileValidator.validate(path);

        if (!vr.isValid()) {
            LOGGER.error(vr.getErrorsAsString());
            throw new ControllerException(vr.getErrorsAsString());
        }

        List<String> carsStringList;
        try {
            carsStringList = reader.read(path);
        } catch (DataFileReaderException e) {
            throw new ControllerException("Reading file was unsuccessful.", e);
        }

        Map<String, String> ferryMap = parser.parseString(carsStringList.get(0));
        return ferryBuilder.build(ferryMap);

    }

}
