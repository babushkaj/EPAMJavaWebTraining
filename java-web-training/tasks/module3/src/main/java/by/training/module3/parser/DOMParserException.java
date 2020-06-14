package by.training.module3.parser;


public class DOMParserException extends ParserException {
    public DOMParserException() {
    }

    public DOMParserException(String message) {
        super(message);
    }

    public DOMParserException(String message, Throwable cause) {
        super(message, cause);
    }

    public DOMParserException(Throwable cause) {
        super(cause);
    }
}
