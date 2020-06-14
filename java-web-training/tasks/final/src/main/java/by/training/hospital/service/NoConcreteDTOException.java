package by.training.hospital.service;

public class NoConcreteDTOException extends Exception {
    public NoConcreteDTOException() {
    }

    public NoConcreteDTOException(String message) {
        super(message);
    }

    public NoConcreteDTOException(String message, Throwable cause) {
        super(message, cause);
    }
}
