package com.nhnacademy.minidooray3teamaccountapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class CommentDTO {
    private long commentId;
    private List<ProjectMemberDTO> projectMember;
    private String content;
    private LocalDateTime createdAt;
}
