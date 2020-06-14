package by.trainig.module2.controller;

import by.trainig.module2.parser.ParserChain;
import by.trainig.module2.validator.FileValidator;
import by.trainig.module2.validator.ValidationResult;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class TextController {
    private static final Logger LOGGER = LogManager.getLogger(TextController.class);

    private FileValidator fileValidator;
    private DataFileReader fileReader;
    private ParserChain parserChain;

    public TextController(FileValidator fileValidator, DataFileReader fileReader, ParserChain parserChain) {
        this.fileValidator = fileValidator;
        this.fileReader = fileReader;
        this.parserChain = parserChain;
    }

    public void handleFile(String path) throws ControllerValidationException, DataFileReaderException {
        ValidationResult vr = fileValidator.validate(path);

        if (!vr.isValid()) {
            LOGGER.error("File with path\n\'" + path + "\' cannot be read.\n" + vr.getErrorsAsString());
            throw new ControllerValidationException("File with path\n\'" + path + "\' cannot be read.");
        }

        String wholeText;
        wholeText = fileReader.read(path);

        parserChain.doChainParsing(wholeText);

    }

}
