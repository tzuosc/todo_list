//package org.example.todo_list.repository;
//
//
//import org.example.todo_list.model.Task;
//import org.example.todo_list.model.TodoList;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@DataJpaTest
//class TaskRepositoryTest {
//
//    @Autowired
//    private TestEntityManager entityManager;
//
//    @Autowired
//    private TaskRepository taskRepository;
//
//    @Test
//    void updateStatusById_UpdatesTaskStatus() {
//        Task task = new Task();
//        task.setStatus(false);
//        entityManager.persistAndFlush(task);
//
//        int updated = taskRepository.updateStatusById(true, task.getId());
//        assertEquals(1, updated);
//
//        Task updatedTask = entityManager.find(Task.class, task.getId());
//        assertTrue(updatedTask.isStatus());
//    }
//
//    @Test
//    void existsByName_ReturnsTrueWhenNameExists() {
//        Task task = new Task();
//        task.setName("Important Task");
//        entityManager.persistAndFlush(task);
//
//        assertTrue(taskRepository.existsByName("Important Task"));
//        assertFalse(taskRepository.existsByName("Non-existent Task"));
//    }
//
//    @Test
//    void updateTaskDateById_UpdatesDeadline() {
//        Task task = new Task();
//        LocalDateTime newDate = LocalDateTime.now();
//        entityManager.persistAndFlush(task);
//
//        int updated = taskRepository.updateTaskDateById(newDate, task.getId());
//        assertEquals(1, updated);
//
//        Task updatedTask = entityManager.find(Task.class, task.getId());
//        assertEquals(newDate, updatedTask.getDeadline());
//    }
//
//    @Test
//    void findTaskIdsByCategory_ReturnsCorrectIds() {
//        TodoList todoList = new TodoList();
//        todoList.setCategory("Work");
//        entityManager.persistAndFlush(todoList);
//
//        Task task1 = new Task();
//        task1.setTodoList(todoList);
//        Task task2 = new Task();
//        task2.setTodoList(todoList);
//        entityManager.persist(task1);
//        entityManager.persist(task2);
//        entityManager.flush();
//
//        List<Long> ids = taskRepository.findTaskIdsByCategory("Work");
//        assertEquals(2, ids.size());
//        assertTrue(ids.contains(task1.getId()));
//        assertTrue(ids.contains(task2.getId()));
//    }
//}
