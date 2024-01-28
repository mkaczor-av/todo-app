package com.softwaremind.todoappbackend.todo.domain;

import com.softwaremind.todoappbackend.todo.dto.CreateTodoDto;
import com.softwaremind.todoappbackend.todo.dto.TodoDto;
import com.softwaremind.todoappbackend.todo.dto.UpdateTodoDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

@Transactional
public class TodoFacade {

    private final TodoRepository todoRepository;

    TodoFacade(TodoRepository todoRepository) {
        this.todoRepository = Objects.requireNonNull(todoRepository);
    }

    public TodoDto create(CreateTodoDto createTodoDto) {
        final var createdTodo = todoRepository.save(new TodoFactory(createTodoDto).create());

        return new TodoMapper().map(createdTodo);
    }

    public TodoDto update(UUID todoUuid, UpdateTodoDto updateTodoDto) {
        final var todo = todoRepository.findByUuid(todoUuid)
                .orElseThrow(() -> new TodoDoesNotExistException(todoUuid));

        todo.update(
                updateTodoDto.name(),
                updateTodoDto.description(),
                updateTodoDto.completed()
        );

        return new TodoMapper().map(todo);
    }

    public void delete(UUID todoUuid) {
        todoRepository.findByUuid(todoUuid)
                .ifPresentOrElse(
                        todo -> todoRepository.deleteByUuid(todo.getUuid()),
                        () -> {
                            throw new TodoDoesNotExistException(todoUuid);
                        }
                );
    }

    public TodoDto find(UUID todoUuid) {
        return todoRepository.findByUuid(todoUuid)
                .map(todoDto -> new TodoMapper().map(todoDto))
                .orElseThrow(() -> new TodoDoesNotExistException(todoUuid));
    }

    public Page<TodoDto> findAll(Pageable pageable, String name) {
        return todoRepository.findAll(pageable, name)
                .map(todo -> new TodoMapper().map(todo));
    }
}
