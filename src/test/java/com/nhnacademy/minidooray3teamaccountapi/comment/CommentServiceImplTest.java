package com.nhnacademy.minidooray3teamaccountapi.comment;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.nhnacademy.minidooray3teamaccountapi.dto.CommentRequestDTO;
import com.nhnacademy.minidooray3teamaccountapi.dto.CommentResponseDTO;
import com.nhnacademy.minidooray3teamaccountapi.entity.Comment;
import com.nhnacademy.minidooray3teamaccountapi.entity.Project;
import com.nhnacademy.minidooray3teamaccountapi.entity.ProjectMember;
import com.nhnacademy.minidooray3teamaccountapi.entity.Task;
import com.nhnacademy.minidooray3teamaccountapi.entity.User;
import com.nhnacademy.minidooray3teamaccountapi.exception.ResourceNotFoundException;
import com.nhnacademy.minidooray3teamaccountapi.repository.CommentRepository;
import com.nhnacademy.minidooray3teamaccountapi.repository.ProjectMemberRepository;
import com.nhnacademy.minidooray3teamaccountapi.repository.TaskRepository;

import com.nhnacademy.minidooray3teamaccountapi.service.CommentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ProjectMemberRepository projectMemberRepository;

    @InjectMocks
    private CommentServiceImpl commentService;

    private User user;
    private Project project;
    private ProjectMember projectMember;
    private Task task;
    private Comment comment;

    @BeforeEach
    void setUp() {
        // User 설정 (간소화)
        user = new User();
        user.setUserId("user2");
        user.setUsername("User Two");

        // Project 설정 (간소화)
        project = new Project();
        project.setProjectId(1L);
        project.setName("Project Alpha");

        // ProjectMember 설정 (간소화)
        projectMember = new ProjectMember();
        projectMember.setProjectMemberId(2L);
        projectMember.setUser(user);
        projectMember.setProject(project);
        projectMember.setRole(ProjectMember.Role.MEMBER); // Role 설정 추가

        // Task 설정 (간소화)
        task = new Task();
        task.setTaskId(1L);
        task.setTitle("Sample Task");
        task.setProject(project);
        task.setProjectMember(projectMember);

        // Comment 설정
        comment = new Comment();
        comment.setCommentId(1L);
        comment.setTask(task);
        comment.setProjectMember(projectMember);
        comment.setContent("좋은 진행입니다!");
    }



    @Test
    void createComment_Success() {
        CommentRequestDTO requestDTO = new CommentRequestDTO();
        requestDTO.setContent("좋은 진행입니다!");

        when(taskRepository.findByProject_ProjectIdAndTaskId(1L, 1L)).thenReturn(Optional.of(task));
        when(projectMemberRepository.findByUser_UserIdAndProject_ProjectId("user2", 1L)).thenReturn(Optional.of(projectMember));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        CommentResponseDTO responseDTO = commentService.createComment(1L, 1L, requestDTO, "user2");

        assertNotNull(responseDTO);
        assertEquals("좋은 진행입니다!", responseDTO.getContent());
        assertEquals("user2", responseDTO.getProjectMember().getUserId());
        assertEquals("MEMBER", responseDTO.getProjectMember().getRole());

        verify(taskRepository, times(1)).findByProject_ProjectIdAndTaskId(1L, 1L);
        verify(projectMemberRepository, times(1)).findByUser_UserIdAndProject_ProjectId("user2", 1L);
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void createComment_TaskNotFound() {
        CommentRequestDTO requestDTO = new CommentRequestDTO();
        requestDTO.setContent("좋은 진행입니다!");

        when(taskRepository.findByProject_ProjectIdAndTaskId(1L, 1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            commentService.createComment(1L, 1L, requestDTO, "user2");
        });

        assertEquals("Task not found in the project", exception.getMessage());

        verify(taskRepository, times(1)).findByProject_ProjectIdAndTaskId(1L, 1L);
        verify(projectMemberRepository, times(0)).findByUser_UserIdAndProject_ProjectId(anyString(), anyLong());
        verify(commentRepository, times(0)).save(any(Comment.class));
    }

    @Test
    void createComment_ProjectMemberNotFound() {
        CommentRequestDTO requestDTO = new CommentRequestDTO();
        requestDTO.setContent("좋은 진행입니다!");

        when(taskRepository.findByProject_ProjectIdAndTaskId(1L, 1L)).thenReturn(Optional.of(task));
        when(projectMemberRepository.findByUser_UserIdAndProject_ProjectId("user2", 1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            commentService.createComment(1L, 1L, requestDTO, "user2");
        });

        assertEquals("Project member not found", exception.getMessage());

        verify(taskRepository, times(1)).findByProject_ProjectIdAndTaskId(1L, 1L);
        verify(projectMemberRepository, times(1)).findByUser_UserIdAndProject_ProjectId("user2", 1L);
        verify(commentRepository, times(0)).save(any(Comment.class));
    }

    @Test
    void getCommentById_Success() {
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        CommentResponseDTO responseDTO = commentService.getCommentById(1L, 1L, 1L);

        assertNotNull(responseDTO);
        assertEquals(1L, responseDTO.getCommentId());
        assertEquals("user2", responseDTO.getProjectMember().getUserId());
        assertEquals("MEMBER", responseDTO.getProjectMember().getRole());
        assertEquals("좋은 진행입니다!", responseDTO.getContent());

        verify(commentRepository, times(1)).findById(1L);
    }

    @Test
    void getCommentById_NotFound() {
        when(commentRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            commentService.getCommentById(1L, 1L, 1L);
        });

        assertEquals("Comment not found", exception.getMessage());

        verify(commentRepository, times(1)).findById(1L);
    }

    @Test
    void getAllCommentsByTask_Success() {
        Comment comment2 = new Comment();
        comment2.setCommentId(2L);
        comment2.setTask(task);
        comment2.setProjectMember(projectMember);
        comment2.setContent("추가 작업이 필요합니다!");
        comment2.setCreatedAt(LocalDateTime.of(2024, 11, 28, 1, 0));
        comment2.setUpdatedAt(LocalDateTime.of(2024, 11, 28, 1, 0));

        List<Comment> comments = Arrays.asList(comment, comment2);
        when(commentRepository.findAllByTask_TaskId(1L)).thenReturn(comments);

        List<CommentResponseDTO> responseDTOList = commentService.getAllCommentsByTask(1L, 1L);

        assertNotNull(responseDTOList);
        assertEquals(2, responseDTOList.size());
        assertEquals("좋은 진행입니다!", responseDTOList.get(0).getContent());
        assertEquals("추가 작업이 필요합니다!", responseDTOList.get(1).getContent());

        verify(commentRepository, times(1)).findAllByTask_TaskId(1L);
    }

    @Test
    void updateComment_Success() {
        CommentRequestDTO requestDTO = new CommentRequestDTO();
        requestDTO.setContent("수정된 코멘트 내용");

        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        CommentResponseDTO responseDTO = commentService.updateComment(1L, 1L, 1L, requestDTO, "user2");

        assertNotNull(responseDTO);
        assertEquals("수정된 코멘트 내용", responseDTO.getContent());

        verify(commentRepository, times(1)).findById(1L);
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void updateComment_NotFound() {
        CommentRequestDTO requestDTO = new CommentRequestDTO();
        requestDTO.setContent("수정된 코멘트 내용");

        when(commentRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            commentService.updateComment(1L, 1L, 1L, requestDTO, "user2");
        });

        assertEquals("Comment not found", exception.getMessage());

        verify(commentRepository, times(1)).findById(1L);
        verify(commentRepository, times(0)).save(any(Comment.class));
    }

    @Test
    void updateComment_Forbidden() {
        CommentRequestDTO requestDTO = new CommentRequestDTO();
        requestDTO.setContent("수정된 코멘트 내용");

        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            commentService.updateComment(1L, 1L, 1L, requestDTO, "anotherUser");
        });

        assertEquals("You do not have permission to update this comment", exception.getMessage());

        verify(commentRepository, times(1)).findById(1L);
        verify(commentRepository, times(0)).save(any(Comment.class));
    }

    @Test
    void deleteComment_Success() {
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        doNothing().when(commentRepository).delete(comment);

        assertDoesNotThrow(() -> {
            commentService.deleteComment(1L, 1L, 1L, "user2");
        });

        verify(commentRepository, times(1)).findById(1L);
        verify(commentRepository, times(1)).delete(comment);
    }

    @Test
    void deleteComment_NotFound() {
        when(commentRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            commentService.deleteComment(1L, 1L, 1L, "user2");
        });

        assertEquals("Comment not found", exception.getMessage());

        verify(commentRepository, times(1)).findById(1L);
        verify(commentRepository, times(0)).delete(any(Comment.class));
    }

    @Test
    void deleteComment_Forbidden() {
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        // userId가 일치하지 않도록 설정
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            commentService.deleteComment(1L, 1L, 1L, "anotherUser");
        });

        assertEquals("You do not have permission to delete this comment", exception.getMessage());

        verify(commentRepository, times(1)).findById(1L);
        verify(commentRepository, times(0)).delete(comment); // 삭제가 호출되지 않아야 함
    }

}
