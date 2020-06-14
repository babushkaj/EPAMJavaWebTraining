package by.training.module3.parser;

public class StAXParserException extends ParserException {
    public StAXParserException() {
    }

    public StAXParserException(String message) {
        super(message);
    }

    public StAXParserException(String message, Throwable cause) {
        super(message, cause);
    }

    public StAXParserException(Throwable cause) {
        super(cause);
    }
}
