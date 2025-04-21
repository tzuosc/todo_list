package org.example.todo_list.service;

import org.example.todo_list.dto.request.CreateTaskRequest;
import org.example.todo_list.dto.request.UpdateTaskRequest;
import org.example.todo_list.exception.TaskException;
import org.example.todo_list.model.Task;
import org.example.todo_list.model.TodoList;
import org.example.todo_list.repository.TaskRepository;
import org.example.todo_list.repository.TodoListRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TodoListRepository todoListRepository;

    @Mock
    private TodoListService todoListService;

    @InjectMocks
    private TaskService taskService;
    @Mock
    private TodoList mockTodoList;

    // 生成器
    private static CreateTaskRequest.CreateTaskRequestBuilder validCreateRequest() {
        return CreateTaskRequest.builder()
                .name("ValidTask")
                .category("Work")
                .status(false)
                .deadline(System.currentTimeMillis() / 1000 + 3600)
                .taskDescription("Valid Description");
    }

    private static Stream<Arguments> invalidCreateRequests() {
        return Stream.of(
                Arguments.of(validCreateRequest()
                        .name("test")
                        .status(false)
                        .category("test")
                        .build()),
                Arguments.of(validCreateRequest()
                        .category("test")
                        .status(false)
                        .deadline(System.currentTimeMillis() / 1000 + 30)
                        .build()),
                Arguments.of(validCreateRequest()
                        .category("test")
                        .name("test")
                        .deadline(System.currentTimeMillis() / 1000 + 30)
                        .status(false)
                        .taskDescription("Valid Description")
                        .build()),
                Arguments.of(validCreateRequest()
                        .name("test")
                        .status(false)
                        .category("test")
                        .taskDescription("Valid Description")
                        .build())
        );
    }


    @ParameterizedTest
    @MethodSource("invalidCreateRequests")
    void updateTask_Normal(CreateTaskRequest createTaskRequest) {
        when(taskRepository.existsByName(anyString())).thenReturn(false);
        when(todoListRepository.existsByCategory("test")).thenReturn(true);
        when(todoListRepository.findByCategory("test")).thenReturn(mockTodoList);
        taskService.createTask(createTaskRequest);
        verify(taskRepository).save(any(Task.class));
        verify(mockTodoList).addTask(any(Task.class));
    }

    @Test
    void contextLoad() {
        assertNotNull(todoListRepository);
        assertNotNull(taskRepository);
        assertNotNull(taskService);
        assertNotNull(mockTodoList);
        assertNotNull(todoListService);
    }

    //      测试新建任务使用新的类别, 如果没有对应的类别, 自动新建一个类别
    @Test
    void createTask_WithNewCategory_AddsToTodoList() {
        CreateTaskRequest request = validCreateRequest().category("NewCategory").build();

        when(taskRepository.existsByName(anyString())).thenReturn(false);
        when(todoListRepository.existsByCategory("NewCategory")).thenReturn(false);
        when(todoListRepository.findByCategory("NewCategory")).thenReturn(mockTodoList);

        taskService.createTask(request);

        verify(todoListService).create("NewCategory");
        verify(mockTodoList).addTask(any(Task.class));
        verify(taskRepository).save(any(Task.class));
    }

    // 测试新建任务使用已经存在的类别, 不自动新建类别
    @Test
    void createTask_WithExistCategory_AddsToTodoList() {
        CreateTaskRequest request = validCreateRequest().category("NewCategory").build();
        when(taskRepository.existsByName(anyString())).thenReturn(false);
        when(todoListRepository.existsByCategory("NewCategory")).thenReturn(true);
        when(todoListRepository.findByCategory("NewCategory")).thenReturn(mockTodoList);
        taskService.createTask(request);
        // 没有重复新建对应的类别
        verify(todoListService, never()).create("NewCategory");
        verify(mockTodoList).addTask(any(Task.class));
        verify(taskRepository).save(any(Task.class));
    }

    // 测试新建任务的时间的最小边界情况
    @Test
    void createTask_WithMinimumValidValues_Succeeds() {
        CreateTaskRequest request = CreateTaskRequest.builder()
                .name("A")
                .category("B")
                .status(false)
                .deadline(System.currentTimeMillis() / 1000 + 1)
                .build();

        when(taskRepository.existsByName("A")).thenReturn(false);
        when(todoListRepository.existsByCategory("B")).thenReturn(true);
        when(todoListRepository.findByCategory("B")).thenReturn(mockTodoList);

        assertDoesNotThrow(() -> taskService.createTask(request));
    }

    @Test
    void createTask_WithPastDeadline_ThrowsNotFutureTimeException() {
        long pastTime = System.currentTimeMillis() / 1000 - 1;
        CreateTaskRequest request = validCreateRequest().deadline(pastTime).build();

        TaskException exception = assertThrows(TaskException.class,
                () -> taskService.createTask(request));

        assertEquals(2003, exception.getCode());
        assertEquals("不是将来的时间", exception.getMessage());
    }

    //测试跟新任务使用过去的时间
    @Test
    void updateTask_WithPastDeadline_ThrowsNotFutureTimeException() {
        Task existingTask = Task.builder()
                .id(1L)
                .deadline(System.currentTimeMillis() / 1000 + 3600)
                .build();

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));

        UpdateTaskRequest request = UpdateTaskRequest.builder()
                .deadline(System.currentTimeMillis() / 1000 - 1)
                .build();

        TaskException exception = assertThrows(TaskException.class,
                () -> taskService.updateTask(1L, request));

        assertEquals(2003, exception.getCode());
        assertEquals("不是将来的时间", exception.getMessage());
    }

    // 测试时间戳表示的日期超过了 2038 年
    @Test
    void updateTask_WithInvalidDeadline_ThrowsInvalidTimeException() {
        Task existingTask = Task.builder()
                .id(1L)
                .deadline(System.currentTimeMillis() / 1000 + 3600)
                .build();

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));

        UpdateTaskRequest request = UpdateTaskRequest.builder()
                .deadline(2147483647L + 100L)
                .build();

        TaskException exception = assertThrows(TaskException.class,
                () -> taskService.updateTask(1L, request));

        assertEquals(2005, exception.getCode());
        assertEquals("非法时间", exception.getMessage());
    }

    @Test
    void updateTask_WithNonExistingId_ThrowsTaskNotFoundException() {
        when(taskRepository.findById(999L)).thenReturn(Optional.empty());

        UpdateTaskRequest request = UpdateTaskRequest.builder().build();

        TaskException exception = assertThrows(TaskException.class,
                () -> taskService.updateTask(999L, request));

        assertEquals(2002, exception.getCode());
        assertEquals("没找到任务", exception.getMessage());
    }


    @Test
    void getTask_WithNonExistingId_ThrowsTaskNotFoundException() {
        when(taskRepository.findById(999L)).thenReturn(Optional.empty());

        TaskException exception = assertThrows(TaskException.class,
                () -> taskService.getTask(999L));

        assertEquals(2002, exception.getCode());
        assertEquals("没找到任务", exception.getMessage());
    }

    @Test
    void deleteTask_WithNonExistingId_ThrowsTaskNotFoundException() {
        when(taskRepository.existsById(999L)).thenReturn(false);

        TaskException exception = assertThrows(TaskException.class,
                () -> taskService.deleteTask(999L));

        assertEquals(2002, exception.getCode());
        assertEquals("没找到任务", exception.getMessage());
    }
}
