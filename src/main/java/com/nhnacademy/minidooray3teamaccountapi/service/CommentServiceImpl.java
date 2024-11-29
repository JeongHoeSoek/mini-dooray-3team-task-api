package com.nhnacademy.minidooray3teamaccountapi.service;

import com.nhnacademy.minidooray3teamaccountapi.dto.*;
import com.nhnacademy.minidooray3teamaccountapi.entity.Comment;
import com.nhnacademy.minidooray3teamaccountapi.entity.ProjectMember;
import com.nhnacademy.minidooray3teamaccountapi.entity.Task;
import com.nhnacademy.minidooray3teamaccountapi.repository.CommentRepository;
import com.nhnacademy.minidooray3teamaccountapi.repository.ProjectMemberRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final ProjectMemberRepository projectMemberRepository;

    public CommentServiceImpl(CommentRepository commentRepository, ProjectMemberRepository projectMemberRepository) {
        this.commentRepository = commentRepository;
        this.projectMemberRepository = projectMemberRepository;
    }

    @Override
    public CommentDTO createComment(Task taskId, long projectMemberId, String content) {
        // 프로젝트 멤버 조회
        ProjectMember projectMember = projectMemberRepository.findById(projectMemberId)
                .orElseThrow(() -> new IllegalArgumentException("Project member not found"));

        // 댓글 엔티티 생성
        Comment comment = new Comment();
        comment.setTask(taskId);
        comment.setProjectMember(projectMember);
        comment.setContent(content);
        comment.setCreatedAt(LocalDateTime.now());

        // 댓글 저장
        Comment savedComment = commentRepository.save(comment);

        // CommentDTO로 변환하여 반환
        return new CommentDTO(
                savedComment.getId(),
                List.of(new ProjectMemberDTO(
                        savedComment.getProjectMember().getUser().getId(),
                        savedComment.getProjectMember().getRole().name()
                )),
                savedComment.getContent(),
                savedComment.getCreatedAt()
        );
    }

    @Override
    public List<CommentDTO> getCommentsByTaskId(long taskId) {
        // 특정 태스크의 댓글 조회
        List<Comment> comments = commentRepository.findByTaskId(taskId);

        // CommentDTO로 변환
        return comments.stream().map(comment -> new CommentDTO(
                comment.getId(),
                List.of(new ProjectMemberDTO(
                        comment.getProjectMember().getUser().getId(),
                        comment.getProjectMember().getRole().name()
                )),
                comment.getContent(),
                comment.getCreatedAt()
        )).collect(Collectors.toList());
    }

    @Override
    public CommentUpdateResponseDTO updateComment(long commentId, CommentUpdate commentUpdate) {
        // 댓글 조회
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));

        // 댓글 내용 수정
        comment.setContent(commentUpdate.getContent());
        comment.setUpdatedAt(LocalDateTime.now());

        // 수정된 댓글 저장
        Comment updatedComment = commentRepository.save(comment);

        // CommentUpdateResponseDTO로 변환하여 반환
        return new CommentUpdateResponseDTO(
                updatedComment.getId(),
                updatedComment.getTask().getId(),
                List.of(new ProjectMemberDTO(
                        updatedComment.getProjectMember().getUser().getId(),
                        updatedComment.getProjectMember().getRole().name()
                )),
                updatedComment.getContent(),
                updatedComment.getCreatedAt(),
                updatedComment.getUpdatedAt()
        );
    }

    @Override
    public void deleteComment(long commentId) {
        // 댓글 존재 여부 확인
        if (!commentRepository.existsById(commentId)) {
            throw new IllegalArgumentException("Comment not found");
        }

        // 댓글 삭제
        commentRepository.deleteById(commentId);
    }
}
