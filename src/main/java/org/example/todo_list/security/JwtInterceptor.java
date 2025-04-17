package org.example.todo_list.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.todo_list.exception.AuthException;
import org.example.todo_list.exception.errors.UserError;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtInterceptor implements HandlerInterceptor {
    private final JwtUtils jwtUtils;
    private final PathMatcher pathMatcher = new AntPathMatcher();
    private final List<String> EXCLUDE_PATH =
            new ArrayList<>(Arrays.asList(
                    "/auth/**",
                    "/v3/api-docs/**",
                    "/swagger-ui/**",
                    "/swagger-resources/**",
                    "/webjars/**"));

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Cookie[] cookies = request.getCookies();
        String servletPath = request.getServletPath();
        String requestURI = request.getRequestURI();
        if (EXCLUDE_PATH.stream().anyMatch(pattern -> pathMatcher.match(pattern, request.getRequestURI()))) {
            return true;
        }
        if (cookies == null) {
            throw new AuthException(
                    UserError.NO_COOKIE.getCode(),
                    UserError.NO_COOKIE.getMessage()
            );
        }

        List<Claims> res = new ArrayList<>();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("jwt_token")) {
                String token = cookie.getValue();
                Claims claim = jwtUtils.parseToken(token);
                res.add(claim);
            }
        }
        return !res.isEmpty();
    }
}
