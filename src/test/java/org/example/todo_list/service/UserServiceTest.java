package org.example.todo_list.service;

import org.example.todo_list.dto.request.LoginRegisterRequest;
import org.example.todo_list.exception.UserException;
import org.example.todo_list.model.User;
import org.example.todo_list.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;


    // 依赖测试
    @Test
    void contextLoads() {
        assertNotNull(userService);
        assertNotNull(userRepository);
        assertNotNull(passwordEncoder);
    }


    // 成功注册
    @Test
    void register_Success_SavesUser() {
        when(userRepository.existsByUsername("newUser")).thenReturn(false);
        when(passwordEncoder.encode("pass123")).thenReturn("encoded_pass");

        @jakarta.validation.Valid LoginRegisterRequest request = new LoginRegisterRequest("newUser", "pass123");
        userService.register(request);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        User savedUser = captor.getValue();
        assertEquals("newUser", savedUser.getUsername());
        assertEquals("encoded_pass", savedUser.getPassword());
    }

    // 成功登录
    @Test
    void login_Success_ReturnsTrue() {
        User mockUser = User.builder()
                .username("user")
                .password("encoded_pass")
                .build();
        when(userRepository.findByUsername("user")).thenReturn(mockUser);
        when(passwordEncoder.matches("password", "encoded_pass")).thenReturn(true);

        LoginRegisterRequest request = new LoginRegisterRequest("user", "password");
        assertTrue(userService.login(request));
        verify(passwordEncoder).matches("password", "encoded_pass");
    }

    // 普通登录注册测试
    @Test
    void normal_register_and_login() {
        // 配置注册阶段 Mock
        when(userRepository.existsByUsername("user")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encoded_password");

        // 执行注册
        @jakarta.validation.Valid LoginRegisterRequest regRequest = new LoginRegisterRequest("user", "password");
        userService.register(regRequest);

        // 验证注册保存的用户
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();
        assertEquals("user", savedUser.getUsername());
        assertEquals("encoded_password", savedUser.getPassword());

        // 配置登录阶段 Mock
        User mockUser = User.builder()
                .username("user")
                .password("encoded_password")
                .build();
        when(userRepository.findByUsername("user")).thenReturn(mockUser);
        when(passwordEncoder.matches("password", "encoded_password")).thenReturn(true);

        // 执行登录
        LoginRegisterRequest loginRegisterRequest = new LoginRegisterRequest("user", "password");
        assertTrue(userService.login(loginRegisterRequest));
    }


// ===================================== 异常测试 ===============================================================================


    // 用户名错误, 不存在用户名, 至少要抛出 userException 异常, 并且执行过 userRepository 的 findByUsername
    @Test
    void login_UserNotFound_ThrowsException() {
        when(userRepository.findByUsername("the_test_user")).thenReturn(null);
        LoginRegisterRequest request = new LoginRegisterRequest("the_test_user", "password");
        assertThrows(UserException.class, () -> userService.login(request));
        verify(userRepository).findByUsername("the_test_user");
    }

    // 登录密码错误 至少要抛出 userException 异常, 并且执行过 userRepository 的 findByUsername
    @Test
    void login_WrongPassword_ThrowsException() {
        User mockUser = User.builder()
                .username("user")
                .password("encoded_pass")
                .build();
        when(userRepository.findByUsername("user")).thenReturn(mockUser);
        when(passwordEncoder.matches("wrong_pass", "encoded_pass")).thenReturn(false);

        LoginRegisterRequest request = new LoginRegisterRequest("user", "wrong_pass");
        assertThrows(UserException.class,
                () -> userService.login(request));
        verify(userRepository).findByUsername("user");
    }

    // 验证非法用户名, 如果用户名非法, 那么你不应该保存到数据库里面, 也不用校验数据库里面是否存在这个用户名
    @ParameterizedTest
    @ValueSource(strings = {"####", "aa", "aaaa#"})
    void register_InvalidUser_ThrowsException(String username) {
        @jakarta.validation.Valid LoginRegisterRequest request = new LoginRegisterRequest(username, "password");

        assertThrows(UserException.class,
                () -> userService.register(request));

        verify(userRepository, never()).existsByUsername(anyString());
        verify(userRepository, never()).save(any());
    }

    // 注册遇到了重复的用户名, 你至少需要抛出 UserException 并且不能保存到数据库里
    @Test
    void register_DuplicateUsername_ThrowsException() {
        when(userRepository.existsByUsername("existingUser")).thenReturn(true);

        @jakarta.validation.Valid LoginRegisterRequest request = new LoginRegisterRequest("existingUser", "pass");
        assertThrows(UserException.class,
                () -> userService.register(request));

        verify(userRepository, never()).save(any());
    }


}
