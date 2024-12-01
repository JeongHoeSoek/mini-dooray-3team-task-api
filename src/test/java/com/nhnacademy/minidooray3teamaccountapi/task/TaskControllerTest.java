package com.nhnacademy.minidooray3teamaccountapi.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.minidooray3teamaccountapi.controller.TaskController;
import com.nhnacademy.minidooray3teamaccountapi.dto.TagResponseDTO;
import com.nhnacademy.minidooray3teamaccountapi.dto.TaskRequest;
import com.nhnacademy.minidooray3teamaccountapi.dto.TaskResponse;
import com.nhnacademy.minidooray3teamaccountapi.exception.GlobalExceptionHandler;
import com.nhnacademy.minidooray3teamaccountapi.exception.ResourceNotFoundException;
import com.nhnacademy.minidooray3teamaccountapi.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringJUnitConfig
class TaskControllerTest {

    private MockMvc mockMvc;

    private TaskService taskService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        taskService = Mockito.mock(TaskService.class);
        TaskController taskController = new TaskController(taskService);
        mockMvc = MockMvcBuilders.standaloneSetup(taskController)
                .setControllerAdvice(new GlobalExceptionHandler()) // 예외 처리기 설정
                .build();
        objectMapper = new ObjectMapper(); // ObjectMapper 직접 생성
    }

    @Test
    void createTask_Success() throws Exception {
        TaskRequest request = new TaskRequest();
        request.setTitle("Test Task");

        TaskResponse response = new TaskResponse();
        response.setTaskId(1L);
        response.setTitle("Test Task");

        when(taskService.createTask(anyLong(), any(TaskRequest.class))).thenReturn(response);

        mockMvc.perform(post("/projects/1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.taskId").value(1L))
                .andExpect(jsonPath("$.title").value("Test Task"));
    }

    @Test
    void getTaskById_Success() throws Exception {
        TaskResponse response = new TaskResponse();
        response.setTaskId(1L);
        response.setTitle("Test Task");

        when(taskService.getTaskById(1L, 1L)).thenReturn(response);

        mockMvc.perform(get("/projects/1/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.taskId").value(1L))
                .andExpect(jsonPath("$.title").value("Test Task"));
    }

    @Test
    void getTaskById_NotFound() throws Exception {
        // Mock 설정: 서비스에서 예외 발생
        when(taskService.getTaskById(1L, 1L)).thenThrow(new ResourceNotFoundException("Task not found"));

        // MockMvc 호출 및 검증
        mockMvc.perform(get("/projects/1/tasks/1"))
                .andExpect(status().isNotFound()) // HTTP 404 상태 코드 확인
                .andExpect(content().string("Task not found")); // 반환 메시지 확인
    }




    @Test
    void updateTask_Success() throws Exception {
        TaskRequest request = new TaskRequest();
        request.setTitle("Updated Task");

        TaskResponse response = new TaskResponse();
        response.setTaskId(1L);
        response.setTitle("Updated Task");

        when(taskService.updateTask(eq(1L), eq(1L), any(TaskRequest.class))).thenReturn(response);

        mockMvc.perform(post("/projects/1/tasks/1/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.taskId").value(1L))
                .andExpect(jsonPath("$.title").value("Updated Task"));
    }

    @Test
    void deleteTask_Success() throws Exception {
        doNothing().when(taskService).deleteTask(1L, 1L);

        mockMvc.perform(post("/projects/1/tasks/1/delete"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteTask_NotFound() throws Exception {
        // Mock 설정: 서비스에서 예외 발생
        doThrow(new ResourceNotFoundException("Task not found"))
                .when(taskService).deleteTask(1L, 1L);

        // MockMvc 호출 및 검증
        mockMvc.perform(post("/projects/1/tasks/1/delete"))
                .andExpect(status().isNotFound()) // HTTP 404 상태 코드 확인
                .andExpect(content().string("Task not found")); // 반환 메시지 확인
    }


    @Test
    void getTasksByMemberId_Success() throws Exception {
        TaskResponse task1 = new TaskResponse();
        task1.setTaskId(1L);
        task1.setTitle("Task 1");

        TaskResponse task2 = new TaskResponse();
        task2.setTaskId(2L);
        task2.setTitle("Task 2");

        when(taskService.getTasksByMemberId(1L, 1L)).thenReturn(List.of(task1, task2));

        mockMvc.perform(get("/projects/1/members/1/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].taskId").value(1L))
                .andExpect(jsonPath("$[1].taskId").value(2L));
    }

    @Test
    void addTagToTask_Success() throws Exception {
        TagResponseDTO tagResponse = new TagResponseDTO();
        tagResponse.setTagId(1L);
        tagResponse.setName("Priority");

        when(taskService.addTagToTask(1L, 1L, 1L, "test-user")).thenReturn(tagResponse);

        mockMvc.perform(post("/projects/1/tasks/1/tags/1")
                        .header("X-User-Id", "test-user"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.tagId").value(1L))
                .andExpect(jsonPath("$.name").value("Priority"));
    }

    @Test
    void addTagToTask_BadRequest() throws Exception {
        when(taskService.addTagToTask(1L, 1L, 1L, "test-user"))
                .thenThrow(new IllegalArgumentException("Invalid tag"));

        mockMvc.perform(post("/projects/1/tasks/1/tags/1")
                        .header("X-User-Id", "test-user"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addTagToTask_NotFound() throws Exception {
        when(taskService.addTagToTask(1L, 1L, 1L, "test-user"))
                .thenThrow(new ResourceNotFoundException("Tag not found"));

        mockMvc.perform(post("/projects/1/tasks/1/tags/1")
                        .header("X-User-Id", "test-user"))
                .andExpect(status().isNotFound());
    }

    @Test
    void removeTagFromTask_Success() throws Exception {
        doNothing().when(taskService).removeTagFromTask(1L, 1L, 1L, "test-user");

        mockMvc.perform(post("/projects/1/tasks/1/tags/1/remove")
                        .header("X-User-Id", "test-user"))
                .andExpect(status().isNoContent());
    }

    @Test
    void removeTagFromTask_NotFound() throws Exception {
        doThrow(new ResourceNotFoundException("Tag not found"))
                .when(taskService).removeTagFromTask(1L, 1L, 1L, "test-user");

        mockMvc.perform(post("/projects/1/tasks/1/tags/1/remove")
                        .header("X-User-Id", "test-user"))
                .andExpect(status().isNotFound());
    }
}
