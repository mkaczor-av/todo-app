package com.softwaremind.todoappbackend.todo.domain;

import com.softwaremind.todoappbackend.todo.dto.CreateTodoDto;
import com.softwaremind.todoappbackend.todo.dto.TodoDto;
import com.softwaremind.todoappbackend.todo.dto.UpdateTodoDto;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Objects;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TodoFacadeTest {

    @Nested
    class CreateTodo {

        @Test
        void shouldCreateTodo() {
            // Given
            final var createTodoDto = new CreateTodoDto("buy groceries", "milk, eggs, bread");
            final var todoFacade = new TodoConfiguration().todoFacade();

            // When
            final var result = todoFacade.create(createTodoDto);

            // Then
            assertThat(result)
                    .matches(todoDto -> todoDto.uuid() != null, "todo uuid is not null")
                    .matches(
                            todoDto -> Objects.equals(todoDto.name(), "buy groceries"),
                            "todo name matches"
                    )
                    .matches(
                            todoDto -> Objects.equals(todoDto.description(), "milk, eggs, bread"),
                            "todo description matches"
                    )
                    .matches(todoDto -> !todoDto.completed(), "todo completed matches");
        }
    }

    @Nested
    class UpdateTodo {

        @Test
        void shouldUpdateTodo() {
            // Given
            final var todoFacade = new TodoConfiguration().todoFacade();
            final var createdTodo = todoFacade.create(createdTodoDto());

            // When
            final var result = todoFacade.update(
                    createdTodo.uuid(),
                    new UpdateTodoDto(
                            "updated name",
                            null,
                            true
                    )
            );

            // Then
            assertThat(result)
                    .matches(todoDto -> todoDto.uuid() != null, "todo uuid is not null")
                    .matches(
                            todoDto -> Objects.equals(todoDto.name(), "updated name"),
                            "todo name matches"
                    )
                    .matches(todoDto -> Objects.isNull(todoDto.description()), "todo description matches")
                    .matches(TodoDto::completed, "todo completed matches");
        }

        @Test
        void shouldThrowExceptionWhenRuleDoesNotExist() {
            // Given
            final var todoFacade = new TodoConfiguration().todoFacade();
            final var nonExistentTodoUuid = UUID.fromString("b37460dc-8881-45b4-8240-a8bfde211a52");
            final var updateTodoDto = new UpdateTodoDto("updated name", null, true);

            // Expect
            assertThatThrownBy(() -> todoFacade.update(nonExistentTodoUuid, updateTodoDto))
                    .isInstanceOf(TodoDoesNotExistException.class)
                    .hasMessage(String.format("Todo with id %s does not exist", nonExistentTodoUuid));
        }
    }

    @Nested
    class FindTodo {

        @Test
        void shouldFindTodo() {
            // Given
            final var todoFacade = new TodoConfiguration().todoFacade();
            final var createdTodo = todoFacade.create(createdTodoDto());

            // When
            final var result = todoFacade.find(createdTodo.uuid());

            // Then
            assertThat(result)
                    .matches(
                            todoDto -> Objects.equals(todoDto.uuid(), createdTodo.uuid()),
                            "todo uuid matches"
                    )
                    .matches(
                            todoDto -> Objects.equals(todoDto.name(), createdTodo.name()),
                            "todo name matches"
                    )
                    .matches(
                            todoDto -> Objects.equals(todoDto.description(), createdTodo.description()),
                            "todo description matches"
                    )
                    .matches(
                            todoDto -> Objects.equals(todoDto.completed(), createdTodo.completed()),
                            "todo completed matches"
                    );
        }
    }

    @Nested
    class DeleteTodo {

        @Test
        void shouldDeleteTodo() {
            // Given
            final var inMemoryTodoRepository = new InMemoryTodoRepository();
            final var todoFacade = new TodoFacade(inMemoryTodoRepository);
            final var createdTodo = todoFacade.create(createdTodoDto());

            // When
            todoFacade.delete(createdTodo.uuid());

            // Then
            assertThat(inMemoryTodoRepository.findByUuid(createdTodo.uuid())).isEmpty();
        }

        @Test
        void shouldThrowExceptionWhenRuleDoesNotExist() {
            // Given
            final var todoFacade = new TodoConfiguration().todoFacade();;
            final var nonExistentTodoUuid = UUID.fromString("b37460dc-8881-45b4-8240-a8bfde211a52");

            // Expect
            assertThatThrownBy(() -> todoFacade.delete(nonExistentTodoUuid))
                    .isInstanceOf(TodoDoesNotExistException.class)
                    .hasMessage(String.format("Todo with id %s does not exist", nonExistentTodoUuid));
        }
    }

    private static CreateTodoDto createdTodoDto() {
        return new CreateTodoDto("todo name", "sample desc");
    }
}
