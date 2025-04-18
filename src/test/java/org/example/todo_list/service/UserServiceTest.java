package org.example.todo_list.service;


import org.example.todo_list.dto.request.LoginRequest;
import org.example.todo_list.dto.request.RegisterRequest;
import org.example.todo_list.exception.UserException;
import org.example.todo_list.model.User;
import org.example.todo_list.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TaskService taskService; // 虽未使用，但需保持依赖关系

    @InjectMocks
    private UserService userService;

    @Test
    void login_UserNotFound_ThrowsException() {
        // 模拟用户不存在
        when(userRepository.findByUsername("testUser")).thenReturn(null);

        LoginRequest request = new LoginRequest("testUser", "password");
        UserException exception = assertThrows(UserException.class, () -> userService.login(request));

        assertEquals(1001, exception.getCode());
        assertEquals("用户不存在", exception.getMessage());
    }

    @Test
    void login_InvalidPassword_ThrowsException() {
        User mockUser = User.builder().username("testUser").build();
        when(userRepository.findByUsername("testUser")).thenReturn(mockUser);
        when(userRepository.existsByUsernameAndPassword("testUser", "wrongPass")).thenReturn(false);

        LoginRequest request = new LoginRequest("testUser", "wrongPass");
        UserException exception = assertThrows(UserException.class, () -> userService.login(request));

        assertEquals(1002, exception.getCode());
        assertEquals("用户名或者密码错误", exception.getMessage());
    }

    @Test
    void login_Success_ReturnsTrue() {
        User mockUser = User.builder().username("testUser").build();
        when(userRepository.findByUsername("testUser")).thenReturn(mockUser);
        when(userRepository.existsByUsernameAndPassword("testUser", "correctPass")).thenReturn(true);

        LoginRequest request = new LoginRequest("testUser", "correctPass");
        assertTrue(userService.login(request));
    }

    @Test
    void register_DuplicateUsername_ThrowsException() {
        when(userRepository.existsByUsername("existingUser")).thenReturn(true);

        RegisterRequest request = new RegisterRequest("existingUser", "pass", "avatar.jpg");
        UserException exception = assertThrows(UserException.class, () -> userService.register(request));

        assertEquals(1003, exception.getCode());
        assertEquals("用户名已经存在", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void register_Success_SavesUser() {
        when(userRepository.existsByUsername("newUser")).thenReturn(false);

        RegisterRequest request = new RegisterRequest("newUser", "pass", "avatar.jpg");
        userService.register(request);

        verify(userRepository).save(argThat(user ->
                user.getUsername().equals("newUser") &&
                        user.getPassword().equals("pass") &&
                        user.getAvatarUrl().equals("avatar.jpg")
        ));
    }
}
