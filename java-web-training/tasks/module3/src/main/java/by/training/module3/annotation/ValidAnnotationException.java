package by.training.module3.annotation;

public class ValidAnnotationException extends RuntimeException {
    public ValidAnnotationException() {
    }

    public ValidAnnotationException(String message) {
        super(message);
    }

    public ValidAnnotationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidAnnotationException(Throwable cause) {
        super(cause);
    }
}
