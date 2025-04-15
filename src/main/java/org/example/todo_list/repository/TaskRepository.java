package org.example.todo_list.repository;

import jakarta.validation.constraints.NotBlank;
import org.example.todo_list.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
    boolean existsByName(@NotBlank String name);
}
