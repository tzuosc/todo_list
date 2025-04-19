package org.example.todo_list.service;

import org.example.todo_list.dto.request.LoginRegisterRequest;
import org.example.todo_list.dto.request.UpdateUserRequest;

public interface InterUserService {
    public boolean login(LoginRegisterRequest loginRegisterRequest);

    public void register(LoginRegisterRequest loginRegisterRequest);

    public void updateUser(Long id, UpdateUserRequest updateUserRequest);

}
