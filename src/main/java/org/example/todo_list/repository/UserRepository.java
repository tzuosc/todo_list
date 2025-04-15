package org.example.todo_list.repository;

import org.example.todo_list.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends
        JpaRepository<User, Long>,
        JpaSpecificationExecutor<User> {
    boolean existsByUsernameAndPassword(String username, String password);

    boolean existsByUsername(String username);
}
