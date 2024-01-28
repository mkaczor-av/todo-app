package com.softwaremind.todoappbackend.todo;

import com.softwaremind.todoappbackend.matcher.UuidPatternMatcher;
import com.softwaremind.todoappbackend.requirements.ApiBaseTest;
import com.softwaremind.todoappbackend.todo.dto.CreateTodoDto;
import com.softwaremind.todoappbackend.todo.dto.UpdateTodoDto;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TodoControllerTest extends ApiBaseTest {

    @BeforeAll
    static void beforeAll() {
        postgresqlContainer.start();
    }

    @AfterAll
    static void cleanUp() {
        postgresqlContainer.stop();
    }

    @Nested
    class CreateTodo {

        @Test
        void shouldCreateTodo() throws Exception {
            // Given
            final var createTodoDto = new CreateTodoDto("buy groceries", "milk, eggs, bread");

            // When
            final var result = performCreateTodo(createTodoDto);

            // Then
            result
                    .andExpect(status().isCreated())
                    .andExpect(content().contentType(APPLICATION_JSON))
                    .andExpect(jsonPath("$.uuid").value(new UuidPatternMatcher()))
                    .andExpect(jsonPath("$.name").value("buy groceries"))
                    .andExpect(jsonPath("$.description").value("milk, eggs, bread"))
                    .andExpect(jsonPath("$.completed").value(false));
        }

        @Test
        void shouldReturnErrorWhenRequestDataIsInvalid() throws Exception {
            // Given
            final var createTodoDto = new CreateTodoDto(
                    "very very long todo name which is not allowed",
                    null
            );

            // When
            final var result = performCreateTodo(createTodoDto);

            // Then
            result
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(APPLICATION_PROBLEM_JSON))
                    .andExpect(jsonPath("$.title").value("Constraint Violation"))
                    .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
                    .andExpect(jsonPath("$.violations", hasSize(1)))
                    .andExpect(jsonPath("$.violations[0].field").value("name"));
        }
    }

    @Nested
    class UpdateTodo {

        @Test
        void shouldUpdateTodo() throws Exception {
            // Given
            final var todoUuid = createTodo(new CreateTodoDto("buy groceries", "milk, eggs, bread"));

            // When
            final var result = performUpdateTodo(todoUuid, new UpdateTodoDto("updated name", null, true));

            // Then
            result
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(APPLICATION_JSON))
                    .andExpect(jsonPath("$.uuid").value(todoUuid.toString()))
                    .andExpect(jsonPath("$.name").value("updated name"))
                    .andExpect(jsonPath("$.description").value(nullValue()))
                    .andExpect(jsonPath("$.completed").value(true));
        }

        @Test
        void shouldReturnErrorWhenRequestDataIsInvalid() throws Exception {
            // Given
            final var todoUuid = createTodo(new CreateTodoDto("buy groceries", "milk, eggs, bread"));

            // When
            final var result = performUpdateTodo(todoUuid, new UpdateTodoDto(null, null, true));

            // Then
            result
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(APPLICATION_PROBLEM_JSON))
                    .andExpect(jsonPath("$.title").value("Constraint Violation"))
                    .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
                    .andExpect(jsonPath("$.violations", hasSize(1)))
                    .andExpect(jsonPath("$.violations[0].field").value("name"));
        }

        @Test
        void shouldThrowExceptionWhenTodoDoesNotExist() throws Exception {
            // Given
            final var nonExistentTodoUuid = UUID.fromString("b37460dc-8881-45b4-8240-a8bfde211a52");

            // When
            final var result = performUpdateTodo(nonExistentTodoUuid, new UpdateTodoDto("update name", null, true));

            // Then
            result
                    .andExpect(status().isNotFound())
                    .andExpect(content().contentType(APPLICATION_PROBLEM_JSON))
                    .andExpect(jsonPath("$.title").value("Not Found"))
                    .andExpect(jsonPath("$.status").value(NOT_FOUND.value()))
                    .andExpect(jsonPath("$.detail").value(String.format("Todo with id %s does not exist", nonExistentTodoUuid)));
        }
    }

    @Nested
    class DeleteTodo {

        @Test
        void shouldDeleteTodo() throws Exception {
            // Given
            final var todoUuid = createTodo(new CreateTodoDto("buy groceries", "milk, eggs, bread"));

            // When
            final var result = performDeleteTodo(todoUuid);

            // Then
            result.andExpect(status().isNoContent());
        }

        @Test
        void shouldThrowExceptionWhenTodoDoesNotExist() throws Exception {
            // Given
            final var nonExistentTodoUuid = UUID.fromString("b37460dc-8881-45b4-8240-a8bfde211a52");

            // When
            final var result = performDeleteTodo(nonExistentTodoUuid);

            // Then
            result
                    .andExpect(status().isNotFound())
                    .andExpect(content().contentType(APPLICATION_PROBLEM_JSON))
                    .andExpect(jsonPath("$.title").value("Not Found"))
                    .andExpect(jsonPath("$.status").value(NOT_FOUND.value()))
                    .andExpect(jsonPath("$.detail").value(String.format("Todo with id %s does not exist", nonExistentTodoUuid)));
        }
    }

    @Nested
    class FindTodo {

        @Test
        void shouldFindTodo() throws Exception {
            // Given
            final var todoUuid = createTodo(new CreateTodoDto("buy groceries", "milk, eggs, bread"));

            // When
            final var result = performFindTodo(todoUuid);

            // Then
            result
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(APPLICATION_JSON))
                    .andExpect(jsonPath("$.uuid").value(todoUuid.toString()))
                    .andExpect(jsonPath("$.name").value("buy groceries"))
                    .andExpect(jsonPath("$.description").value("milk, eggs, bread"))
                    .andExpect(jsonPath("$.completed").value(false));
        }

        @Test
        void shouldThrowExceptionWhenTodoDoesNotExist() throws Exception {
            // Given
            final var nonExistentTodoUuid = UUID.fromString("b37460dc-8881-45b4-8240-a8bfde211a52");

            // When
            final var result = performFindTodo(nonExistentTodoUuid);

            // Then
            result
                    .andExpect(status().isNotFound())
                    .andExpect(content().contentType(APPLICATION_PROBLEM_JSON))
                    .andExpect(jsonPath("$.title").value("Not Found"))
                    .andExpect(jsonPath("$.status").value(NOT_FOUND.value()))
                    .andExpect(jsonPath("$.detail").value(String.format("Todo with id %s does not exist", nonExistentTodoUuid)));
        }
    }

    @Nested
    class FindTodos {

        @Test
        void shouldFindTodos() throws Exception {
            // Given
            final var todoUuid1 = createTodo(new CreateTodoDto("test1", null));
            final var todoUuid2 = createTodo(new CreateTodoDto("test2", null));
            final var todoUuid3 = createTodo(new CreateTodoDto("test3", null));

            // When
            final var result = performFindTodos(null);

            // Then
            result
                    .andExpect(status().is(200))
                    .andExpect(content().contentType(APPLICATION_JSON))
                    .andExpect(jsonPath("$.content.*", hasSize(3)))

                    .andExpect(jsonPath("$.content[0].uuid").value(todoUuid3.toString()))
                    .andExpect(jsonPath("$.content[0].name").value("test3"))
                    .andExpect(jsonPath("$.content[0].description").value(nullValue()))
                    .andExpect(jsonPath("$.content[0].completed").value(false))

                    .andExpect(jsonPath("$.content[1].uuid").value(todoUuid2.toString()))
                    .andExpect(jsonPath("$.content[1].name").value("test2"))
                    .andExpect(jsonPath("$.content[1].description").value(nullValue()))
                    .andExpect(jsonPath("$.content[1].completed").value(false))

                    .andExpect(jsonPath("$.content[2].uuid").value(todoUuid1.toString()))
                    .andExpect(jsonPath("$.content[2].name").value("test1"))
                    .andExpect(jsonPath("$.content[2].description").value(nullValue()))
                    .andExpect(jsonPath("$.content[2].completed").value(false))

                    .andExpect(jsonPath("$.number").value(0))
                    .andExpect(jsonPath("$.size").value(20));
        }

        @Test
        void shouldFindTodosLimitedByNameFilter() throws Exception {
            // Given
            createTodo(new CreateTodoDto("todo00", null));
            final var todoUuid2 = createTodo(new CreateTodoDto("todo10", null));
            final var todoUuid3 = createTodo(new CreateTodoDto("todo11", null));
            createTodo(new CreateTodoDto("todo20", null));

            // When
            final var result = performFindTodos("do1");

            // Then
            result
                    .andExpect(status().is(200))
                    .andExpect(content().contentType(APPLICATION_JSON))
                    .andExpect(jsonPath("$.content.*", hasSize(2)))

                    .andExpect(jsonPath("$.content[0].uuid").value(todoUuid3.toString()))
                    .andExpect(jsonPath("$.content[0].name").value("todo11"))
                    .andExpect(jsonPath("$.content[0].description").value(nullValue()))
                    .andExpect(jsonPath("$.content[0].completed").value(false))

                    .andExpect(jsonPath("$.content[1].uuid").value(todoUuid2.toString()))
                    .andExpect(jsonPath("$.content[1].name").value("todo10"))
                    .andExpect(jsonPath("$.content[1].description").value(nullValue()))
                    .andExpect(jsonPath("$.content[1].completed").value(false))

                    .andExpect(jsonPath("$.number").value(0))
                    .andExpect(jsonPath("$.size").value(20));
        }
    }
}
