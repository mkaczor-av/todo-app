package com.softwaremind.todoappbackend.todo.domain;

import com.softwaremind.todoappbackend.infrastructure.exception.BusinessException;
import org.springframework.http.HttpStatus;

import java.util.UUID;

class TodoDoesNotExistException extends BusinessException {

    TodoDoesNotExistException(UUID todoUuid) {
        super(
                String.format("Todo with id %s does not exist", todoUuid),
                HttpStatus.NOT_FOUND
        );
    }
}
