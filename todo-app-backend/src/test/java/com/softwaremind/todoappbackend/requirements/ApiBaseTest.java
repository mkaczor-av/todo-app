package com.softwaremind.todoappbackend.requirements;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softwaremind.todoappbackend.todo.dto.CreateTodoDto;
import com.softwaremind.todoappbackend.todo.dto.TodoDto;
import com.softwaremind.todoappbackend.todo.dto.UpdateTodoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public abstract class ApiBaseTest {

    @Container
    protected static PostgreSQLContainer postgresqlContainer = new PostgreSQLContainer("postgres:16.1-alpine")
            .withDatabaseName("todo_db")
            .withUsername("user")
            .withPassword("pass");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresqlContainer::getUsername);
        registry.add("spring.datasource.password", postgresqlContainer::getPassword);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    protected UUID createTodo(CreateTodoDto createTodoDto) throws Exception {
        final var result = performCreateTodo(createTodoDto)
                .andReturn()
                .getResponse();

        return objectMapper.readValue(result.getContentAsString(), TodoDto.class).uuid();
    }

    protected ResultActions performCreateTodo(CreateTodoDto createTodoDto) throws Exception {
        return mockMvc.perform(
                post("/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createTodoDto))
        );
    }

    protected ResultActions performUpdateTodo(UUID uuid, UpdateTodoDto updateTodoDto) throws Exception {
        return mockMvc.perform(
                put("/todos/{uuid}", uuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateTodoDto))
        );
    }

    protected ResultActions performDeleteTodo(UUID uuid) throws Exception {
        return mockMvc.perform(
                delete("/todos/{uuid}", uuid)
                        .contentType(MediaType.APPLICATION_JSON)
        );
    }

    protected ResultActions performFindTodo(UUID uuid) throws Exception {
        return mockMvc.perform(
                get("/todos/{uuid}", uuid)
                        .contentType(MediaType.APPLICATION_JSON)
        );
    }

    protected ResultActions performFindTodos(String name) throws Exception {
        return mockMvc.perform(
                get("/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("name", name)
        );
    }
}
