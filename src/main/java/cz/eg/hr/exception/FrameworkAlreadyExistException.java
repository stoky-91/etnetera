package cz.eg.hr.exception;

public class FrameworkAlreadyExistException extends RuntimeException {
    public FrameworkAlreadyExistException(String message) {
        super(message);
    }
}
