package org.example.todo_list.service;

import lombok.RequiredArgsConstructor;
import org.example.todo_list.dto.request.LoginRequest;
import org.example.todo_list.dto.request.RegisterRequest;
import org.example.todo_list.exception.AuthException;
import org.example.todo_list.exception.errors.UserError;
import org.example.todo_list.model.User;
import org.example.todo_list.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    public boolean login(LoginRequest request) {

        User user = userRepository.findByUsername(request.username());

        // 如果用户名不存在
        if (user == null) {
            throw new AuthException(
                    UserError.INVALID_PASSWD_USER.getCode(),
                    UserError.INVALID_PASSWD_USER.getMessage());
        }

        // 验证密码是错误
        boolean judge = passwordEncoder.matches(request.password(), user.getPassword());

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

        // 如果存在相同的用户名
        if (userRepository.existsByUsername(request.username())) {
            throw new AuthException(
                    UserError.DUPLICATE_USERNAME.getCode(),
                    UserError.DUPLICATE_USERNAME.getMessage()
            );
        }

        // md5 加密密码
        String encodedPassword = passwordEncoder.encode(request.password());

        User user = User.builder()
                .avatarUrl(request.avatarUrl())
                .username(request.username())
                .password(encodedPassword)
                .build();

        userRepository.save(user);
    }
}
