package org.example.todo_list.controller;

import org.example.todo_list.security.JwtUtils;
import org.example.todo_list.service.TaskService;
import org.example.todo_list.service.UserService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class TestConfig {
    @Bean
    @Primary
    public JwtUtils jwtUtils() {
        return Mockito.mock(JwtUtils.class);
    }

    @Bean
    @Primary
    public TaskService taskService() {
        return Mockito.mock(TaskService.class);
    }

    @Bean
    @Primary
    public UserService userService() {
        return Mockito.mock(UserService.class);
    }
}
