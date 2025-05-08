package org.example.todo_list.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.todo_list.dto.request.LoginRegisterRequest;
import org.example.todo_list.exception.UserException;
import org.example.todo_list.exception.errors.UserError;
import org.example.todo_list.model.User;
import org.example.todo_list.repository.JPA.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Value("${file.access-path}")
    private String accessPath;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public void register(LoginRegisterRequest request) {

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
                .username(request.username())
                .password(encodedPassword)
                .build();

        userRepository.save(user);
    }


//    public boolean login(LoginRegisterRequest request) {
//
//        /*
//         * TODO 登录
//         *    你需要处理
//         *    - 如果用户名不存在
//         *    - 如果密码错误
//         * */
//
//    }
//
//    public String storeFile(Long userId, MultipartFile file) throws IOException {
//       // TODO 存储头像图片. 随意你存储在哪里, 只要最终可以通过 http://localhost:8080/images/文件名 这个地址访问到对应的图片就算成功
//
//    }
//
//    public void updateUser(Long id, UpdateUserRequest newUser) {
//        /*TODO
//         *  更新用户
//         *  你需要处理的业务异常与注册差不多. 这是增量更新, 只需要判断需要更新的字段的业务异常就可以了
//         * */
//
//    }


}
