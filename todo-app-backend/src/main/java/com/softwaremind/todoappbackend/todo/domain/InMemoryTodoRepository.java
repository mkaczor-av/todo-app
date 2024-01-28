package com.softwaremind.todoappbackend.todo.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

class InMemoryTodoRepository implements TodoRepository {

    private final ConcurrentHashMap<UUID, Todo> db = new ConcurrentHashMap<>();

    @Override
    public Todo save(Todo todo) {
        db.put(todo.getUuid(), todo);

        return todo;
    }

    @Override
    public Optional<Todo> findByUuid(UUID uuid) {
        return Optional.ofNullable(db.get(uuid));
    }

    @Override
    public Page<Todo> findAll(Pageable pageable, String name) {
        // TODO mk filer + pagination
        return new PageImpl<>(db.values().stream().toList(), pageable, 10);
    }

    @Override
    public void deleteByUuid(UUID uuid) {
        db.remove(uuid);
    }
}
