package com.nhnacademy.minidooray3teamaccountapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class CommentUpdateResponseDTO {
    private long commentId;
    private long taskId;
    private List<ProjectMemberSummaryDTO> projectMember;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
