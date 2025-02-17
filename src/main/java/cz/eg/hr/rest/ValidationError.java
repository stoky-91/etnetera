package cz.eg.hr.rest;

public record ValidationError(String field, String message) {
    @Override
    public String toString() {
        return field + ": " + message;
    }
}
