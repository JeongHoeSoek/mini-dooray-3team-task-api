package com.nhnacademy.minidooray3teamaccountapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class CommentCreateResponseDTO {
    private long commentId;
    private long taskId;
    private List<ProjectMemberDTO> projectMember;
    private String content;
    private LocalDateTime createdAt;
}
