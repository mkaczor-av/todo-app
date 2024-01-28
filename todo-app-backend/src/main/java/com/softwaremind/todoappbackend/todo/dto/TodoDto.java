package com.softwaremind.todoappbackend.todo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record TodoDto(
        @JsonProperty("uuid") UUID uuid,
        @JsonProperty("name") String name,
        @JsonProperty("description") String description,
        @JsonProperty("completed") boolean completed
) {
}
