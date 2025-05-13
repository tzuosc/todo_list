package org.example.todo_list.repository.jpa;

import org.example.todo_list.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {


    @Transactional
    @Query("select t.id from Task t where t.todoList.id = ?1")
    List<Long> findIdsByTodoList_Id(Long id);

    boolean existsByName(String name);

    boolean existsById(Long id);
}
