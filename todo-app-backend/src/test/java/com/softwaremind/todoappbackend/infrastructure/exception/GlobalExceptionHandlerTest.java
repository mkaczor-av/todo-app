package com.softwaremind.todoappbackend.infrastructure.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    @Test
    void shouldHandleMethodArgumentNotValidException() {
        // Given
        final var methodArgumentNotValidException = mock(MethodArgumentNotValidException.class);
        when(methodArgumentNotValidException.getFieldErrors())
                .thenReturn(List.of(new FieldError("name1", "field1", "message1")));
        final var globalExceptionHandler = new GlobalExceptionHandler();

        // When
        final var responseEntity = globalExceptionHandler.handleMethodArgumentNotValid(
                methodArgumentNotValidException,
                HttpHeaders.EMPTY,
                HttpStatusCode.valueOf(400),
                new ServletWebRequest(new MockHttpServletRequest()));

        // Then
        final var problemDetail = (ProblemDetail) Objects.requireNonNull(responseEntity).getBody();
        assertThat(problemDetail)
                .satisfies(pD -> {
                    assertThat(pD.getTitle()).isEqualTo("Constraint Violation");
                    assertThat(pD.getStatus()).isEqualTo(400);
                    assertThat(pD.getProperties())
                            .containsExactly(
                                    Map.entry("violations",
                                    List.of(new GlobalExceptionHandler.Violation("field1", "message1")))
                            );
                });
    }

    @Test
    void shouldHandleBusinessException() {
        // Given
        final var sampleBusinessException = new BusinessException("test message", HttpStatus.CONFLICT) {
            @Override
            public HttpStatus getHttpStatus() {
                return super.getHttpStatus();
            }
        };
        final var globalExceptionHandler = new GlobalExceptionHandler();

        // When
        final var responseEntity = globalExceptionHandler.handleBusinessException(sampleBusinessException);

        // Then
        final var problemDetail = (ProblemDetail) Objects.requireNonNull(responseEntity).getBody();
        assertThat(problemDetail)
                .satisfies(pD -> {
                    assertThat(pD.getTitle()).isEqualTo("Conflict");
                    assertThat(pD.getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
                    assertThat(pD.getDetail()).isEqualTo("test message");
                });
    }
}
