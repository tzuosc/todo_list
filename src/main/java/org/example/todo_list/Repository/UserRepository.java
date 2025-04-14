package org.example.todo_list.Repository;

import org.example.todo_list.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
