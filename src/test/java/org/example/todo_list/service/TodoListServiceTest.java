package org.example.todo_list.service;

import org.example.todo_list.exception.ListException;
import org.example.todo_list.model.TodoList;
import org.example.todo_list.model.User;
import org.example.todo_list.repository.TodoListRepository;
import org.example.todo_list.repository.UserRepository;
import org.example.todo_list.security.JwtUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TodoListServiceTest {

    private final Long testUserId = 1L;
    private final String validCategory = "Work";
    @Mock
    private TodoListRepository todoListRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private JwtUtils jwtUtils; // 虽然测试中未使用，但需要保持依赖完整
    @InjectMocks
    private TodoListService todoListService;

    @Test
    void create_WithNewCategory_SavesNewList() {
        // 配置模拟行为
        when(todoListRepository.existsByCategory(validCategory, testUserId)).thenReturn(false);
        when(userRepository.findById(testUserId)).thenReturn(Optional.of(new User()));

        // 执行测试
        assertDoesNotThrow(() -> todoListService.create(validCategory, testUserId));

        // 验证交互
        verify(todoListRepository).save(any(TodoList.class));
    }

    @Test
    void create_WithExistingCategory_ThrowsListException() {
        // 配置模拟行为（网页6的异常模拟方式）
        when(todoListRepository.existsByCategory(validCategory, testUserId)).thenReturn(true);

        // 执行并验证异常
        assertThrows(ListException.class,
                () -> todoListService.create(validCategory, testUserId));
    }

    @Test
    void delete_WithValidId_RemovesList() {
        // 配置模拟对象链（网页9的依赖注入模式）
        TodoList mockList = new TodoList();
        when(todoListRepository.findById(anyLong())).thenReturn(Optional.of(mockList));
        when(userRepository.findById(testUserId)).thenReturn(Optional.of(new User()));

        // 执行测试
        assertDoesNotThrow(() -> todoListService.delete(1L, testUserId));

        // 验证删除操作
        verify(todoListRepository).deleteById(anyLong());
    }

    @Test
    void changeListCategory_WithNewCategory_UpdatesSuccessfully() {
        // 配置模拟行为（网页1的参数匹配方法）
        when(todoListRepository.existsByCategory("NewCategory", testUserId)).thenReturn(false);
        when(todoListRepository.updateCategoryById(any(), anyLong())).thenReturn(1);

        // 执行测试
        assertDoesNotThrow(() ->
                todoListService.changeListCategory(1L, "NewCategory", testUserId));
    }
//
//    @Test
//    void getAllLists_ReturnsFormattedResponses() {
//        // 准备测试数据（网页3的测试数据构造方式）
//        List<TodoList> mockLists = List.of(
//                TodoList.builder().id(1L).category("Work").build(),
//                TodoList.builder().id(2L).category("Personal").build()
//        );
//
//        // 配置模拟行为
//        when(todoListRepository.findTodoListByUserId(testUserId)).thenReturn(mockLists);
//
//        // 执行测试
//        List<GetListResponse> result = todoListService.getAllLists(testUserId);
//
//        // 验证结果
//        assertEquals(2, result.size());
//        assertEquals("Work", result.getFirst().category());
//    }

    @Test
    void getListById_WithInvalidId_ThrowsException() {
        // 配置模拟行为（网页8的异常抛出方式）
        when(todoListRepository.findById(999L)).thenReturn(Optional.empty());

        // 执行并验证异常
        assertThrows(ListException.class,
                () -> todoListService.getListById(999L));
    }
}
