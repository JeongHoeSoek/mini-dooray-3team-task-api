package com.nhnacademy.minidooray3teamaccountapi.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentResponseDTO {
    private Long commentId;
    private Long taskId;
    private ProjectMemberDTO projectMember;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
