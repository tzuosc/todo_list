package org.example.todo_list.repository;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.example.todo_list.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    @Transactional
    @Modifying
    @Query("update Task t set t.status = ?1 where t.id = ?2")
    int updateStatusById(@NonNull boolean status, @NonNull Long id);

    boolean existsByName(@NotBlank String name);

    boolean existsById(@NotNull Long id);

    @Transactional
    @Modifying
    @Query("update Task t set t.deadline = ?1 where t.id = ?2")
    int updateTaskDateById(@NonNull Long taskDate, @NonNull Long id);


    @Query("select t.id from Task t where t.todoList.category = ?1")
    List<Long> findTaskIdsByCategory(@NonNull String category);


    @Query("select t.id from Task t where t.todoList.id = ?1")
    List<Long> findIdsByTodoList_Id(Long id);
}
