package org.example.todo_list.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.todo_list.dto.request.LoginRequest;
import org.example.todo_list.dto.request.RegisterRequest;
import org.example.todo_list.exception.UserException;
import org.example.todo_list.exception.errors.UserError;
import org.example.todo_list.model.User;
import org.example.todo_list.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;


    public boolean login(LoginRequest request) {

        User user = userRepository.findByUsername(request.username());

        // 如果用户名不存在
        if (user == null) {
            throw new UserException(
                    UserError.INVALID_PASSWD_USER.getCode(),
                    UserError.INVALID_PASSWD_USER.getMessage());
        }

        // 验证密码是错误
        boolean judge = passwordEncoder.matches(request.password(), user.getPassword());

        // 如果密码错误
        if (!judge) {
            throw new UserException(
                    UserError.INVALID_PASSWD_USER.getCode(),
                    UserError.INVALID_PASSWD_USER.getMessage()
            );
        }
        return true;
    }

    public void register(RegisterRequest request) {

        // 如果注册用户名使用非法字符
        String username = request.username();
        if (!username.matches("[a-zA-Z0-9_]{3,15}")) {
            // 可在此处打印异常信息
            log.warn("非法用户名是: {}", request.username());

            throw new UserException(
                    UserError.INVALID_USERNAME.getCode(),
                    UserError.INVALID_USERNAME.getMessage()
            );
        }

        // 如果存在相同的用户名
        if (userRepository.existsByUsername(request.username())) {
            throw new UserException(
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
