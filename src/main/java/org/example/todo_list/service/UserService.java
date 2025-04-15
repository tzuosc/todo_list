package org.example.todo_list.service;

import lombok.RequiredArgsConstructor;
import org.example.todo_list.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

//    public boolean login(String username, String password) {
//        return userRepository.existsByUsernameAndPassword(username, password);
//    }
}
