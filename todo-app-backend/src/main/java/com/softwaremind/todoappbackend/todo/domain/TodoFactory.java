package com.softwaremind.todoappbackend.todo.domain;

import com.softwaremind.todoappbackend.todo.dto.CreateTodoDto;

import java.util.Objects;

class TodoFactory {

    private final CreateTodoDto createTodoDto;

    TodoFactory(CreateTodoDto createTodoDto) {
        this.createTodoDto = Objects.requireNonNull(createTodoDto);
    }

    Todo create() {
        return new Todo(createTodoDto.name(), createTodoDto.description());
    }
}
