package org.example.todo_list.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.todo_list.DTO.request.LoginRequest;
import org.example.todo_list.DTO.request.RegisterRequest;
import org.example.todo_list.model.User;
import org.example.todo_list.repository.UserRepository;
import org.example.todo_list.service.UserService;
import org.example.todo_list.utils.ApiResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "用户相关Api", description = "用于登录和注册")
@RestController
@RequestMapping(value = "/auth")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;

    @Operation(summary = "登录", description = "传入用户名和密码")
    @PostMapping(value = "/login")
    public ApiResponse<String> login(@RequestBody LoginRequest request) {
        boolean judge = userRepository.existsByUsernameAndPassword(
                request.username(), request.password());
        if (judge) {
            return ApiResponse.success(null);
        } else {
            return ApiResponse.error(400, "用户或者密码错误");
        }
    }

    @Operation(summary = "注册", description = "传入用户名,密码和头像地址")
    @PostMapping(value = "/register")
    public ApiResponse<String> register(@RequestBody RegisterRequest request) {
        User user = User.builder()
                .avatarUrl(request.avatarUrl())
                .username(request.username())
                .password(request.password())
                .build();
        if (userRepository.existsByUsername(request.username())) {
            return ApiResponse.error(400, "用户已存在");
        } else {
            userRepository.save(user);
            return ApiResponse.success(null);
        }
    }
}
