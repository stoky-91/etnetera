package cz.eg.hr.rest;

import java.util.List;
import java.util.stream.Collectors;

public record Errors(List<ValidationError> errors) {
    @Override
    public String toString() {
        return errors.stream()
            .map(ValidationError::toString)
            .collect(Collectors.joining(", "));
    }
}
