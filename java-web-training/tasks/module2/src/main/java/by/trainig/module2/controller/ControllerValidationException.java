package by.trainig.module2.controller;

public class ControllerValidationException extends Exception {

    public ControllerValidationException() {
    }

    public ControllerValidationException(String message) {
        super(message);
    }

    public ControllerValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
