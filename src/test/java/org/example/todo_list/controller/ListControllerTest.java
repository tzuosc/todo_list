package org.example.todo_list.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.todo_list.config.TestSecurityConfig;
import org.example.todo_list.dto.response.GetListResponse;
import org.example.todo_list.exception.ListException;
import org.example.todo_list.exception.errors.ListError;
import org.example.todo_list.service.TodoListService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(
        controllers = TodoListController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class  // 排除安全自动配置
)
@Import({TestSecurityConfig.class, TestConfig.class})
@AutoConfigureMockMvc(addFilters = false)
public class ListControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TodoListService todoListService;

    @Autowired
    private ObjectMapper objectMapper;

    // region 创建任务列表测试
    @Test
    void createList_Success() throws Exception {
        // Given
        String category = "work";

        // When & Then
        mockMvc.perform(put("/list/{category}", category)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value("创建成功"));

        Mockito.verify(todoListService).create(category);
    }
    // endregion

    // region 删除任务列表测试
    @Test
    void deleteList_Success() throws Exception {
        // Given
        Long listId = 1L;

        // When & Then
        mockMvc.perform(delete("/list/{id}", listId)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value("删除成功"));

        Mockito.verify(todoListService).delete(listId);
    }

    @Test
    void deleteList_NotFound() throws Exception {
        // Given
        Long invalidId = 999L;
        Mockito.doThrow(new ListException(
                        ListError.LIST_NOT_FOUND.getCode(),
                        ListError.LIST_NOT_FOUND.getMessage()
                ))
                .when(todoListService).delete(invalidId);

        // When & Then
        mockMvc.perform(delete("/list/{id}", invalidId)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("列表不存在"));
    }
    // endregion

    // region 修改列表类别测试
    @Test
    void changeCategory_Success() throws Exception {
        // Given
        Long listId = 1L;
        String newCategory = "personal";

        // When & Then
        mockMvc.perform(patch("/list/change_category/{id}", listId)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .param("newCategory", newCategory))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value("修改成功"));

        Mockito.verify(todoListService).changeListCategory(listId, newCategory);
    }

    @Test
    void changeCategory_EmptyCategory() throws Exception {
        // Given
        Long listId = 1L;
        String invalidCategory = "";

        // When & Then
        mockMvc.perform(patch("/list/change_category/{id}", listId)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .param("newCategory", invalidCategory))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("aaa"));
    }
    // endregion

    // region 获取所有列表测试
    @Test
    void fetchAllLists_Success() throws Exception {
        // Given
        List<GetListResponse> mockLists = List.of(
                new GetListResponse(1L, "work", List.of(1L, 2L)),
                new GetListResponse(2L, "personal", List.of(3L))
        );
        Mockito.when(todoListService.getAllLists()).thenReturn(mockLists);

        // When & Then
        mockMvc.perform(get("/list/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].category").value("work"))
                .andExpect(jsonPath("$.data[1].tasks.length()").value(1));
    }
    // endregion

    // region 根据ID获取列表测试
    @Test
    void getListById_Success() throws Exception {
        // Given
        Long listId = 1L;
        GetListResponse mockResponse = new GetListResponse(
                listId, "work", List.of(1L, 2L)
        );
        Mockito.when(todoListService.getListById(listId)).thenReturn(mockResponse);

        // When & Then
        mockMvc.perform(get("/list/{id}", listId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.tasks.length()").value(2));
    }

    @Test
    void getListById_NotFound() throws Exception {
        // Given
        Long invalidId = 999L;
        Mockito.when(todoListService.getListById(invalidId))
                .thenThrow(new ListException(
                        ListError.LIST_NOT_FOUND.getCode(),
                        ListError.LIST_NOT_FOUND.getMessage()));

        // When & Then
        mockMvc.perform(get("/list/{id}", invalidId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("列表不存在"));
    }
    // endregion
}
