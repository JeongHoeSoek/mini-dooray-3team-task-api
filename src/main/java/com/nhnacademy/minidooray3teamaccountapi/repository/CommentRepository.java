package com.nhnacademy.minidooray3teamaccountapi.repository;

import com.nhnacademy.minidooray3teamaccountapi.entity.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByTask_TaskId(Long taskId);
    // 추가적인 쿼리 메서드가 필요하다면 여기에 추가
}
