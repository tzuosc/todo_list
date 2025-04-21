package org.example.todo_list.service;

import org.example.todo_list.dto.response.GetListResponse;
import org.example.todo_list.exception.ListException;
import org.example.todo_list.model.TodoList;
import org.example.todo_list.repository.TaskRepository;
import org.example.todo_list.repository.TodoListRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TodoListServiceTest {

    // region Test Data Builders
    private static final Long EXISTING_LIST_ID = 1L;
    private static final Long NON_EXISTING_LIST_ID = 999L;
    private static final String WORK_CATEGORY = "Work";
    private static final String PERSONAL_CATEGORY = "Personal";
    @Mock
    private TodoListRepository todoListRepository;
    @Mock
    private TaskRepository taskRepository;
    @InjectMocks
    private TodoListService todoListService;

    private List<Long> mockTaskIds(Long... ids) {
        return Arrays.asList(ids);
    }
    // endregion

    // region Create List Tests
    @Test
    void create_WithNewCategory_SavesNewList() {
        when(todoListRepository.existsByCategory(WORK_CATEGORY)).thenReturn(false);

        todoListService.create(WORK_CATEGORY);

        verify(todoListRepository).save(argThat(list ->
                list.getCategory().equals(WORK_CATEGORY) &&
                        list.getTasks().isEmpty()
        ));
    }

    @Test
    void create_WithExistingCategory_ThrowsDuplicateError() {
        when(todoListRepository.existsByCategory(WORK_CATEGORY)).thenReturn(true);

        ListException exception = assertThrows(ListException.class,
                () -> todoListService.create(WORK_CATEGORY));

        assertListException(exception, 3001, "重复类型的任务");
    }
    // endregion

    // region Delete List Tests
    @Test
    void delete_WithExistingId_DeletesSuccessfully() {
        when(todoListRepository.existsById(EXISTING_LIST_ID)).thenReturn(true);

        todoListService.delete(EXISTING_LIST_ID);

        verify(todoListRepository).deleteById(EXISTING_LIST_ID);
    }

    @Test
    void delete_WithNonExistingId_ThrowsNotFoundError() {
        when(todoListRepository.existsById(NON_EXISTING_LIST_ID)).thenReturn(false);

        ListException exception = assertThrows(ListException.class,
                () -> todoListService.delete(NON_EXISTING_LIST_ID));

        assertListException(exception, 3002, "没找到任务列表");
    }
    // endregion

    // region Update Category Tests
    @Test
    void changeListCategory_WithValidData_UpdatesSuccessfully() {
        when(todoListRepository.updateCategoryById("Updated", EXISTING_LIST_ID))
                .thenReturn(1);

        todoListService.changeListCategory(EXISTING_LIST_ID, "Updated");

        verify(todoListRepository).updateCategoryById("Updated", EXISTING_LIST_ID);
    }

    @Test
    void changeListCategory_WithNonExistingId_ThrowsNotFoundError() {
        when(todoListRepository.updateCategoryById(any(), anyLong())).thenReturn(0);

        ListException exception = assertThrows(ListException.class,
                () -> todoListService.changeListCategory(NON_EXISTING_LIST_ID, "Any"));

        assertListException(exception, 3002, "没找到任务列表");
    }

    @Test
    void changeListCategory_WithDuplicateCategory_ThrowsConflictError() {
        when(todoListRepository.existsByCategory("Duplicate")).thenReturn(true);

        ListException exception = assertThrows(ListException.class,
                () -> todoListService.changeListCategory(EXISTING_LIST_ID, "Duplicate"));

        assertListException(exception, 3003, "已存在类别"); // 根据实际错误码调整
    }
    // endregion

    // region Get Lists Tests
    @Test
    void getAllLists_WhenNoLists_ReturnsEmptyList() {
        when(todoListRepository.findAll()).thenReturn(Collections.emptyList());

        List<GetListResponse> result = todoListService.getAllLists();

        assertTrue(result.isEmpty(), "应返回空列表");
    }

    @Test
    void getAllLists_WithMultipleLists_ReturnsCorrectStructure() {
        // Arrange
        List<TodoList> mockLists = Arrays.asList(
                buildTodoListWithTasks(1L, WORK_CATEGORY, 101L, 102L),
                buildTodoListWithTasks(2L, PERSONAL_CATEGORY, 201L)
        );
        when(todoListRepository.findAll()).thenReturn(mockLists);

        // Mock task ID查询（根据category）
        when(taskRepository.findTaskIdsByCategory(WORK_CATEGORY))
                .thenReturn(mockTaskIds(101L, 102L));
        when(taskRepository.findTaskIdsByCategory(PERSONAL_CATEGORY))
                .thenReturn(mockTaskIds(201L));

        // Act
        List<GetListResponse> result = todoListService.getAllLists();

        // Assert
        assertAll(
                () -> assertEquals(2, result.size(), "列表数量不符"),
                () -> assertResponse(result.get(0), 1L, WORK_CATEGORY, 101L, 102L),
                () -> assertResponse(result.get(1), 2L, PERSONAL_CATEGORY, 201L)
        );
    }
    // endregion

    // region Get List by ID Tests
    @Test
    void getListById_WithExistingId_ReturnsCorrectData() {
        // Arrange
        TodoList mockList = buildTodoListWithTasks(EXISTING_LIST_ID, WORK_CATEGORY);
        when(todoListRepository.findById(EXISTING_LIST_ID))
                .thenReturn(Optional.of(mockList));
        when(taskRepository.findIdsByTodoList_Id(EXISTING_LIST_ID))
                .thenReturn(mockTaskIds(101L, 102L));

        // Act
        GetListResponse response = todoListService.getListById(EXISTING_LIST_ID);

        // Assert
        assertResponse(response, EXISTING_LIST_ID, WORK_CATEGORY, 101L, 102L);
    }

    @Test
    void getListById_WithNonExistingId_ThrowsNotFoundError() {
        when(todoListRepository.findById(NON_EXISTING_LIST_ID))
                .thenReturn(Optional.empty());

        ListException exception = assertThrows(ListException.class,
                () -> todoListService.getListById(NON_EXISTING_LIST_ID));

        assertListException(exception, 3002, "没找到任务列表");
    }
    // endregion

    // region Helper Methods
    private TodoList buildTodoListWithTasks(Long id, String category, Long... taskIds) {
        return TodoList.builder()
                .id(id)
                .category(category)
                .build();
    }

    private void assertListException(ListException exception,
                                     int expectedCode,
                                     String expectedMessage) {
        assertAll(
                () -> assertEquals(expectedCode, exception.getCode(), "错误码不匹配"),
                () -> assertTrue(exception.getMessage().contains(expectedMessage), "错误信息不匹配")
        );
    }

    private void assertResponse(GetListResponse response,
                                Long expectedId,
                                String expectedCategory,
                                Long... expectedTaskIds) {
        assertAll(
                () -> assertEquals(expectedId, response.id(), "ID不匹配"),
                () -> assertEquals(expectedCategory, response.category(), "分类不匹配"),
                () -> assertArrayEquals(expectedTaskIds, response.tasks().toArray(), "任务列表不匹配")
        );
    }
    // endregion
}
