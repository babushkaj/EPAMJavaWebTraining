package by.training.hospital.dao;

public class NoConcreteEntityInDatabaseException extends Exception {
    public NoConcreteEntityInDatabaseException() {
    }

    public NoConcreteEntityInDatabaseException(String message) {
        super(message);
    }

    public NoConcreteEntityInDatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
