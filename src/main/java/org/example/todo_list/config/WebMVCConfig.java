package org.example.todo_list.config;

import lombok.RequiredArgsConstructor;
import org.example.todo_list.security.JwtInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMVCConfig implements WebMvcConfigurer {
    private final JwtInterceptor jwtInterceptor;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                .addMapping("/**") // 匹配所有路径
                .allowedOrigins("*") // 允许所有来源
                .allowedMethods("*") // 允许所有HTTP方法
                .allowedHeaders("*") // 允许所有请求头
                .allowCredentials(false);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**")               // 拦截所有路径
                .excludePathPatterns(
                        "/login",
                        "/static/**",
                        "/register",
                        "/logout",
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-resources/**",
                        "/webjars/**");
    }
}
