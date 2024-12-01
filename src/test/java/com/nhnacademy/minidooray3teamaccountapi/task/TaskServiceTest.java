package com.nhnacademy.minidooray3teamaccountapi.task;

import com.nhnacademy.minidooray3teamaccountapi.dto.TagResponseDTO;
import com.nhnacademy.minidooray3teamaccountapi.dto.TaskRequest;
import com.nhnacademy.minidooray3teamaccountapi.dto.TaskResponse;
import com.nhnacademy.minidooray3teamaccountapi.entity.*;
import com.nhnacademy.minidooray3teamaccountapi.exception.ResourceNotFoundException;
import com.nhnacademy.minidooray3teamaccountapi.repository.*;
import com.nhnacademy.minidooray3teamaccountapi.service.TaskService;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    @InjectMocks
    private TaskService taskService;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ProjectMemberRepository projectMemberRepository;

    @Mock
    private MilestoneRepository milestoneRepository;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private TaskTagRepository taskTagRepository;

    private Project project;
    private ProjectMember projectMember;
    private MileStone milestone;
    private Task task;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        project = new Project();
        project.setProjectId(1L);

        User user = new User();
        user.setUserId("test-user");

        projectMember = new ProjectMember();
        projectMember.setProjectMemberId(1L);
        projectMember.setUser(user);
        projectMember.setRole(ProjectMember.Role.MEMBER);

        milestone = new MileStone();
        milestone.setMilestoneId(1L);
        milestone.setStatus(MileStone.Status.START);

        task = new Task();
        task.setTaskId(1L);
        task.setProject(project);
        task.setProjectMember(projectMember);
        task.setMilestone(milestone);
    }

    @Test
    void createTask_Success() {
        TaskRequest request = new TaskRequest();
        request.setProjectMemberId(1L);
        request.setTitle("Test Task");
        request.setDescription("Description");
        request.setMilestoneId(1L);

        task.setTitle("Test Task");

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(projectMemberRepository.findById(1L)).thenReturn(Optional.of(projectMember));
        when(milestoneRepository.findById(1L)).thenReturn(Optional.of(milestone));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        TaskResponse response = taskService.createTask(1L, request);

        assertThat(response).isNotNull();
        assertThat(response.getTitle()).isEqualTo("Test Task");
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void createTask_WithoutMilestone() {
        TaskRequest request = new TaskRequest();
        request.setProjectMemberId(1L);
        request.setTitle("Test Task");

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(projectMemberRepository.findById(1L)).thenReturn(Optional.of(projectMember));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        TaskResponse response = taskService.createTask(1L, request);

        assertThat(response).isNotNull();
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void createTask_ProjectNotFound() {
        TaskRequest request = new TaskRequest();
        request.setProjectMemberId(1L);

        when(projectRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> taskService.createTask(1L, request));
    }

    @Test
    void getTaskById_Success() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        TaskResponse response = taskService.getTaskById(1L, 1L);

        assertThat(response).isNotNull();
        assertThat(response.getTaskId()).isEqualTo(1L);
    }

    @Test
    void getTaskById_NotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> taskService.getTaskById(1L, 1L));
    }

    @Test
    void getTaskById_ProjectMismatch() {
        task.getProject().setProjectId(2L);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        assertThrows(ResourceNotFoundException.class, () -> taskService.getTaskById(1L, 1L));
    }

    @Test
    void updateTask_Success() {
        TaskRequest request = new TaskRequest();
        request.setTitle("Updated Title");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        TaskResponse response = taskService.updateTask(1L, 1L, request);

        assertThat(response).isNotNull();
        assertThat(response.getTitle()).isEqualTo("Updated Title");
    }

    @Test
    void updateTask_TaskNotFound() {
        TaskRequest request = new TaskRequest();

        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> taskService.updateTask(1L, 1L, request));
    }

    @Test
    void updateTask_MilestoneNotFound() {
        TaskRequest request = new TaskRequest();
        request.setMilestoneId(99L);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(milestoneRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> taskService.updateTask(1L, 1L, request));
    }

    @Test
    void deleteTask_Success() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        taskService.deleteTask(1L, 1L);

        verify(taskRepository, times(1)).delete(task);
    }

    @Test
    void deleteTask_TaskNotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> taskService.deleteTask(1L, 1L));
    }

    @Test
    void deleteTask_ProjectMismatch() {
        task.getProject().setProjectId(2L);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        assertThrows(ResourceNotFoundException.class, () -> taskService.deleteTask(1L, 1L));
    }



    @Test
    void getTasksByMemberId_Success() {
        Task task1 = new Task();
        task1.setTaskId(1L);
        task1.setProjectMember(projectMember);

        Task task2 = new Task();
        task2.setTaskId(2L);
        task2.setProjectMember(projectMember);

        when(taskRepository.findByProject_ProjectId(1L)).thenReturn(List.of(task1, task2));

        var tasks = taskService.getTasksByMemberId(1L, 1L);

        assertThat(tasks).hasSize(2);
        verify(taskRepository, times(1)).findByProject_ProjectId(1L);
    }

    @Test
    void addTagToTask_Success() {
        // given
        Tag tag = new Tag(1L, "Priority");
        TaskTag taskTag = new TaskTag(tag, task);

        when(taskRepository.findByProject_ProjectIdAndTaskId(1L, 1L)).thenReturn(Optional.of(task));
        when(tagRepository.findById(1L)).thenReturn(Optional.of(tag));
        when(taskTagRepository.existsByTaskAndTag(task, tag)).thenReturn(false);

        // when
        TagResponseDTO response = taskService.addTagToTask(1L, 1L, 1L, "test-user");

        // then
        assertThat(response).isNotNull();
        assertThat(response.getTagId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("Priority");
        verify(taskTagRepository, times(1)).save(any(TaskTag.class));
    }

    @Test
    void addTagToTask_TagAlreadyAssigned() {
        // given
        Tag tag = new Tag(1L, "Priority");

        when(taskRepository.findByProject_ProjectIdAndTaskId(1L, 1L)).thenReturn(Optional.of(task));
        when(tagRepository.findById(1L)).thenReturn(Optional.of(tag));
        when(taskTagRepository.existsByTaskAndTag(task, tag)).thenReturn(true);

        // when & then
        assertThrows(IllegalArgumentException.class,
                () -> taskService.addTagToTask(1L, 1L, 1L, "test-user"));
        verify(taskTagRepository, never()).save(any(TaskTag.class));
    }

    @Test
    void addTagToTask_TagNotFound() {
        // given
        when(taskRepository.findByProject_ProjectIdAndTaskId(1L, 1L)).thenReturn(Optional.of(task));
        when(tagRepository.findById(1L)).thenReturn(Optional.empty());

        // when & then
        assertThrows(ResourceNotFoundException.class,
                () -> taskService.addTagToTask(1L, 1L, 1L, "test-user"));
    }
    @Test
    void toResponse_WithComments() {
        // given
        Comment comment = new Comment();
        comment.setCommentId(1L);
        comment.setContent("Test Comment");
        comment.setCreatedAt(LocalDateTime.now());
        comment.setProjectMember(projectMember); // 댓글 작성자 설정
        task.setComments(List.of(comment)); // 댓글 추가

        // when
        TaskResponse response = invokeToResponse(task);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getComments()).hasSize(1);
        assertThat(response.getComments().get(0).getContent()).isEqualTo("Test Comment");
        assertThat(response.getComments().get(0).getProjectMember().getUserId()).isEqualTo("test-user");
    }

    // `toResponse` 메서드에 접근하기 위한 헬퍼 메서드
    private TaskResponse invokeToResponse(Task task) {
        // 리플렉션이나 간접 호출을 위한 헬퍼 메서드 사용
        try {
            Method toResponseMethod = TaskService.class.getDeclaredMethod("toResponse", Task.class);
            toResponseMethod.setAccessible(true);
            return (TaskResponse) toResponseMethod.invoke(taskService, task);
        } catch (Exception e) {
            throw new RuntimeException("Error invoking toResponse", e);
        }
    }

}
