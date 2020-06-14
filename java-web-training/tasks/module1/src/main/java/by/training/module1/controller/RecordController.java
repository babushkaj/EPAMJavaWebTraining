
package by.training.module1.controller;

import by.training.module1.model.Format;
import by.training.module1.model.Record;
import by.training.module1.parser.LineParser;
import by.training.module1.service.RecordService;
import by.training.module1.validator.*;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class RecordController {

    private static final Logger LOGGER = LogManager.getLogger(RecordController.class);
    private RecordService recordService;

    private FormatValidator formatValidator;
    private FileValidator fileValidator;
    private LineValidatorFactory lineValidatorFactory;
    private DataFileReader dataFileReader;
    private LineParser lineParser;
    private RecordBuilderFactory factory;

    public RecordController(RecordService recordService, FormatValidator formatValidator, FileValidator fileValidator,
                            LineValidatorFactory lineValidatorFactory, DataFileReader dataFileReader, LineParser lineParser,
                            RecordBuilderFactory factory) {
        this.recordService = recordService;
        this.formatValidator = formatValidator;
        this.fileValidator = fileValidator;
        this.lineValidatorFactory = lineValidatorFactory;
        this.dataFileReader = dataFileReader;
        this.lineParser = lineParser;
        this.factory = factory;
    }

    public void processFile(String path) throws IOException {
        ValidationResult vr = fileValidator.validatePath(path);

        if (vr.isValid()) {
            List<String> allModelsInfo = dataFileReader.read(path);
            int stringNumber = 0;

            for (String oneModelInfo : allModelsInfo) {
                Map<String, String> modelMap = lineParser.parseString(oneModelInfo);
                vr = formatValidator.validate(modelMap);

                if(vr.isValid()){
                    Format format = Format.valueOf(modelMap.get("format").trim().toUpperCase());
                    LineValidator lineValidator = lineValidatorFactory.getValidator(format);
                    vr = lineValidator.validate(modelMap);

                    if (vr.isValid()) {
                        RecordBuilder recordBuilder = factory.getRecordBuilder(format);
                        Record record = recordBuilder.build(modelMap);
                        LOGGER.info(record.getFormat() + "Record has been successfully created from string No" +
                                ++stringNumber + ".");
                        recordService.save(record);
                    } else {
                       logErrors("Model cannot be created from string No" + ++stringNumber + ".", vr);
                    }

                } else {
                   logErrors("Model cannot be created from string No" + ++stringNumber + "." , vr);
                }
            }

        } else {
            logErrors("File with path\n\'" + path + "\' cannot be read.", vr);
            throw new IOException("File with path\n\'" + path + "\' cannot be read.");
        }
    }

    private void logErrors(String title, ValidationResult vr){
        Map<String, String> errors = vr.getResult();
        StringBuilder sb = new StringBuilder();
        sb.append(title + "\n");

        for (Map.Entry<String, String> entry : errors.entrySet()) {
            sb.append("\t" + entry.getValue() + "\n");
        }

        LOGGER.error(sb.toString());
    }
}


