package com.softwaremind.todoappbackend.todo.domain;

import com.softwaremind.todoappbackend.todo.dto.TodoDto;

class TodoMapper {

    TodoDto map(Todo todo) {
        return new TodoDto(
                todo.getUuid(),
                todo.getName(),
                todo.getDescription(),
                todo.isCompleted()
        );
    }
}
