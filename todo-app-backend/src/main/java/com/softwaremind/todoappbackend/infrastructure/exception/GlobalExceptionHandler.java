package com.softwaremind.todoappbackend.infrastructure.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

@RestControllerAdvice
class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String CONSTRAINT_VIOLATION_TITLE = "Constraint Violation";
    private static final String VIOLATIONS_PROPERTY = "violations";

    @Override
    @SuppressWarnings("NullableProblems")
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        final var problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle(CONSTRAINT_VIOLATION_TITLE);
        problemDetail.setProperty(VIOLATIONS_PROPERTY, violations(ex));

        return handleExceptionInternal(ex, problemDetail, headers, status, request);
    }

    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ProblemDetail> handleBusinessException(BusinessException ex) {
        final var httpStatus = ex.getHttpStatus();
        final var problemDetail = ProblemDetail.forStatus(httpStatus);
        problemDetail.setTitle(httpStatus.getReasonPhrase());
        problemDetail.setDetail(ex.getMessage());

        return new ResponseEntity<>(problemDetail, httpStatus);
    }

    private static List<Violation> violations(MethodArgumentNotValidException ex) {
        return ex.getFieldErrors().stream()
                .map(Violation::of)
                .toList();
    }

    @JsonSerialize
    record Violation(String field, String message) {
        static Violation of(FieldError fieldError) {
            return new Violation(fieldError.getField(), fieldError.getDefaultMessage());
        }
    }
}
