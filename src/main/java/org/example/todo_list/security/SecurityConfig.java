<<<<<<<< HEAD:src/main/java/org/example/todo_list/config/SecurityConfig.java
package org.example.todo_list.config;
========
package org.example.todo_list.security;
>>>>>>>> master:src/main/java/org/example/todo_list/security/SecurityConfig.java

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(
                        auth -> auth.anyRequest().permitAll() // 允许所有请求
                )
                .csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }
}
