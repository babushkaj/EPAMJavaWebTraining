package by.training.module3.parser;

public class SAXParserException extends ParserException {
    public SAXParserException() {
    }

    public SAXParserException(String message) {
        super(message);
    }

    public SAXParserException(String message, Throwable cause) {
        super(message, cause);
    }

    public SAXParserException(Throwable cause) {
        super(cause);
    }
}