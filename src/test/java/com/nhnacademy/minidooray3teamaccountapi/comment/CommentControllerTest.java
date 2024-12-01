package com.nhnacademy.minidooray3teamaccountapi.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.minidooray3teamaccountapi.controller.CommentController;
import com.nhnacademy.minidooray3teamaccountapi.dto.CommentRequestDTO;
import com.nhnacademy.minidooray3teamaccountapi.dto.CommentResponseDTO;
import com.nhnacademy.minidooray3teamaccountapi.dto.ProjectMemberDTO;
import com.nhnacademy.minidooray3teamaccountapi.exception.GlobalExceptionHandler;
import com.nhnacademy.minidooray3teamaccountapi.exception.ResourceNotFoundException;
import com.nhnacademy.minidooray3teamaccountapi.service.CommentService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import org.junit.jupiter.api.extension.ExtendWith;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CommentControllerTest {

    @Mock
    private CommentService commentService;

    @InjectMocks
    private CommentController commentController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    private CommentResponseDTO commentResponseDTO;
    private CommentResponseDTO updatedCommentResponseDTO;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(commentController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        ProjectMemberDTO projectMemberDTO = new ProjectMemberDTO(2L, "user2", "MEMBER");

        commentResponseDTO = new CommentResponseDTO(
                1L,
                1L,
                projectMemberDTO,
                "좋은 진행입니다!",
                LocalDateTime.of(2024, 11, 28, 0, 30, 0),
                LocalDateTime.of(2024, 11, 28, 0, 30, 0)
        );

        updatedCommentResponseDTO = new CommentResponseDTO(
                1L,
                1L,
                projectMemberDTO,
                "수정된 코멘트 내용",
                LocalDateTime.of(2024, 11, 28, 0, 30, 0),
                LocalDateTime.of(2024, 11, 28, 2, 0, 0)
        );
    }

    @Test
    void createComment_Success() throws Exception {
        CommentRequestDTO requestDTO = new CommentRequestDTO();
        requestDTO.setContent("좋은 진행입니다!");

        when(commentService.createComment(anyLong(), anyLong(), any(CommentRequestDTO.class), anyString()))
                .thenReturn(commentResponseDTO);

        mockMvc.perform(post("/projects/1/tasks/1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-User-Id", "user2")
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.commentId").value(1L))
                .andExpect(jsonPath("$.taskId").value(1L))
                .andExpect(jsonPath("$.projectMember.userId").value("user2"))
                .andExpect(jsonPath("$.projectMember.role").value("MEMBER"))
                .andExpect(jsonPath("$.content").value("좋은 진행입니다!"))
                .andExpect(jsonPath("$.createdAt").value("2024-11-28T00:30:00"))
                .andExpect(jsonPath("$.updatedAt").value("2024-11-28T00:30:00"));

        verify(commentService, times(1)).createComment(eq(1L), eq(1L), any(CommentRequestDTO.class), eq("user2"));
    }

    @Test
    void createComment_NotFound() throws Exception {
        CommentRequestDTO requestDTO = new CommentRequestDTO();
        requestDTO.setContent("좋은 진행입니다!");

        when(commentService.createComment(anyLong(), anyLong(), any(CommentRequestDTO.class), anyString()))
                .thenThrow(new ResourceNotFoundException("Task not found in the project"));

        mockMvc.perform(post("/projects/1/tasks/1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-User-Id", "user2")
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Task not found in the project"));

        verify(commentService, times(1)).createComment(eq(1L), eq(1L), any(CommentRequestDTO.class), eq("user2"));
    }

    @Test
    void getCommentById_Success() throws Exception {
        when(commentService.getCommentById(1L, 1L, 1L)).thenReturn(commentResponseDTO);

        mockMvc.perform(get("/projects/1/tasks/1/comments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.commentId").value(1L))
                .andExpect(jsonPath("$.taskId").value(1L))
                .andExpect(jsonPath("$.projectMember.userId").value("user2"))
                .andExpect(jsonPath("$.projectMember.role").value("MEMBER"))
                .andExpect(jsonPath("$.content").value("좋은 진행입니다!"))
                .andExpect(jsonPath("$.createdAt").value("2024-11-28T00:30:00"))
                .andExpect(jsonPath("$.updatedAt").value("2024-11-28T00:30:00"));

        verify(commentService, times(1)).getCommentById(1L, 1L, 1L);
    }

    @Test
    void getCommentById_NotFound() throws Exception {
        when(commentService.getCommentById(1L, 1L, 1L))
                .thenThrow(new ResourceNotFoundException("Comment not found"));

        mockMvc.perform(get("/projects/1/tasks/1/comments/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Comment not found"));

        verify(commentService, times(1)).getCommentById(1L, 1L, 1L);
    }

    @Test
    void getAllCommentsByTask_Success() throws Exception {
        ProjectMemberDTO projectMemberDTO = new ProjectMemberDTO(2L, "user2", "MEMBER");
        ProjectMemberDTO projectMemberDTO2 = new ProjectMemberDTO(3L, "user3", "MEMBER");

        CommentResponseDTO comment1 = new CommentResponseDTO(
                1L,
                1L,
                projectMemberDTO,
                "좋은 진행입니다!",
                LocalDateTime.of(2024, 11, 28, 0, 30, 0),
                LocalDateTime.of(2024, 11, 28, 0, 30, 0)
        );

        CommentResponseDTO comment2 = new CommentResponseDTO(
                2L,
                1L,
                projectMemberDTO2,
                "추가 작업이 필요합니다!",
                LocalDateTime.of(2024, 11, 28, 1, 0, 0),
                LocalDateTime.of(2024, 11, 28, 1, 0, 0)
        );

        List<CommentResponseDTO> comments = Arrays.asList(comment1, comment2);

        when(commentService.getAllCommentsByTask(1L, 1L)).thenReturn(comments);

        mockMvc.perform(get("/projects/1/tasks/1/comments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].commentId").value(1L))
                .andExpect(jsonPath("$[0].taskId").value(1L))
                .andExpect(jsonPath("$[0].projectMember.userId").value("user2"))
                .andExpect(jsonPath("$[0].projectMember.role").value("MEMBER"))
                .andExpect(jsonPath("$[0].content").value("좋은 진행입니다!"))
                .andExpect(jsonPath("$.comments[0].createdAt").value("2024-11-28T00:30:00"))
                .andExpect(jsonPath("$[1].commentId").value(2L))
                .andExpect(jsonPath("$[1].taskId").value(1L))
                .andExpect(jsonPath("$[1].projectMember.userId").value("user3"))
                .andExpect(jsonPath("$[1].projectMember.role").value("MEMBER"))
                .andExpect(jsonPath("$[1].content").value("추가 작업이 필요합니다!"))
                .andExpect(jsonPath("$[1].createdAt").value("2024-11-28T01:00:00"))
                .andExpect(jsonPath("$[1].updatedAt").value("2024-11-28T01:00:00"));

        verify(commentService, times(1)).getAllCommentsByTask(1L, 1L);
    }

    @Test
    void updateComment_Success() throws Exception {
        CommentRequestDTO requestDTO = new CommentRequestDTO();
        requestDTO.setContent("수정된 코멘트 내용");

        when(commentService.updateComment(eq(1L), eq(1L), eq(1L), any(CommentRequestDTO.class), eq("user2")))
                .thenReturn(updatedCommentResponseDTO);

        mockMvc.perform(put("/projects/1/tasks/1/comments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-User-Id", "user2")
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.commentId").value(1L))
                .andExpect(jsonPath("$.taskId").value(1L))
                .andExpect(jsonPath("$.projectMember.userId").value("user2"))
                .andExpect(jsonPath("$.projectMember.role").value("MEMBER"))
                .andExpect(jsonPath("$.content").value("수정된 코멘트 내용"))
                .andExpect(jsonPath("$.createdAt").value("2024-11-28T00:30:00"))
                .andExpect(jsonPath("$.updatedAt").value("2024-11-28T02:00:00"));

        verify(commentService, times(1)).updateComment(eq(1L), eq(1L), eq(1L), any(CommentRequestDTO.class), eq("user2"));
    }

    @Test
    void updateComment_NotFound() throws Exception {
        CommentRequestDTO requestDTO = new CommentRequestDTO();
        requestDTO.setContent("수정된 코멘트 내용");

        when(commentService.updateComment(eq(1L), eq(1L), eq(1L), any(CommentRequestDTO.class), eq("user2")))
                .thenThrow(new ResourceNotFoundException("Comment not found"));

        mockMvc.perform(put("/projects/1/tasks/1/comments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-User-Id", "user2")
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Comment not found"));

        verify(commentService, times(1)).updateComment(eq(1L), eq(1L), eq(1L), any(CommentRequestDTO.class), eq("user2"));
    }

    @Test
    void updateComment_Forbidden() throws Exception {
        CommentRequestDTO requestDTO = new CommentRequestDTO();
        requestDTO.setContent("수정된 코멘트 내용");

        when(commentService.updateComment(eq(1L), eq(1L), eq(1L), any(CommentRequestDTO.class), eq("anotherUser")))
                .thenThrow(new IllegalArgumentException("You do not have permission to update this comment"));

        mockMvc.perform(put("/projects/1/tasks/1/comments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-User-Id", "anotherUser")
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("You do not have permission to update this comment"));

        verify(commentService, times(1)).updateComment(eq(1L), eq(1L), eq(1L), any(CommentRequestDTO.class), eq("anotherUser"));
    }

    @Test
    void deleteComment_Success() throws Exception {
        doNothing().when(commentService).deleteComment(1L, 1L, 1L, "user2");

        mockMvc.perform(delete("/projects/1/tasks/1/comments/1")
                        .header("X-User-Id", "user2"))
                .andExpect(status().isNoContent());

        verify(commentService, times(1)).deleteComment(1L, 1L, 1L, "user2");
    }

    @Test
    void deleteComment_NotFound() throws Exception {
        doThrow(new ResourceNotFoundException("Comment not found"))
                .when(commentService).deleteComment(1L, 1L, 1L, "user2");

        mockMvc.perform(delete("/projects/1/tasks/1/comments/1")
                        .header("X-User-Id", "user2"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Comment not found"));

        verify(commentService, times(1)).deleteComment(1L, 1L, 1L, "user2");
    }

    @Test
    void deleteComment_Forbidden() throws Exception {
        doThrow(new IllegalArgumentException("You do not have permission to delete this comment"))
                .when(commentService).deleteComment(1L, 1L, 1L, "anotherUser");

        mockMvc.perform(delete("/projects/1/tasks/1/comments/1")
                        .header("X-User-Id", "anotherUser"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("You do not have permission to delete this comment"));

        verify(commentService, times(1)).deleteComment(1L, 1L, 1L, "anotherUser");
    }
}
