package cz.eg.hr.exception;

public class FrameworkNotFoundException extends RuntimeException {
    public FrameworkNotFoundException(String message) {
        super(message);
    }
}
