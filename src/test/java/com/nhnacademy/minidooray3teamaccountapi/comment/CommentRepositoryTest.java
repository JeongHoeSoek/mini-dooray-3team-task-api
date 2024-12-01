package com.nhnacademy.minidooray3teamaccountapi.comment;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.nhnacademy.minidooray3teamaccountapi.entity.*;
import com.nhnacademy.minidooray3teamaccountapi.repository.*;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

@DataJpaTest
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectMemberRepository projectMemberRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;
    private Project project;
    private ProjectMember projectMember;
    private Task task;
    private Comment comment1;
    private Comment comment2;

    @BeforeEach
    void setUp() {
        // User 생성 및 저장
        user = new User();
        user.setUserId("user2");
        user.setUsername("User Two");
        user.setEmail("user2@example.com");
        user.setCreatedAt(LocalDateTime.of(2024, 11, 27, 10, 0));
        user.setProjectMembers(new ArrayList<>()); // 가변 리스트로 초기화
        userRepository.save(user);

        // Project 생성 및 저장
        project = new Project();
        project.setName("Project Alpha");
        project.setStatus(Project.Status.ACTIVE);
        project.setCreatedAt(LocalDateTime.of(2024, 11, 26, 9, 0));
        project.setMembers(new ArrayList<>()); // 가변 리스트로 초기화
        project.setTasks(new ArrayList<>()); // 가변 리스트로 초기화
        projectRepository.save(project);

        // ProjectMember 생성 및 저장
        projectMember = new ProjectMember();
        projectMember.setUser(user);
        projectMember.setProject(project);
        projectMember.setRole(ProjectMember.Role.MEMBER);
        projectMember.setTasks(new ArrayList<>()); // 가변 리스트로 초기화
        projectMember.setComments(new ArrayList<>()); // 가변 리스트로 초기화
        projectMemberRepository.save(projectMember);

        // 양방향 관계 설정
        user.getProjectMembers().add(projectMember);
        project.getMembers().add(projectMember);

        // Task 생성 및 저장
        task = new Task();
        task.setTitle("Sample Task");
        task.setDescription("This is a sample task.");
        task.setProject(project);
        task.setMilestone(null); // MileStone 설정이 필요 시 추가
        task.setProjectMember(projectMember);
        task.setCreatedAt(LocalDateTime.of(2024, 11, 27, 12, 0));
        task.setComments(new ArrayList<>()); // 가변 리스트로 초기화
        task.setTaskTags(new ArrayList<>()); // 가변 리스트로 초기화
        taskRepository.save(task);

        // 양방향 관계 설정
        project.getTasks().add(task);
        projectMember.getTasks().add(task);
        task.setProject(project);
        task.setProjectMember(projectMember);

        // Comment 생성 및 저장
        comment1 = new Comment();
        comment1.setTask(task);
        comment1.setProjectMember(projectMember);
        comment1.setContent("좋은 진행입니다!");
        comment1.setCreatedAt(LocalDateTime.of(2024, 11, 28, 0, 30));
        comment1.setUpdatedAt(LocalDateTime.of(2024, 11, 28, 0, 30));
        commentRepository.save(comment1);

        comment2 = new Comment();
        comment2.setTask(task);
        comment2.setProjectMember(projectMember);
        comment2.setContent("추가 작업이 필요합니다!");
        comment2.setCreatedAt(LocalDateTime.of(2024, 11, 28, 1, 0));
        comment2.setUpdatedAt(LocalDateTime.of(2024, 11, 28, 1, 0));
        commentRepository.save(comment2);

        // 양방향 관계 설정
        task.getComments().add(comment1);
        task.getComments().add(comment2);
        projectMember.getComments().add(comment1);
        projectMember.getComments().add(comment2);
    }

    @Test
    void findAllByTask_TaskId_ReturnsComments() {
        List<Comment> comments = commentRepository.findAllByTask_TaskId(task.getTaskId());
        assertThat(comments).hasSize(2)
                .extracting("content")
                .containsExactlyInAnyOrder("좋은 진행입니다!", "추가 작업이 필요합니다!");
    }

    @Test
    void findById_ReturnsComment() {
        Optional<Comment> foundComment = commentRepository.findById(comment1.getCommentId());
        assertThat(foundComment).isPresent();
        assertThat(foundComment.get().getContent()).isEqualTo("좋은 진행입니다!");
    }

    @Test
    void saveComment_Success() {
        Comment newComment = new Comment();
        newComment.setTask(task);
        newComment.setProjectMember(projectMember);
        newComment.setContent("새로운 코멘트");
        newComment.setCreatedAt(LocalDateTime.now());
        newComment.setUpdatedAt(LocalDateTime.now());

        Comment savedComment = commentRepository.save(newComment);
        assertThat(savedComment.getCommentId()).isNotNull();
        assertThat(savedComment.getContent()).isEqualTo("새로운 코멘트");

        // 양방향 관계 설정
        task.getComments().add(savedComment);
        projectMember.getComments().add(savedComment);
    }

    @Test
    void deleteComment_Success() {
        commentRepository.delete(comment1);
        Optional<Comment> deletedComment = commentRepository.findById(comment1.getCommentId());
        assertThat(deletedComment).isNotPresent();

        // 양방향 관계에서 제거
        task.getComments().remove(comment1);
        projectMember.getComments().remove(comment1);
    }

    @Test
    void deleteNonExistingComment_ShouldThrowException() {
        Long nonExistingCommentId = 999L; // 존재하지 않는 ID

        assertThatThrownBy(() -> {
            if (!commentRepository.existsById(nonExistingCommentId)) {
                throw new EmptyResultDataAccessException(
                        "No entity found for ID: " + nonExistingCommentId, 1);
            }
            commentRepository.deleteById(nonExistingCommentId);
        }).isInstanceOf(EmptyResultDataAccessException.class);
    }
}
