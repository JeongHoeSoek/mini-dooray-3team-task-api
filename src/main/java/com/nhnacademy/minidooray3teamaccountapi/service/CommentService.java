package com.nhnacademy.minidooray3teamaccountapi.service;

import com.nhnacademy.minidooray3teamaccountapi.dto.CommentRequestDTO;
import com.nhnacademy.minidooray3teamaccountapi.dto.CommentResponseDTO;
import java.util.List;

public interface CommentService {
    CommentResponseDTO createComment(Long projectId, Long taskId, CommentRequestDTO commentRequestDTO, String userId);
    CommentResponseDTO getCommentById(Long projectId, Long taskId, Long commentId);
    List<CommentResponseDTO> getAllCommentsByTask(Long projectId, Long taskId);
    CommentResponseDTO updateComment(Long projectId, Long taskId, Long commentId, CommentRequestDTO commentRequestDTO, String userId);
    void deleteComment(Long projectId, Long taskId, Long commentId, String userId);
}
