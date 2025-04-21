package org.example.todo_list.service;

import org.example.todo_list.dto.request.LoginRegisterRequest;
import org.example.todo_list.dto.request.UpdateUserRequest;

public interface InterUserService {
    boolean login(LoginRegisterRequest loginRegisterRequest);

    void register(LoginRegisterRequest loginRegisterRequest);

    void updateUser(Long id, UpdateUserRequest updateUserRequest);

}
