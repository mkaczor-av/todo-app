package com.softwaremind.todoappbackend.todo;

import com.softwaremind.todoappbackend.infrastructure.metrics.RequestCounter;
import com.softwaremind.todoappbackend.todo.domain.TodoFacade;
import com.softwaremind.todoappbackend.todo.dto.CreateTodoDto;
import com.softwaremind.todoappbackend.todo.dto.TodoDto;
import com.softwaremind.todoappbackend.todo.dto.UpdateTodoDto;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.UUID;

@RestController
class TodoController {

    private final TodoFacade todoFacade;
    private final RequestCounter requestCounter;

    TodoController(TodoFacade todoFacade, RequestCounter requestCounter) {
        this.todoFacade = Objects.requireNonNull(todoFacade);
        this.requestCounter = Objects.requireNonNull(requestCounter);
    }

    @PostMapping("/todos")
    @Operation(summary = "Create todo")
    ResponseEntity<TodoDto> createTodo(@RequestBody @Valid CreateTodoDto createTodoDto) {
        requestCounter.recordRequest(RequestCounter.CounterType.CREATE_TODO);

        return new ResponseEntity<>(todoFacade.create(createTodoDto), HttpStatus.CREATED);
    }

    @PutMapping("/todos/{uuid}")
    @Operation(summary = "Update todo")
    ResponseEntity<TodoDto> update(
            @PathVariable("uuid") UUID uuid,
            @RequestBody @Valid UpdateTodoDto updateTodoDto
    ) {
        requestCounter.recordRequest(RequestCounter.CounterType.UPDATE_TODO);

        return ResponseEntity.ok(todoFacade.update(uuid, updateTodoDto));
    }

    @DeleteMapping("/todos/{uuid}")
    @Operation(summary = "Delete todo")
    ResponseEntity<Void> delete(@PathVariable("uuid") UUID uuid) {
        requestCounter.recordRequest(RequestCounter.CounterType.DELETE_TODO);
        todoFacade.delete(uuid);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/todos/{uuid}")
    @Operation(summary = "Find todo")
    ResponseEntity<TodoDto> findTodo(@PathVariable("uuid") UUID uuid) {
        requestCounter.recordRequest(RequestCounter.CounterType.FIND_TODO);

        return ResponseEntity.ok(todoFacade.find(uuid));
    }

    @GetMapping("/todos")
    @Operation(summary = "Find all todos, optionally filter by name")
    ResponseEntity<Page<TodoDto>> findAllTodos(
            Pageable pageable,
            @RequestParam(value = "name", required = false) String name
    ) {
        requestCounter.recordRequest(RequestCounter.CounterType.FIND_TODOS);

        return ResponseEntity.ok(todoFacade.findAll(pageable, name));
    }
}
