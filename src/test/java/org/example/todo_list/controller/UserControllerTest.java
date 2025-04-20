package org.example.todo_list.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.todo_list.dto.request.LoginRegisterRequest;
import org.example.todo_list.dto.request.UpdateUserRequest;
import org.example.todo_list.security.JwtUtils;
import org.example.todo_list.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@Import(TestConfig.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private ObjectMapper objectMapper;

    // region 登录测试
    @Test
    void login_Success() throws Exception {
        LoginRegisterRequest request = new LoginRegisterRequest("test", "123456");
        String mockToken = "mock-jwt-token";

        // 模拟服务层返回true，并生成Token
        Mockito.when(userService.login(request)).thenReturn(true);
        Mockito.when(jwtUtils.generateToken(request.username())).thenReturn(mockToken);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$.data").value("登录成功"))
                .andExpect(cookie().exists("token")) // 验证Cookie存在
                .andExpect(cookie().value("token", mockToken)); // 验证Token值
    }

    @Test
    void login_FailedWhenInvalidCredentials() throws Exception {
        LoginRegisterRequest request = new LoginRegisterRequest("wrong", "wrong");

        // 模拟服务层返回false
        Mockito.when(userService.login(request)).thenReturn(false);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized()) // 假设修改代码返回401
                .andExpect((ResultMatcher) jsonPath("$.message").value("用户名或密码错误"));
    }

    @Test
    void login_ValidationFailedWhenBlankFields() throws Exception {
        // 构造无效请求（用户名和密码为空）
        String invalidJson = "{ \"username\": \"\", \"password\": \"\" }";

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect((ResultMatcher) jsonPath("$.data.username").value("用户名不能为空"))
                .andExpect((ResultMatcher) jsonPath("$.data.password").value("密码不能为空"));
    }
    // endregion

    // region 注册测试
    @Test
    void register_Success() throws Exception {
        LoginRegisterRequest request = new LoginRegisterRequest("newUser", "password123");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$.data").value("注册成功"));

        // 验证userService.register被调用
        Mockito.verify(userService).register(request);
    }

    @Test
    void register_ValidationFailedWhenBlankFields() throws Exception {
        String invalidJson = "{ \"username\": \"\", \"password\": \"\" }";

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect((ResultMatcher) jsonPath("$.data.username").value("用户名不能为空"))
                .andExpect((ResultMatcher) jsonPath("$.data.password").value("密码不能为空"));
    }
    // endregion

    // region 更新用户测试
    @Test
    void updateUser_Success() throws Exception {
        Long userId = 1L;
        UpdateUserRequest request = new UpdateUserRequest("newName", "newPass", "avatar.jpg");

        mockMvc.perform(patch("/auth/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$.data").value("更新成功"));

        // 验证路径参数和服务方法调用
        Mockito.verify(userService).updateUser(userId, request);
    }

    @Test
    void updateUser_ValidationFailedWhenInvalidData() throws Exception {
        Long userId = 1L;
        // 假设UpdateUserRequest中某个字段有校验（如密码长度）
        String invalidJson = "{ \"password\": \"123\" }"; // 密码过短

        mockMvc.perform(patch("/auth/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect((ResultMatcher) jsonPath("$.data.password").value("密码长度至少6位"));
    }
    // endregion

    // region 退出测试
    @Test
    void logout_Success() throws Exception {
        mockMvc.perform(get("/auth/logout"))
                .andExpect(status().isOk())
                .andExpect(cookie().maxAge("token", 0)) // 验证Cookie被设为过期
                .andExpect(cookie().value("token", ""));
    }
    // endregion
}
