package com.softwaremind.todoappbackend.todo.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

interface TodoRepository extends Repository<Todo, Long> {
    Todo save(Todo todo);
    Optional<Todo> findByUuid(UUID uuid);

    @Query("FROM Todo t WHERE (:name IS NULL OR t.name LIKE %:name%) ORDER BY t.createdAt DESC")
    Page<Todo> findAll(Pageable pageable, @Param("name") String name);

    void deleteByUuid(UUID uuid);
}
