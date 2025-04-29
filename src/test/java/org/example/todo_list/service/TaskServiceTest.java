package org.example.todo_list.service;

import org.example.todo_list.dto.request.CreateTaskRequest;
import org.example.todo_list.dto.request.UpdateTaskRequest;
import org.example.todo_list.exception.TaskException;
import org.example.todo_list.model.Task;
import org.example.todo_list.repository.TaskRepository;
import org.example.todo_list.repository.TodoListRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    // 在测试类中添加时间工具
    private static final long FIXED_TIMESTAMP = 1735689600L; // 2025-01-01 00:00:00
    private final Long testUserId = 1L;
    private final String validCategory = "Work";
    private final Long validDeadline = 2147483646L; // 2038年临界值
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private TodoListRepository todoListRepository;
    @Mock
    private TodoListService todoListService;
    @InjectMocks
    private TaskService taskService;


    @Test
    void createTask_WithExpiredDeadline_ThrowsException() {
        CreateTaskRequest request = new CreateTaskRequest(
                "Bug Fix", "Work", true, "Fix critical bug", System.currentTimeMillis() / 1000 - 3600
        );

        assertThrows(TaskException.class,
                () -> taskService.createTask(request, testUserId));
    }

    @Test
    void getTask_WithExistingId_ReturnsResponse() {
        // 准备测试数据
        Task mockTask = Task.builder()
                .id(1L).name("Code Review").status(true).deadline(validDeadline).build();

        when(taskRepository.findById(1L)).thenReturn(Optional.of(mockTask));

        // 执行测试
        var response = taskService.getTask(1L);

        // 验证结果
        assertEquals("Code Review", response.name());
        assertTrue(response.status());
    }

    @Test
    void getTask_WithInvalidId_ThrowsException() {
        when(taskRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(TaskException.class,
                () -> taskService.getTask(999L));
    }

    @Test
    void updateTask_ChangeCategory_CreatesNewTodoList() {
        // 准备更新请求
        UpdateTaskRequest request = new UpdateTaskRequest(
                "New Category", "Updated Task", true, "New Desc", validDeadline
        );

        // 模拟现有任务和分类不存在
        Task existingTask = new Task();
        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));
        when(todoListRepository.existsByCategory("New Category", testUserId)).thenReturn(false);

        // 执行测试
        assertDoesNotThrow(() -> taskService.updateTask(1L, request, testUserId));

        // 验证交互
        verify(todoListService).create("New Category", testUserId);
        verify(taskRepository).save(existingTask);
    }

    @Test
    void updateTask_WithInvalidTime_ThrowsException() {
        UpdateTaskRequest request = new UpdateTaskRequest(
                null, null, null, null, 2147483648L
        );

        assertThrows(TaskException.class,
                () -> taskService.updateTask(1L, request, testUserId));
    }

    @Test
    void deleteTask_WithExistingId_DeletesRecord() {
        when(taskRepository.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> taskService.deleteTask(1L));
        verify(taskRepository).deleteById(1L);
    }

    @Test
    void deleteTask_WithNonExistingId_ThrowsException() {
        when(taskRepository.existsById(999L)).thenReturn(false);

        assertThrows(TaskException.class,
                () -> taskService.deleteTask(999L));
    }
}
