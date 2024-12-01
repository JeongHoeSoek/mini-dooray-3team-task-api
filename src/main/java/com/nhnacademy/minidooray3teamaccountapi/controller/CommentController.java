package com.nhnacademy.minidooray3teamaccountapi.controller;

import com.nhnacademy.minidooray3teamaccountapi.dto.CommentRequestDTO;
import com.nhnacademy.minidooray3teamaccountapi.dto.CommentResponseDTO;
import com.nhnacademy.minidooray3teamaccountapi.exception.ResourceNotFoundException;
import com.nhnacademy.minidooray3teamaccountapi.service.CommentService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/projects/{projectId}/tasks/{taskId}/comments")
public class CommentController {

    private final CommentService commentService;

    // 생성자 주입 (권장)
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // 코멘트 생성
    @PostMapping
    public ResponseEntity<CommentResponseDTO> createComment(
            @PathVariable Long projectId,
            @PathVariable Long taskId,
            @Valid @RequestBody CommentRequestDTO commentRequestDTO,
            @RequestHeader("X-User-Id") String userId) {
        try {
            CommentResponseDTO createdComment = commentService.createComment(projectId, taskId, commentRequestDTO, userId);
            return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // 특정 코멘트 조회
    @GetMapping("/{commentId}")
    public ResponseEntity<CommentResponseDTO> getCommentById(
            @PathVariable Long projectId,
            @PathVariable Long taskId,
            @PathVariable Long commentId) {
        try {
            CommentResponseDTO comment = commentService.getCommentById(projectId, taskId, commentId);
            return ResponseEntity.ok(comment);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // 특정 태스크의 모든 코멘트 조회
    @GetMapping
    public ResponseEntity<List<CommentResponseDTO>> getAllCommentsByTask(
            @PathVariable Long projectId,
            @PathVariable Long taskId) {
        List<CommentResponseDTO> comments = commentService.getAllCommentsByTask(projectId, taskId);
        return ResponseEntity.ok(comments);
    }

    // 코멘트 수정 (POST로 변경)
    @PostMapping("/{commentId}/update")
    public ResponseEntity<CommentResponseDTO> updateComment(
            @PathVariable Long projectId,
            @PathVariable Long taskId,
            @PathVariable Long commentId,
            @Valid @RequestBody CommentRequestDTO commentRequestDTO,
            @RequestHeader("X-User-Id") String userId) {
        try {
            CommentResponseDTO updatedComment = commentService.updateComment(projectId, taskId, commentId, commentRequestDTO, userId);
            return ResponseEntity.ok(updatedComment);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }


    // 코멘트 삭제 (POST로 변경)
    @PostMapping("/{commentId}/delete")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long projectId,
            @PathVariable Long taskId,
            @PathVariable Long commentId,
            @RequestHeader("X-User-Id") String userId) {
        try {
            commentService.deleteComment(projectId, taskId, commentId, userId);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
}
