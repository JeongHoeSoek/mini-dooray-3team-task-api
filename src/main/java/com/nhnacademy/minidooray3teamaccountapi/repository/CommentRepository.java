package com.nhnacademy.minidooray3teamaccountapi.repository;

import com.nhnacademy.minidooray3teamaccountapi.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByTaskId(Long taskId); // 특정 Task에 속한 Comment 조회
    List<Comment> findByProjectMemberId(Long projectMemberId); // 특정 멤버가 작성한 Comment 조회
    List<Comment> findByTaskIdAndProjectMemberId(Long taskId, Long projectMemberId);
    List<Comment> findByProjectMemberIdAndTaskId(Long projectMemberId, Long taskId);
}
