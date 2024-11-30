package com.nhnacademy.minidooray3teamaccountapi.service;


import com.nhnacademy.minidooray3teamaccountapi.dto.*;
import com.nhnacademy.minidooray3teamaccountapi.entity.Task;

import java.util.List;

public interface CommentService {
    // 댓글 생성
    Comment2DTO createComment(Task taskId, long projectMemberId, String content);

    // 특정 태스크(taskId)의 댓글 조회
    List<Comment2DTO> getCommentsByTaskId(long taskId);

    // 댓글 수정
    CommentUpdateResponseDTO updateComment(long commentId, CommentUpdate commentUpdate);

    // 댓글 삭제
    void deleteComment(long commentId);
}
