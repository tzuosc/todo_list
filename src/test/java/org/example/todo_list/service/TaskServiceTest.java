//package org.example.todo_list.service;
//
//import org.example.todo_list.dto.request.CreateTaskRequest;
//import org.example.todo_list.dto.response.GetTaskResponse;
//import org.example.todo_list.exception.TaskException;
//import org.example.todo_list.model.Task;
//import org.example.todo_list.model.TodoList;
//import org.example.todo_list.repository.TaskRepository;
//import org.example.todo_list.repository.TodoListRepository;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.time.LocalDateTime;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class TaskServiceTest {
//
//    @Mock
//    private TaskRepository taskRepository;
//
//    @Mock
//    private TodoListRepository todoListRepository;
//
//    @Mock
//    private TodoListService todoListService;
//
//    @InjectMocks
//    private TaskService taskService;
//
//    @Test
//    void createTask_DuplicateName_ThrowsException() {
//        // 模拟存在重复任务名
//        when(taskRepository.existsByName("ExistingTask")).thenReturn(true);
//
//        CreateTaskRequest request = new CreateTaskRequest(
//                "ExistingTask", "Work", false, "Description", LocalDateTime.now()
//        );
//        TaskException exception = assertThrows(TaskException.class, () -> taskService.createTask(request));
//
//        assertEquals(20000, exception.getCode());
//        assertEquals("任务名重复", exception.getMessage());
//        verify(taskRepository, never()).save(any());  // 确保不保存
//    }
//
//    @Test
//    void createTask_NewCategory_CreatesTodoList() {
//        // 模拟分类不存在，且新建分类
//        when(taskRepository.existsByName("NewTask")).thenReturn(false);
//        when(todoListRepository.existsByCategory("Work")).thenReturn(false);
//        when(todoListRepository.findByCategory("Work")).thenReturn(new TodoList());
//
//        CreateTaskRequest request = new CreateTaskRequest(
//                "NewTask", "Work", true, "Desc", LocalDateTime.now()
//        );
//        taskService.createTask(request);
//
//        verify(todoListService).create("Work");  // 验证分类创建逻辑
//        verify(taskRepository).save(argThat(task ->
//                "NewTask".equals(task.getName()) &&
//                        "Desc".equals(task.getDescription()) &&
//                        task.getTodoList() != null
//        ));
//    }
//
//    @Test
//    void getTask_NotFound_ThrowsException() {
//        when(taskRepository.findById(999L)).thenReturn(Optional.empty());
//
//        TaskException exception = assertThrows(TaskException.class, () -> taskService.getTask(999L));
//        assertEquals(1005, exception.getCode());
//        assertEquals("没找到任务", exception.getMessage());
//    }
//
//    @Test
//    void getTask_Found_ReturnsResponse() {
//        Task mockTask = Task.builder()
//                .id(1L)
//                .name("Task1")
//                .description("Test")
//                .deadline(LocalDateTime.of(2023, 10, 1, 12, 0))
//                .status(true)
//                .build();
//        when(taskRepository.findById(1L)).thenReturn(Optional.of(mockTask));
//
//        GetTaskResponse response = taskService.getTask(1L);
//        assertEquals("Task1", response.name());
//        assertEquals("Test", response.description());
//        assertTrue(response.status());
//    }
//
//    @Test
//    void changeStatus_TaskNotFound_ThrowsException() {
//        when(taskRepository.updateStatusById(true, 999L)).thenReturn(0);
//
//        TaskException exception = assertThrows(TaskException.class,
//                () -> taskService.changeStatus(999L, true));
//        assertEquals(1005, exception.getCode());
//    }
//
//    @Test
//    void deleteTask_TaskNotFound_ThrowsException() {
//        when(taskRepository.existsById(999L)).thenReturn(false);
//
//        TaskException exception = assertThrows(TaskException.class,
//                () -> taskService.deleteTask(999L));
//        verify(taskRepository, never()).deleteById(999L);
//    }
//
//    @Test
//    void deleteTask_Success_DeletesTask() {
//        when(taskRepository.existsById(1L)).thenReturn(true);
//        taskService.deleteTask(1L);
//        verify(taskRepository).deleteById(1L);
//    }
//
//    @Test
//    void changeDeadline_TaskNotFound_ThrowsException() {
//        LocalDateTime newDeadline = LocalDateTime.now();
//        when(taskRepository.updateTaskDateById(newDeadline, 999L)).thenReturn(0);
//
//        TaskException exception = assertThrows(TaskException.class,
//                () -> taskService.changeDeadline(999L, newDeadline));
//        assertEquals(1005, exception.getCode());
//    }
//}
