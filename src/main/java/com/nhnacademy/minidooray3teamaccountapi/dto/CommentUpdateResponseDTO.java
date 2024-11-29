package com.nhnacademy.minidooray3teamaccountapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class CommentUpdateResponseDTO {
    private long commentId;
    private long taskId;
    private List<ProjectMemberDTO> projectMember;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
