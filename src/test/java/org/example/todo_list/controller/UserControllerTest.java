package org.example.todo_list.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.todo_list.config.TestConfig;
import org.example.todo_list.config.TestSecurityConfig;
import org.example.todo_list.config.WebMVCConfig;
import org.example.todo_list.dto.request.LoginRegisterRequest;
import org.example.todo_list.dto.request.UpdateUserRequest;
import org.example.todo_list.security.JwtUtils;
import org.example.todo_list.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = UserController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = WebMVCConfig.class // 排除拦截器配置类
        )
)
@Import({TestSecurityConfig.class, TestConfig.class})
@AutoConfigureMockMvc(addFilters = false)
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
        // Given
        LoginRegisterRequest request = new LoginRegisterRequest("test", "123456");
        String mockToken = "jwt_token";

        // When
        Mockito.when(userService.login(request)).thenReturn(true);
        Mockito.when(jwtUtils.generateToken(request.username())).thenReturn(mockToken);

        // Then
        mockMvc.perform(post("/auth/login")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value("登录成功"))
                .andExpect(cookie().exists("jwt_token"))
                .andExpect(cookie().value("jwt_token", mockToken));
    }


    @Test
    void login_ValidationFailedWhenBlankFields() throws Exception {
        // Given
        String invalidJson = "{ \"username\": \"\", \"password\": \"\" }";

        // When & Then
        mockMvc.perform(post("/auth/login")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data.username").value("用户名不能为空"))
                .andExpect(jsonPath("$.data.password").value("密码不能为空"));
    }
    // endregion

    // region 注册测试
    @Test
    void register_Success() throws Exception {
        // Given
        LoginRegisterRequest request = new LoginRegisterRequest("newUser", "password123");

        // When & Then
        mockMvc.perform(post("/auth/register")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value("注册成功"));

        Mockito.verify(userService).register(request);
    }

    @Test
    void register_ValidationFailedWhenBlankFields() throws Exception {
        // Given
        String invalidJson = "{ \"username\": \"\", \"password\": \"\" }";

        // When & Then
        mockMvc.perform(post("/auth/register")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data.username").value("用户名不能为空"))
                .andExpect(jsonPath("$.data.password").value("密码不能为空"));
    }
    // endregion

    // region 更新用户测试
    @Test
    void updateUser_Success() throws Exception {
        // Given
        Long userId = 1L;
        UpdateUserRequest request = new UpdateUserRequest("newName", "newPass", "avatar.jpg");

        // When & Then
        mockMvc.perform(patch("/auth/{id}", userId)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value("更新成功"));

        Mockito.verify(userService).updateUser(userId, request);
    }


    // region 退出测试
    @Test
    void logout_Success() throws Exception {
        mockMvc.perform(get("/auth/logout"))
                .andExpect(status().isOk())
                .andExpect(cookie().maxAge("jwt_token", 0))
                .andExpect(cookie().value("jwt_token", ""));
    }
    // endregion
}
