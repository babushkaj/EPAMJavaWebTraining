package by.training.module1.validator;

import by.training.module1.model.Format;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class LineValidatorFactory {

    private static final Logger LOGGER = LogManager.getLogger(LineValidatorFactory.class);

    public MainLineValidator getValidator(Format format) {
        switch (format) {
            case MP3:
                return new MP3LineValidator();
            case CDDA:
                return new CDDALineValidator();
            default: {
                LOGGER.fatal("Unknown format in LineValidatorFactory");
                throw new RuntimeException("Unknown format in LineValidatorFactory");
            }
        }
    }

}
