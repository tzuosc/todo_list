package org.example.todo_list.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.todo_list.dto.request.LoginRequest;
import org.example.todo_list.dto.request.RegisterRequest;
import org.example.todo_list.exception.AuthException;
import org.example.todo_list.exception.UserError;
import org.example.todo_list.model.User;
import org.example.todo_list.repository.UserRepository;
import org.example.todo_list.security.JwtUtils;
import org.example.todo_list.utils.ApiResponse;
import org.example.todo_list.utils.CookieUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "用户相关Api", description = "用于登录和注册")
@RestController
@RequestMapping(value = "/auth")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    @Operation(summary = "登录",
            description = "传入用户名和密码")
    @PostMapping(value = "/login")
    public ApiResponse<String> login(@Valid @RequestBody LoginRequest request,
                                     HttpServletResponse response) {

        User user = userRepository.findByUsername(request.username());
        boolean judge = userRepository.existsByUsernameAndPassword(
                request.username(), request.password());

        // 如果用户名不存在
        if (user == null) {
            throw new AuthException(
                    UserError.USER_NOT_FOUND.getCode(),
                    UserError.USER_NOT_FOUND.getMessage());
        }
        // 如果密码错误
        if (!judge) {
            throw new AuthException(
                    UserError.INVALID_PASSWD_USER.getCode(),
                    UserError.INVALID_PASSWD_USER.getMessage()
            );
        }
        CookieUtil.setCookie(response, jwtUtils.generateToken(request.username()));
        return ApiResponse.success("登录成功");

    }

    @Operation(summary = "注册",
            description = "传入用户名,密码和头像地址")
    @PostMapping(value = "/register")
    public ApiResponse<String> register(@Valid @RequestBody RegisterRequest request) {

        if (userRepository.existsByUsername(request.username())) {
            throw new AuthException(
                    UserError.DUPLICATE_USERNAME.getCode(),
                    UserError.DUPLICATE_USERNAME.getMessage()
            );
        }

        User user = User.builder()
                .avatarUrl(request.avatarUrl())
                .username(request.username())
                .password(request.password())
                .build();
        userRepository.save(user);
        return ApiResponse.success("注册成功");
    }
}
