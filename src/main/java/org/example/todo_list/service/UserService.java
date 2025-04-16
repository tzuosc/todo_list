package org.example.todo_list.service;

import lombok.RequiredArgsConstructor;
import org.example.todo_list.dto.request.LoginRequest;
import org.example.todo_list.dto.request.RegisterRequest;
import org.example.todo_list.exception.AuthException;
import org.example.todo_list.exception.errors.UserError;
import org.example.todo_list.model.User;
import org.example.todo_list.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final TaskService taskService;

    public boolean login(LoginRequest request) {
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
        return true;
    }

    public void register(RegisterRequest request) {
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
    }
}
