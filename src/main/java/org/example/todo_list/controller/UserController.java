package org.example.todo_list.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.todo_list.dto.request.LoginRegisterRequest;
import org.example.todo_list.dto.request.UpdateUserRequest;
import org.example.todo_list.dto.response.UserResponse;
import org.example.todo_list.exception.UserException;
import org.example.todo_list.exception.errors.UserError;
import org.example.todo_list.model.User;
import org.example.todo_list.repository.UserRepository;
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
    private final UserRepository userRepository;

    @Operation(summary = "登录",
            description = "传入用户名和密码")
    @PostMapping("/login")
    public ApiResponse<UserResponse> login(@Valid @RequestBody LoginRegisterRequest request,
                                           HttpServletResponse response) {
        // TODO 登录, 传入 LoginRegisterRequest, HttpServletResponse, 登录成功后为 HttpServletResponse 添加 setCookie 响应头, 值为 token

        User user = userRepository.findByUsername(request.username());

        if (userService.login(request)) {
            String token = jwtUtils.generateToken(user.getId());
            CookieUtil.setCookie(response, token);
        }

        UserResponse userResponse = UserResponse.builder()
                .username(user.getUsername())
//                .avatarUrl(user.getAvatarUrl())
                .build();
        return ApiResponse.success(userResponse);
    }

    @Operation(summary = "注册",
            description = "传入用户名,密码和头像地址")
    @PostMapping("/register")
    public ApiResponse<String> register(@RequestBody @Valid LoginRegisterRequest request) {
        userService.register(request);
        return ApiResponse.success("注册成功");
    }

    @PatchMapping("/{id}")
    public ApiResponse<UserResponse> update(@PathVariable Long id, @RequestBody @Valid UpdateUserRequest request) {

        // TODO 更新用户, 传入 id , UpdateUser(所有参数都是非必须的)
        userService.updateUser(id, request);

        return userRepository.findById(id)
                .map(user -> ApiResponse.success(UserResponse.builder()
//                        .avatarUrl(user.getAvatarUrl())
                        .username(user.getUsername())
                        .build()
                )).orElseThrow(() -> new UserException(
                        UserError.NO_USER.getCode(),
                        UserError.NO_USER.getMessage())
                );
    }

    @GetMapping("/logout")
    public ApiResponse<String> logout(HttpServletResponse response) {
        // TODO 登出, 直接调用 cookieUtil 的删除 cookie 的函数就行了

        CookieUtil.deleteCookie(response);
        return ApiResponse.success(null);
    }
}
