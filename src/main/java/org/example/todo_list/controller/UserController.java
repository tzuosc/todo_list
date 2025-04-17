package org.example.todo_list.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.todo_list.dto.request.LoginRequest;
import org.example.todo_list.dto.request.RegisterRequest;
import org.example.todo_list.security.JwtUtils;
import org.example.todo_list.service.UserService;
import org.example.todo_list.utils.ApiResponse;
import org.example.todo_list.utils.CookieUtil;
import org.springframework.web.bind.annotation.*;

@Tag(name = "用户相关Api", description = "用于登录和注册")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {
    private final JwtUtils jwtUtils;
    private final UserService userService;

    @Operation(summary = "登录",
            description = "传入用户名和密码")
    @PostMapping("/login")
    public ApiResponse<String> login(@Valid @RequestBody LoginRequest request,
                                     HttpServletResponse response) {
        if (userService.login(request)) {
            CookieUtil.setCookie(response, jwtUtils.generateToken(request.username()));
        }
        return ApiResponse.success("登录成功");
    }

    @Operation(summary = "注册",
            description = "传入用户名,密码和头像地址")
    @PostMapping("/register")
    public ApiResponse<String> register(@Valid @RequestBody RegisterRequest request) {
        userService.register(request);
        return ApiResponse.success("注册成功");
    }

    @GetMapping("/logout")
    public void logout(HttpServletResponse response) {
        CookieUtil.deleteCookie(response);
    }
}
