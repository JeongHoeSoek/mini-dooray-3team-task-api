package com.nhnacademy.minidooray3teamaccountapi.project;

import com.nhnacademy.minidooray3teamaccountapi.dto.*;
import com.nhnacademy.minidooray3teamaccountapi.entity.*;
import com.nhnacademy.minidooray3teamaccountapi.exception.*;
import com.nhnacademy.minidooray3teamaccountapi.repository.ProjectMemberRepository;
import com.nhnacademy.minidooray3teamaccountapi.repository.ProjectRepository;
import com.nhnacademy.minidooray3teamaccountapi.repository.UserRepository;
import com.nhnacademy.minidooray3teamaccountapi.service.ProjectServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProjectServiceImplTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ProjectMemberRepository projectMemberRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ProjectServiceImpl projectService;

    private User testUser;
    private Project testProject;
    private ProjectMember testMember;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock 데이터 초기화
        testUser = new User("admin-user", "Admin User", "admin@example.com", LocalDateTime.now(), Collections.emptyList());
        testProject = new Project(1L, "Test Project", Project.Status.ACTIVE, LocalDateTime.now(), Collections.emptyList(), Collections.emptyList());
        testMember = new ProjectMember(1L, testProject, testUser, ProjectMember.Role.ADMIN, Collections.emptyList(), Collections.emptyList());

        // 사용자 Mock 설정
        when(userRepository.findById("admin-user")).thenReturn(Optional.of(testUser));
    }

    @Test
    void createProject_Success() {
        ProjectCreateRequestDTO requestDTO = new ProjectCreateRequestDTO("New Project", "ACTIVE");

        when(userRepository.findById("admin-user")).thenReturn(Optional.of(testUser));
        when(projectRepository.save(any(Project.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ProjectDTO result = projectService.createProject("admin-user", requestDTO);

        assertNotNull(result);
        assertEquals("New Project", result.getName());
        verify(projectRepository, times(1)).save(any(Project.class));
        verify(projectMemberRepository, times(1)).save(any(ProjectMember.class));
    }

    @Test
    void createProject_UserNotFound() {
        // Given
        ProjectCreateRequestDTO requestDTO = new ProjectCreateRequestDTO("New Project", "ACTIVE");

        // Mock 설정: 사용자가 존재하지 않는 경우
        when(userRepository.findById("non-existent-user")).thenReturn(Optional.empty());

        // When & Then: 예외가 발생하는지 확인
        assertThrows(UserNotFoundException.class,
                () -> projectService.createProject("non-existent-user", requestDTO));

        // Verify: userRepository.findById()가 호출되었는지 확인
        verify(userRepository, times(1)).findById("non-existent-user");
    }

    @Test
    void testCreateProjectDetailsDTO() {
        // Given
        User user = new User("user1", "User One", "user1@example.com", LocalDateTime.now(), Collections.emptyList());
        Project project = new Project(1L, "Test Project", Project.Status.ACTIVE, LocalDateTime.now(), Collections.emptyList(), Collections.emptyList());
        ProjectMember projectMember = new ProjectMember(1L, project, user, ProjectMember.Role.ADMIN, Collections.emptyList(), Collections.emptyList());
        project.setMembers(List.of(projectMember));

        Task task = new Task(1L, project, new MileStone(1L, "Milestone 1", MileStone.Status.START, LocalDateTime.now(), null), projectMember, "Task Title", "Task Description", LocalDateTime.now(), null, null);
        project.setTasks(List.of(task));

        Tag tag = new Tag(1L, "Tag1", null);
        TaskTag taskTag = new TaskTag(1L, task, tag);
        task.setTaskTags(List.of(taskTag));

        when(userRepository.findById("user1")).thenReturn(Optional.of(user));
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        // When
        ProjectDetailsDTO result = projectService.getProjectDetails("user1", 1L);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getProjectMembers().size());
        assertEquals(1, result.getTasks().size());
        assertEquals(1, result.getMilestones().size());
        assertEquals(1, result.getTags().size());
    }

    @Test
    void createProjectDetailsDTO_WithTasksAndTags() {
        // 태스크 및 태그 설정
        Task task1 = new Task(1L, testProject, null, testMember, "Task 1", "Description 1", LocalDateTime.now(), null, null);
        Task task2 = new Task(2L, testProject, null, testMember, "Task 2", "Description 2", LocalDateTime.now(), null, null);

        Tag tag1 = new Tag(1L, "Tag1", null);
        Tag tag2 = new Tag(2L, "Tag2", null);

        TaskTag taskTag1 = new TaskTag(1L, task1, tag1);
        TaskTag taskTag2 = new TaskTag(2L, task2, tag2);

        task1.setTaskTags(List.of(taskTag1));
        task2.setTaskTags(List.of(taskTag2));

        testProject.setTasks(List.of(task1, task2));
        testProject.setMembers(List.of(testMember));

        // Mock 설정 추가
        when(projectRepository.findById(1L)).thenReturn(Optional.of(testProject));

        // 테스트 실행
        ProjectDetailsDTO result = projectService.getProjectDetails("admin-user", 1L);

        // 검증
        assertEquals(2, result.getTasks().size());
        assertEquals(2, result.getTags().size());
    }


    @Test
    void getProjectDetails_UserNotFound() {
        when(userRepository.findById("admin-user")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> projectService.getProjectDetails("admin-user", 1L));
    }

    @Test
    void getProjectDetails_ProjectNotFound() {
        when(userRepository.findById("admin-user")).thenReturn(Optional.of(testUser));
        when(projectRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProjectNotFoundException.class, () -> projectService.getProjectDetails("admin-user", 1L));
    }

    @Test
    void getProjectDetails_UnauthorizedAccess() {
        testProject.setMembers(Collections.emptyList());
        when(userRepository.findById("admin-user")).thenReturn(Optional.of(testUser));
        when(projectRepository.findById(1L)).thenReturn(Optional.of(testProject));

        assertThrows(UnauthorizedAccessException.class, () -> projectService.getProjectDetails("admin-user", 1L));
    }

    @Test
    void getUserProjects_Success() {
        when(userRepository.findById("admin-user")).thenReturn(Optional.of(testUser));
        when(projectRepository.findByMembers_User_UserId("admin-user")).thenReturn(List.of(testProject));

        List<UserProjectDTO> result = projectService.getUserProjects("admin-user");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Project", result.get(0).getName());
    }

    @Test
    void getUserProjects_UserNotFound() {
        // Given
        String nonExistentUserId = "non-existent-user";

        // Mock 설정: 사용자가 존재하지 않는 경우
        when(userRepository.findById(nonExistentUserId)).thenReturn(Optional.empty());

        // When & Then: 예외가 발생하는지 확인
        assertThrows(UserNotFoundException.class,
                () -> projectService.getUserProjects(nonExistentUserId));

        // Verify: userRepository.findById()가 호출되었는지 확인
        verify(userRepository, times(1)).findById(nonExistentUserId);
    }

    @Test
    void deleteProject_Success() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(testProject));

        testMember.setRole(ProjectMember.Role.ADMIN);

        testProject.setMembers(Collections.singletonList(testMember));

        when(projectMemberRepository.findByProject_ProjectIdAndUser_UserId(1L, "admin-user"))
                .thenReturn(Optional.of(testMember));

        assertDoesNotThrow(() -> projectService.deleteProject("admin-user", 1L));
        verify(projectRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteProject_ProjectNotFound() {
        // Given: 프로젝트가 존재하지 않는 경우
        when(projectRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then: 예외 발생 확인
        assertThrows(ProjectNotFoundException.class, () -> projectService.deleteProject("admin-user", 1L));

        // Verify: projectRepository.findById() 호출 확인
        verify(projectRepository, times(1)).findById(1L);
    }

    @Test
    void deleteProject_UserNotAdmin() {
        // Given: 프로젝트와 비관리자 사용자 설정
        Project project = new Project(1L, "Test Project", Project.Status.ACTIVE, LocalDateTime.now(), null, null);
        ProjectMember member = new ProjectMember(1L, project, testUser, ProjectMember.Role.MEMBER, null, null);
        project.setMembers(List.of(member));

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        // When & Then: 예외 발생 확인
        assertThrows(UnauthorizedAccessException.class, () -> projectService.deleteProject("admin-user", 1L));

        // Verify: projectRepository.findById() 호출 확인
        verify(projectRepository, times(1)).findById(1L);
    }

    @Test
    void deleteProject_UnauthorizedAccess() {
        // Given
        Project project = new Project(1L, "Test Project", Project.Status.ACTIVE, LocalDateTime.now(), null, null);
        ProjectMember nonAdminMember = new ProjectMember(1L, project, new User("user1", "User One", "user1@example.com", LocalDateTime.now(), null), ProjectMember.Role.MEMBER, null, null);
        project.setMembers(List.of(nonAdminMember));
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        // When & Then
        assertThrows(UnauthorizedAccessException.class, () -> {
            projectService.deleteProject("user1", 1L);
        });
    }

    @Test
    void addMemberToProject_Success() {
        ProjectMemberSummaryDTO memberDTO = new ProjectMemberSummaryDTO("new-member", "MEMBER");
        when(projectRepository.findById(1L)).thenReturn(Optional.of(testProject));
        when(userRepository.findById("new-member")).thenReturn(Optional.of(testUser));
        when(projectMemberRepository.existsByProject_ProjectIdAndUser_UserId(1L, "new-member")).thenReturn(false);

        ProjectMemberDTO result = projectService.addMemberToProject("admin-user", 1L, memberDTO);

        assertNotNull(result);
        assertEquals("new-member", result.getUserId());
        verify(projectMemberRepository, times(1)).save(any(ProjectMember.class));
    }

    @Test
    void addMemberToProject_ProjectNotFound() {
        // Given: 존재하지 않는 프로젝트 설정
        ProjectMemberSummaryDTO memberDTO = new ProjectMemberSummaryDTO("new-member", "MEMBER");
        when(projectRepository.findById(1L)).thenReturn(Optional.empty()); // 프로젝트가 없음

        // When & Then: 예외 발생 확인
        ProjectNotFoundException exception = assertThrows(ProjectNotFoundException.class,
                () -> projectService.addMemberToProject("admin-user", 1L, memberDTO));

        // Verify: 예외 메시지 확인
        assertEquals("Project not found: ID=1", exception.getMessage());

        // Verify: projectRepository.findById()가 호출되었는지 확인
        verify(projectRepository, times(1)).findById(1L);
    }

    @Test
    void addMemberToProject_UserNotFound() {
        // Given: 사용자 존재하지 않음
        ProjectMemberSummaryDTO memberDTO = new ProjectMemberSummaryDTO("non-existent-user", "MEMBER");
        when(projectRepository.findById(1L)).thenReturn(Optional.of(testProject));
        when(userRepository.findById("non-existent-user")).thenReturn(Optional.empty());

        // When & Then: 예외 발생 확인
        assertThrows(UserNotFoundException.class, () -> projectService.addMemberToProject("admin-user", 1L, memberDTO));

        // Verify: userRepository.findById() 호출 확인
        verify(userRepository, times(1)).findById("non-existent-user");
    }

    @Test
    void addMemberToProject_MemberAlreadyExists() {
        // Given: 이미 존재하는 멤버 설정
        ProjectMemberSummaryDTO memberDTO = new ProjectMemberSummaryDTO("existing-member", "MEMBER");
        when(projectRepository.findById(1L)).thenReturn(Optional.of(testProject));
        when(userRepository.findById("existing-member")).thenReturn(Optional.of(testUser));
        when(projectMemberRepository.existsByProject_ProjectIdAndUser_UserId(1L, "existing-member"))
                .thenReturn(true);

        // When & Then: 예외 발생 확인
        assertThrows(MemberAlreadyExistsException.class, () -> projectService.addMemberToProject("admin-user", 1L, memberDTO));

        // Verify: projectMemberRepository.existsByProject_ProjectIdAndUser_UserId() 호출 확인
        verify(projectMemberRepository, times(1)).existsByProject_ProjectIdAndUser_UserId(1L, "existing-member");
    }

    @Test
    void testAddMemberToProject_DuplicateMember() {
        // Given
        Project project = new Project(1L, "Test Project", Project.Status.ACTIVE, LocalDateTime.now(), null, null);
        User user = new User("user2", "User Two", "user2@example.com", LocalDateTime.now(), null);
        when(projectRepository.findById(anyLong())).thenReturn(Optional.of(project));
        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));
        when(projectMemberRepository.existsByProject_ProjectIdAndUser_UserId(1L, "user2")).thenReturn(true);

        // When & Then
        assertThrows(MemberAlreadyExistsException.class, () -> {
            projectService.addMemberToProject("admin", 1L, new ProjectMemberSummaryDTO("user2", "MEMBER"));
        });
    }

    @Test
    void addMemberToProject_AlreadyExists() {
        ProjectMemberSummaryDTO memberDTO = new ProjectMemberSummaryDTO("new-member", "MEMBER");
        when(projectRepository.findById(1L)).thenReturn(Optional.of(testProject));
        when(userRepository.findById("new-member")).thenReturn(Optional.of(testUser));
        when(projectMemberRepository.existsByProject_ProjectIdAndUser_UserId(1L, "new-member")).thenReturn(true);

        assertThrows(MemberAlreadyExistsException.class, () -> projectService.addMemberToProject("admin-user", 1L, memberDTO));
    }

    @Test
    void removeMemberFromProject_Success() {
        when(projectMemberRepository.findByProject_ProjectIdAndUser_UserId(1L, "new-member"))
                .thenReturn(Optional.of(testMember));

        assertDoesNotThrow(() -> projectService.removeMemberFromProject("admin-user", 1L, "new-member"));
        verify(projectMemberRepository, times(1)).delete(testMember);
    }

    @Test
    void removeMemberFromProject_NotFound() {
        when(projectMemberRepository.findByProject_ProjectIdAndUser_UserId(1L, "new-member"))
                .thenReturn(Optional.empty());

        assertThrows(MemberNotFoundException.class, () -> projectService.removeMemberFromProject("admin-user", 1L, "new-member"));
    }


}
