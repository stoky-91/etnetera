package cz.eg.hr.controller;

import cz.eg.hr.exception.FrameworkAlreadyExistException;
import cz.eg.hr.exception.FrameworkNotFoundException;
import cz.eg.hr.rest.Errors;
import cz.eg.hr.rest.ValidationError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GeneralControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Errors> handleValidationException(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        List<ValidationError> errorList = result.getFieldErrors().stream()
            .map(e -> new ValidationError(e.getField(), e.getCode()))
            .toList();

        return ResponseEntity.badRequest().body(new Errors(errorList));
    }

    @ExceptionHandler(FrameworkNotFoundException.class)
    public ResponseEntity<String> handleFrameworkNotFoundException(FrameworkNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(FrameworkAlreadyExistException.class)
    public ResponseEntity<Map<String, String>> handleFrameworkAlreadyExistException(FrameworkAlreadyExistException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}
