
//package com.nhnacademy.minidooray3teamaccountapi.comment;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.nhnacademy.minidooray3teamaccountapi.controller.CommentController;
//import com.nhnacademy.minidooray3teamaccountapi.dto.CommentRequestDTO;
//import com.nhnacademy.minidooray3teamaccountapi.dto.CommentResponseDTO;
//import com.nhnacademy.minidooray3teamaccountapi.dto.ProjectMemberDTO;
//import com.nhnacademy.minidooray3teamaccountapi.exception.ResourceNotFoundException;
//import com.nhnacademy.minidooray3teamaccountapi.service.CommentService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import java.time.LocalDateTime;
//import java.util.Collections;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
////
////@SpringBootTest
////@AutoConfigureMockMvc
//
//class CommentControllerTest {
//
//    private MockMvc mockMvc;
//    private CommentService commentService;
//    private ObjectMapper objectMapper;
//
//    @BeforeEach
//    void setUp() {
//        // Mock CommentService 생성
//        commentService = Mockito.mock(CommentService.class);
//
//        // Controller 생성 및 MockMvc 설정
//        CommentController commentController = new CommentController(commentService);
//        mockMvc = MockMvcBuilders.standaloneSetup(commentController).build();
//
//        // ObjectMapper 초기화
//        objectMapper = new ObjectMapper();
//    }
//
//    @Test
//    void testCreateComment() throws Exception {
//        CommentRequestDTO requestDTO = new CommentRequestDTO();
//        requestDTO.setContent("Test comment");
//
//        CommentResponseDTO responseDTO = new CommentResponseDTO(
//                1L,
//                1L,
//                new ProjectMemberDTO(1L, "user1", "MEMBER"),
//                "Test comment",
//                LocalDateTime.now(),
//                LocalDateTime.now()
//        );
//
//        Mockito.when(commentService.createComment(anyLong(), anyLong(), any(CommentRequestDTO.class), eq("user1")))
//                .thenReturn(responseDTO);
//
//        mockMvc.perform(post("/projects/1/tasks/1/comments")
//                        .header("X-User-Id", "user1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(requestDTO)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.commentId").value(1L))
//                .andExpect(jsonPath("$.content").value("Test comment"));
//    }
//
//    @Test
//    void testGetCommentById() throws Exception {
//        CommentResponseDTO responseDTO = new CommentResponseDTO(
//                1L,
//                1L,
//                new ProjectMemberDTO(1L, "user1", "MEMBER"),
//                "Test comment",
//                LocalDateTime.now(),
//                LocalDateTime.now()
//        );
//
//        Mockito.when(commentService.getCommentById(anyLong(), anyLong(), eq(1L)))
//                .thenReturn(responseDTO);
//
//        mockMvc.perform(get("/projects/1/tasks/1/comments/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.commentId").value(1L))
//                .andExpect(jsonPath("$.content").value("Test comment"));
//    }
//
//    @Test
//    void testGetAllCommentsByTask() throws Exception {
//        CommentResponseDTO responseDTO = new CommentResponseDTO(
//                1L,
//                1L,
//                new ProjectMemberDTO(1L, "user1", "MEMBER"),
//                "Test comment",
//                LocalDateTime.now(),
//                LocalDateTime.now()
//        );
//
//        Mockito.when(commentService.getAllCommentsByTask(anyLong(), anyLong()))
//                .thenReturn(Collections.singletonList(responseDTO));
//
//        mockMvc.perform(get("/projects/1/tasks/1/comments"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].commentId").value(1L))
//                .andExpect(jsonPath("$[0].content").value("Test comment"));
//    }
//
//    @Test
//    void testUpdateComment() throws Exception {
//        CommentRequestDTO requestDTO = new CommentRequestDTO();
//        requestDTO.setContent("Updated comment");
//
//        CommentResponseDTO responseDTO = new CommentResponseDTO(
//                1L,
//                1L,
//                new ProjectMemberDTO(1L, "user1", "MEMBER"),
//                "Updated comment",
//                LocalDateTime.now(),
//                LocalDateTime.now()
//        );
//
//        Mockito.when(commentService.updateComment(anyLong(), anyLong(), eq(1L), any(CommentRequestDTO.class), eq("user1")))
//                .thenReturn(responseDTO);
//
//        mockMvc.perform(post("/projects/1/tasks/1/comments/1/update")
//                        .header("X-User-Id", "user1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(requestDTO)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.content").value("Updated comment"));
//    }
//
//    @Test
//    void testDeleteComment() throws Exception {
//        Mockito.doNothing().when(commentService).deleteComment(anyLong(), anyLong(), eq(1L), eq("user1"));
//
//        mockMvc.perform(post("/projects/1/tasks/1/comments/1/delete")
//                        .header("X-User-Id", "user1"))
//                .andExpect(status().isNoContent());
//    }
//
//    @Test
//    void testCreateCommentNotFound() throws Exception {
//        CommentRequestDTO requestDTO = new CommentRequestDTO();
//        requestDTO.setContent("Test comment");
//
//        // ResourceNotFoundException 예외 발생
//        Mockito.when(commentService.createComment(anyLong(), anyLong(), any(CommentRequestDTO.class), eq("user1")))
//                .thenThrow(new ResourceNotFoundException("Resource not found"));
//
//        mockMvc.perform(post("/projects/1/tasks/1/comments")
//                        .header("X-User-Id", "user1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(requestDTO)))
//                .andExpect(status().isNotFound());
//    }
//
//    @Test
//    void testUpdateCommentForbidden() throws Exception {
//        CommentRequestDTO requestDTO = new CommentRequestDTO();
//        requestDTO.setContent("Updated comment");
//
//        // IllegalArgumentException 예외 발생 (Forbidden)
//        Mockito.when(commentService.updateComment(anyLong(), anyLong(), eq(1L), any(CommentRequestDTO.class), eq("user1")))
//                .thenThrow(new IllegalArgumentException("Forbidden"));
//
//        mockMvc.perform(post("/projects/1/tasks/1/comments/1/update")
//                        .header("X-User-Id", "user1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(requestDTO)))
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    void testDeleteCommentForbidden() throws Exception {
//        // IllegalArgumentException 예외 발생 (Forbidden)
//        Mockito.doThrow(new IllegalArgumentException("Forbidden"))
//                .when(commentService).deleteComment(anyLong(), anyLong(), eq(1L), eq("user1"));
//
//        mockMvc.perform(post("/projects/1/tasks/1/comments/1/delete")
//                        .header("X-User-Id", "user1"))
//                .andExpect(status().isForbidden());
//    }
//
//}



package com.nhnacademy.minidooray3teamaccountapi.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.minidooray3teamaccountapi.controller.CommentController;
import com.nhnacademy.minidooray3teamaccountapi.dto.CommentRequestDTO;
import com.nhnacademy.minidooray3teamaccountapi.dto.CommentResponseDTO;
import com.nhnacademy.minidooray3teamaccountapi.exception.ResourceNotFoundException;
import com.nhnacademy.minidooray3teamaccountapi.service.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CommentControllerTest {

    private MockMvc mockMvc;
    private CommentService commentService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        // Mock CommentService 생성
        commentService = Mockito.mock(CommentService.class);

        // Controller 생성 및 MockMvc 설정
        CommentController commentController = new CommentController(commentService);
        mockMvc = MockMvcBuilders.standaloneSetup(commentController).build();

        // ObjectMapper 초기화
        objectMapper = new ObjectMapper();
    }

    @Test
    void testCreateComment() throws Exception {
        // Given
        CommentRequestDTO requestDTO = new CommentRequestDTO();
        requestDTO.setContent("Test comment");

        CommentResponseDTO responseDTO = new CommentResponseDTO(
                1L, 1L, null, "Test comment", LocalDateTime.now(), LocalDateTime.now());

        Mockito.when(commentService.createComment(anyLong(), anyLong(), any(CommentRequestDTO.class), eq("user1")))
                .thenReturn(responseDTO);

        // When & Then
        mockMvc.perform(post("/projects/1/tasks/1/comments")
                        .header("X-User-Id", "user1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.commentId").value(1L))
                .andExpect(jsonPath("$.content").value("Test comment"));
    }

    @Test
    void testCreateCommentNotFound() throws Exception {
        // Given
        CommentRequestDTO requestDTO = new CommentRequestDTO();
        requestDTO.setContent("Test comment");

        Mockito.when(commentService.createComment(anyLong(), anyLong(), any(CommentRequestDTO.class), eq("user1")))
                .thenThrow(new ResourceNotFoundException("Not found"));

        // When & Then
        mockMvc.perform(post("/projects/1/tasks/1/comments")
                        .header("X-User-Id", "user1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateCommentBadRequest() throws Exception {
        // Given
        CommentRequestDTO requestDTO = new CommentRequestDTO();
        requestDTO.setContent(null); // Invalid content

        // When & Then
        mockMvc.perform(post("/projects/1/tasks/1/comments")
                        .header("X-User-Id", "user1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetCommentById() throws Exception {
        // Given
        CommentResponseDTO responseDTO = new CommentResponseDTO(
                1L, 1L, null, "Test comment", LocalDateTime.now(), LocalDateTime.now());

        Mockito.when(commentService.getCommentById(anyLong(), anyLong(), eq(1L)))
                .thenReturn(responseDTO);

        // When & Then
        mockMvc.perform(get("/projects/1/tasks/1/comments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.commentId").value(1L))
                .andExpect(jsonPath("$.content").value("Test comment"));
    }

    @Test
    void testGetCommentByIdNotFound() throws Exception {
        // Given
        Mockito.when(commentService.getCommentById(anyLong(), anyLong(), eq(1L)))
                .thenThrow(new ResourceNotFoundException("Comment not found"));

        // When & Then
        mockMvc.perform(get("/projects/1/tasks/1/comments/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAllCommentsByTask() throws Exception {
        // Given
        CommentResponseDTO responseDTO = new CommentResponseDTO(
                1L, 1L, null, "Test comment", LocalDateTime.now(), LocalDateTime.now());
        Mockito.when(commentService.getAllCommentsByTask(anyLong(), anyLong()))
                .thenReturn(Collections.singletonList(responseDTO));

        // When & Then
        mockMvc.perform(get("/projects/1/tasks/1/comments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].commentId").value(1L))
                .andExpect(jsonPath("$[0].content").value("Test comment"));
    }

    @Test
    void testUpdateComment() throws Exception {
        // Given
        CommentRequestDTO requestDTO = new CommentRequestDTO();
        requestDTO.setContent("Updated comment");

        CommentResponseDTO responseDTO = new CommentResponseDTO(
                1L, 1L, null, "Updated comment", LocalDateTime.now(), LocalDateTime.now());

        Mockito.when(commentService.updateComment(anyLong(), anyLong(), eq(1L), any(CommentRequestDTO.class), eq("user1")))
                .thenReturn(responseDTO);

        // When & Then
        mockMvc.perform(post("/projects/1/tasks/1/comments/1/update")
                        .header("X-User-Id", "user1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Updated comment"));
    }

    @Test
    void testUpdateCommentNotFound() throws Exception {
        // Given
        CommentRequestDTO requestDTO = new CommentRequestDTO();
        requestDTO.setContent("Updated comment");

        Mockito.when(commentService.updateComment(anyLong(), anyLong(), eq(1L), any(CommentRequestDTO.class), eq("user1")))
                .thenThrow(new ResourceNotFoundException("Not found"));

        // When & Then
        mockMvc.perform(post("/projects/1/tasks/1/comments/1/update")
                        .header("X-User-Id", "user1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateCommentForbidden() throws Exception {
        // Given
        CommentRequestDTO requestDTO = new CommentRequestDTO();
        requestDTO.setContent("Updated comment");

        Mockito.when(commentService.updateComment(anyLong(), anyLong(), eq(1L), any(CommentRequestDTO.class), eq("user1")))
                .thenThrow(new IllegalArgumentException("Forbidden"));

        // When & Then
        mockMvc.perform(post("/projects/1/tasks/1/comments/1/update")
                        .header("X-User-Id", "user1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isForbidden());
    }

    @Test
    void testDeleteComment() throws Exception {
        // Given
        Mockito.doNothing().when(commentService).deleteComment(anyLong(), anyLong(), eq(1L), eq("user1"));

        // When & Then
        mockMvc.perform(post("/projects/1/tasks/1/comments/1/delete")
                        .header("X-User-Id", "user1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteCommentNotFound() throws Exception {
        // Given
        Mockito.doThrow(new ResourceNotFoundException("Not found")).when(commentService)
                .deleteComment(anyLong(), anyLong(), eq(1L), eq("user1"));

        // When & Then
        mockMvc.perform(post("/projects/1/tasks/1/comments/1/delete")
                        .header("X-User-Id", "user1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteCommentForbidden() throws Exception {
        // Given
        Mockito.doThrow(new IllegalArgumentException("Forbidden")).when(commentService)
                .deleteComment(anyLong(), anyLong(), eq(1L), eq("user1"));

        // When & Then
        mockMvc.perform(post("/projects/1/tasks/1/comments/1/delete")
                        .header("X-User-Id", "user1"))
                .andExpect(status().isForbidden());
    }
}
