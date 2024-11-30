package com.nhnacademy.minidooray3teamaccountapi.service;

import com.nhnacademy.minidooray3teamaccountapi.dto.CommentRequestDTO;
import com.nhnacademy.minidooray3teamaccountapi.dto.CommentResponseDTO;
import com.nhnacademy.minidooray3teamaccountapi.dto.ProjectMemberDTO;
import com.nhnacademy.minidooray3teamaccountapi.entity.Comment;
import com.nhnacademy.minidooray3teamaccountapi.entity.ProjectMember;
import com.nhnacademy.minidooray3teamaccountapi.entity.Task;
import com.nhnacademy.minidooray3teamaccountapi.exception.ResourceNotFoundException;
import com.nhnacademy.minidooray3teamaccountapi.repository.CommentRepository;
import com.nhnacademy.minidooray3teamaccountapi.repository.ProjectMemberRepository;
import com.nhnacademy.minidooray3teamaccountapi.repository.TaskRepository;
import com.nhnacademy.minidooray3teamaccountapi.service.CommentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final ProjectMemberRepository projectMemberRepository;

    public CommentServiceImpl(CommentRepository commentRepository,
                              TaskRepository taskRepository,
                              ProjectMemberRepository projectMemberRepository) {
        this.commentRepository = commentRepository;
        this.taskRepository = taskRepository;
        this.projectMemberRepository = projectMemberRepository;
    }

    @Override
    public CommentResponseDTO createComment(Long projectId, Long taskId, CommentRequestDTO commentRequestDTO, String userId) {
        Task task = taskRepository.findByProject_ProjectIdAndTaskId(projectId, taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found in the project"));

        ProjectMember projectMember = projectMemberRepository.findByUser_UserIdAndProject_ProjectId(userId, projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project member not found"));

        Comment comment = new Comment();
        comment.setTask(task);
        comment.setProjectMember(projectMember);
        comment.setContent(commentRequestDTO.getContent());

        Comment savedComment = commentRepository.save(comment);

        ProjectMemberDTO projectMemberDTO = new ProjectMemberDTO(
                projectMember.getProjectMemberId(),
                projectMember.getUser().getUserId(),
                projectMember.getRole().name()
        );

        return new CommentResponseDTO(
                savedComment.getCommentId(),
                taskId,
                projectMemberDTO,
                savedComment.getContent(),
                savedComment.getCreatedAt(),
                savedComment.getUpdatedAt()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public CommentResponseDTO getCommentById(Long projectId, Long taskId, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .filter(c -> c.getTask().getTaskId().equals(taskId) &&
                        c.getTask().getProject().getProjectId().equals(projectId))
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));

        ProjectMember projectMember = comment.getProjectMember();

        ProjectMemberDTO projectMemberDTO = new ProjectMemberDTO(
                projectMember.getProjectMemberId(),
                projectMember.getUser().getUserId(),
                projectMember.getRole().name()
        );

        return new CommentResponseDTO(
                comment.getCommentId(),
                taskId,
                projectMemberDTO,
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentResponseDTO> getAllCommentsByTask(Long projectId, Long taskId) {
        List<Comment> comments = commentRepository.findAllByTask_TaskId(taskId);

        return comments.stream()
                .filter(c -> c.getTask().getProject().getProjectId().equals(projectId))
                .map(c -> {
                    ProjectMember projectMember = c.getProjectMember();
                    ProjectMemberDTO projectMemberDTO = new ProjectMemberDTO(
                            projectMember.getProjectMemberId(),
                            projectMember.getUser().getUserId(),
                            projectMember.getRole().name()
                    );
                    return new CommentResponseDTO(
                            c.getCommentId(),
                            taskId,
                            projectMemberDTO,
                            c.getContent(),
                            c.getCreatedAt(),
                            c.getUpdatedAt()
                    );
                })
                .collect(Collectors.toList());
    }

    @Override
    public CommentResponseDTO updateComment(Long projectId, Long taskId, Long commentId, CommentRequestDTO commentRequestDTO, String userId) {
        Comment comment = commentRepository.findById(commentId)
                .filter(c -> c.getTask().getTaskId().equals(taskId) &&
                        c.getTask().getProject().getProjectId().equals(projectId))
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));

        // 권한 확인: 코멘트를 작성한 사용자만 수정할 수 있도록 합니다.
        if (!comment.getProjectMember().getUser().getUserId().equals(userId)) {
            throw new IllegalArgumentException("You do not have permission to update this comment");
        }

        comment.setContent(commentRequestDTO.getContent());

        Comment updatedComment = commentRepository.save(comment);

        ProjectMember projectMember = updatedComment.getProjectMember();

        ProjectMemberDTO projectMemberDTO = new ProjectMemberDTO(
                projectMember.getProjectMemberId(),
                projectMember.getUser().getUserId(),
                projectMember.getRole().name()
        );

        return new CommentResponseDTO(
                updatedComment.getCommentId(),
                taskId,
                projectMemberDTO,
                updatedComment.getContent(),
                updatedComment.getCreatedAt(),
                updatedComment.getUpdatedAt()
        );
    }

    @Override
    public void deleteComment(Long projectId, Long taskId, Long commentId, String userId) {
        Comment comment = commentRepository.findById(commentId)
                .filter(c -> c.getTask().getTaskId().equals(taskId) &&
                        c.getTask().getProject().getProjectId().equals(projectId))
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));

        // 권한 확인: 코멘트를 작성한 사용자만 삭제할 수 있도록 합니다.
        if (!comment.getProjectMember().getUser().getUserId().equals(userId)) {
            throw new IllegalArgumentException("You do not have permission to delete this comment");
        }

        commentRepository.delete(comment);
    }
}
