package com.softwaremind.todoappbackend.todo.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "todo")
@EntityListeners(AuditingEntityListener.class)
class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @NotNull
    private long id;

    @Column(name = "uuid")
    @NotNull
    private UUID uuid;

    @Column(name = "name")
    @NotNull
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "completed")
    @NotNull
    private boolean completed;

    @Column(name = "created_at")
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    private LocalDateTime updatedAt;

    public Todo() {
        // JPA
    }

    Todo(String name, String description) {
        this.uuid = UUID.randomUUID();
        this.name = name;
        this.description = description;
        this.completed = false;
    }

    void update(String name, String description, boolean completed) {
        this.name = name;
        this.description = description;
        this.completed = completed;
    }

    UUID getUuid() {
        return uuid;
    }

    String getName() {
        return name;
    }

    String getDescription() {
        return description;
    }

    boolean isCompleted() {
        return completed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof final Todo todo)) return false;
        return Objects.equals(uuid, todo.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}
