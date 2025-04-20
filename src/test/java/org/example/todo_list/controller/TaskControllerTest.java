package org.example.todo_list.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.todo_list.dto.request.CreateTaskRequest;
import org.example.todo_list.dto.request.UpdateTaskRequest;
import org.example.todo_list.dto.response.GetTaskResponse;
import org.example.todo_list.service.TaskService;
import org.example.todo_list.utils.ApiResponse;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(TestConfig.class)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @InjectMocks
    private TaskService taskService;

    @Test
    public void contextLoads() {
        assertNotNull(mockMvc);
        assertNotNull(objectMapper);
        assertNotNull(taskService);
    }

    @WithMockUser
    @Test
    void createTask_Success() throws Exception {
        CreateTaskRequest request = new CreateTaskRequest(
                "work",
                "task1",
                false,
                "description",
                1830268799L
        );

        mockMvc.perform(post("/task")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        Mockito.verify(taskService).createTask(request);
    }

    @Test
    void getTask_Success() throws Exception {
        Long taskId = 1L;
        GetTaskResponse mockResponse = GetTaskResponse.builder()
                .id(taskId)
                .name("task1")
                .description("description")
                .status(false)
                .deadline(1830268799L)
                .build();

        Mockito.when(taskService.getTask(taskId)).thenReturn(mockResponse);

        MvcResult result = mockMvc.perform(get("/task/{id}", taskId))
                .andExpect(status().isOk())
                .andReturn();

        // 使用 TypeReference 明确泛型类型
        ApiResponse<GetTaskResponse> response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<ApiResponse<GetTaskResponse>>() {
                }
        );

        // 确保比较的是 GetTaskResponse 对象
        assertEquals(mockResponse, response.getData());
    }

    @Test
    void updateTask_Success() throws Exception {
        Long taskId = 1L;
        UpdateTaskRequest request = new UpdateTaskRequest(
                "updatedWork",
                "updatedTask",
                true,
                "updatedDesc",
                2177423998L
        );

        mockMvc.perform(patch("/task/{id}", taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        Mockito.verify(taskService).updateTask(taskId, request);
    }

    @Test
    void deleteTask_Success() throws Exception {
        Long taskId = 1L;

        MvcResult result = mockMvc.perform(delete("/task/{id}", taskId))
                .andExpect(status().isOk())
                .andReturn();

        ApiResponse<?> response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                ApiResponse.class
        );
        assertEquals("success", response.getMessage());
        Mockito.verify(taskService).deleteTask(taskId);
    }

    // 测试验证失败的场景（示例）
    @Test
    void createTask_ValidationFailed() throws Exception {
        CreateTaskRequest invalidRequest = new CreateTaskRequest(
                "", // 空的category
                "", // 空的name
                true, // status应该为false
                "desc",
                123L
        );

        mockMvc.perform(post("/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }
}
