package org.example.todo_list.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

// src/test/java/org/example/todo_list/config/TestSecurityConfig.java
@Configuration
@EnableWebSecurity
public class TestSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // 测试环境禁用 CSRF
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/*", "/list/*", "/task/*").permitAll()
                        .anyRequest().authenticated()
                );
        return http.build();
    }
}
