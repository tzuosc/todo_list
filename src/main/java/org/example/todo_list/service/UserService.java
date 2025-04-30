package org.example.todo_list.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.todo_list.dto.request.LoginRegisterRequest;
import org.example.todo_list.dto.request.UpdateUserRequest;
import org.example.todo_list.exception.UserException;
import org.example.todo_list.exception.errors.UserError;
import org.example.todo_list.model.User;
import org.example.todo_list.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public boolean login(LoginRegisterRequest request) {

        /*
         * TODO 登录
         *    你需要处理
         *    - 如果用户名不存在
         *    - 如果密码错误
         * */


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

    public String storeFile(MultipartFile file) throws IOException {
        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path path = Paths.get(uploadDir).resolve(filename);
        Files.createDirectories(path.getParent());
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        return filename;
    }

    public void updateUser(Long id, UpdateUserRequest newUser) {
        /*TODO
         *  更新用户
         *  你需要处理的业务异常与注册差不多. 这是增量更新, 只需要判断需要更新的字段的业务异常就可以了
         * */

        userRepository.findById(id).ifPresentOrElse(
                user -> {
                    if (newUser.username() != null) {
                        if (!newUser.username().matches("[a-zA-Z0-9_]{3,15}")) {
                            throw new UserException(
                                    UserError.INVALID_USERNAME.getCode(),
                                    UserError.INVALID_USERNAME.getMessage()
                            );
                        }
                        user.setUsername(newUser.username());
                    }
                    if (newUser.password() != null) {
                        String encodedPassword = passwordEncoder.encode(newUser.password());
                        user.setPassword(encodedPassword);
                    }
//                    if (newUser.avatar() != null) {
//
//                        try {
//                            storeFile(newUser.avatar());
//                        } catch (IOException e) {
//                            throw new RuntimeException(e);
//                        }
//                    }
                    userRepository.save(user);
                },
                () -> {
                    throw new UserException(
                            UserError.NO_USER.getCode(),
                            UserError.NO_USER.getMessage()
                    );
                }
        );
    }

    public String upload(Long id, MultipartFile file) throws IOException {
        if (file.isEmpty()) throw new UserException(UserError.EMPTY_FILE.getCode(), UserError.EMPTY_FILE.getMessage());
        try {
            // 保存文件到本地
            String filename = file.getOriginalFilename();
            Path path = Paths.get(uploadDir + filename);
            Files.createDirectories(path.getParent());
            Files.write(path, file.getBytes());
            userRepository.findById(id).ifPresent(user -> {
                user.setAvatarUrl(uploadDir + filename);
            });
            return uploadDir + filename;
        } catch (IOException exception) {
            log.error(exception.getMessage());
            return "上传失败";
        }
    }
}
