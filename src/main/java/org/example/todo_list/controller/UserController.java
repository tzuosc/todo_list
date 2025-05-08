package org.example.todo_list.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.todo_list.dto.request.LoginRegisterRequest;
import org.example.todo_list.repository.JPA.UserRepository;
import org.example.todo_list.security.JwtUtils;
import org.example.todo_list.service.UserService;
import org.example.todo_list.utils.ApiResponse;
import org.springframework.web.bind.annotation.*;

@Tag(name = "用户相关Api", description = "用于登录和注册")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final JwtUtils jwtUtils;
    private final UserService userService;
    private final UserRepository userRepository;

//    @Operation(summary = "登录",
//            description = "传入用户名和密码, 如果登陆成功就返回一个 cookie 给前端. 这个 cookie 的值就是 jwt_toke.")
//    @PostMapping("/login")
//    public ApiResponse<UserResponse> login(@Valid @RequestBody LoginRegisterRequest request,
//                                           HttpServletResponse response) {
//        // TODO 登录, 传入 LoginRegisterRequest, HttpServletResponse, 登录成功后为 HttpServletResponse 添加 setCookie 响应头, 值为 token
//
//    }

    @Operation(summary = "注册",
            description = "传入用户名,密码和头像地址")
    @PostMapping("/register")
    public ApiResponse<String> register(@RequestBody @Valid LoginRegisterRequest request) {
        userService.register(request);
        return ApiResponse.success("注册成功");
    }


//    @Operation(summary = "更新用户", description = "增量更新, 可以传入一个或者多个值, 传入的数据对应的字段如果不为空, 就更新他")
//    @PatchMapping({"/", ""})
//    public ApiResponse<UserResponse> update(@RequestBody @Valid UpdateUserRequest request,
//                                            @RequestAttribute("userId") Long id) {
//
//        // TODO 更新用户, 传入 id(可以选择使用拦截器拦截 cookie 后获取 id, 我已经把 id 保存到了 cookie 中, 只需要在其他地方使用 @RequestAttribute 注解就可以把相应的用户的 id 取出来) , UpdateUser(所有参数都是非必须的)
//
//    }

//    @Operation(summary = "用于上传头像", description = "传入一个 id, 用于指定传入的头像归属于那个用户. 一个图片文件作为头像. 记得设置 content-type 为 multipart/form-data")
//    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ApiResponse<String> upload(@RequestParam("file") MultipartFile file,
//                                      @RequestAttribute("userId") Long id) throws IOException {
//       //TODO 更新头像 api 需要了解如何上传文件.
//    }

//    @Operation(summary = "登出", description = "想要删除 cookie 就把这个 token 的生命周期设置为 0 就可以了.")
//    @GetMapping("/logout")
//    public ApiResponse<String> logout(HttpServletResponse response) {
//        // TODO 登出, 直接调用 cookieUtil 的删除 cookie 的函数就行了
//
//    }
}
