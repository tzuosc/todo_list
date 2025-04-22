package org.example.todo_list.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.todo_list.dto.request.LoginRegisterRequest;
import org.example.todo_list.dto.request.UpdateUserRequest;
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
    public ApiResponse<String> login(@Valid @RequestBody LoginRegisterRequest request,
                                     HttpServletResponse response) {
        // TODO 登录, 传入 LoginRegisterRequest, HttpServletResponse, 登录成功后为 HttpServletResponse 添加 setCookie 响应头, 值为 token
        if (userService.login(request)) {
            CookieUtil.setCookie(response, jwtUtils.generateToken(request.username()));
        }
        return ApiResponse.success("登录成功");
    }

    @Operation(summary = "注册",
            description = "传入用户名,密码和头像地址")
    @PostMapping("/register")
    public ApiResponse<String> register(@RequestBody @Valid LoginRegisterRequest request) {
        userService.register(request);
        return ApiResponse.success("注册成功");
    }

    @PatchMapping("/{id}")
    public ApiResponse<String> update(@PathVariable Long id, @RequestBody @Valid UpdateUserRequest request) {
        // TODO 更新用户, 传入 id , UpdateUser(所有参数都是非必须的)
        userService.updateUser(id, request);
        return ApiResponse.success("更新成功");
    }

    @GetMapping("/logout")
    public void logout(HttpServletResponse response) {
        // TODO 登出, 直接调用 cookieUtil 的删除 cookie 的函数就行了

        CookieUtil.deleteCookie(response);
    }
}
