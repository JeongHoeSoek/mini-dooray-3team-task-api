package com.nhnacademy.minidooray3teamaccountapi.controller;

import com.nhnacademy.minidooray3teamaccountapi.dto.*;
import com.nhnacademy.minidooray3teamaccountapi.entity.Task;
import com.nhnacademy.minidooray3teamaccountapi.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // 댓글 생성
    @PostMapping
    public ResponseEntity<CommentDTO> createComment(@RequestBody CommentCreateDTO commentCreateDTO) {
        // Task 객체를 직접 생성하거나 서비스에서 조회하여 전달
        Task task = new Task(); // 실제로는 TaskService 등을 통해 Task 조회 로직이 필요


        CommentDTO commentDTO = commentService.createComment(
                task,
                commentCreateDTO.getProjectMemberId(),
                commentCreateDTO.getContent()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(commentDTO);
    }

    // 특정 태스크의 댓글 조회
    @GetMapping("/task/{taskId}")
    public ResponseEntity<List<CommentDTO>> getCommentsByTaskId(@PathVariable long taskId) {
        List<CommentDTO> comments = commentService.getCommentsByTaskId(taskId);
        return ResponseEntity.ok(comments);
    }

    // 댓글 수정
    @PutMapping("/{commentId}")
    public ResponseEntity<CommentUpdateResponseDTO> updateComment(
            @PathVariable long commentId,
            @RequestBody CommentUpdate commentUpdate) {
        CommentUpdateResponseDTO updatedComment = commentService.updateComment(commentId, commentUpdate);
        return ResponseEntity.ok(updatedComment);
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }
}
