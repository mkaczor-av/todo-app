package com.softwaremind.todoappbackend.todo.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class TodoConfiguration {

    TodoFacade todoFacade() {
        return new TodoFacade(new InMemoryTodoRepository());
    }

    @Bean
    TodoFacade todoFacade(TodoRepository todoRepository) {
        return new TodoFacade(todoRepository);
    }
}
