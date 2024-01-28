package com.softwaremind.todoappbackend.todo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateTodoDto(
        @JsonProperty("name") @NotBlank @Size(max = 30) String name,
        @JsonProperty("description") @Size(max = 200) String description
) {
}
