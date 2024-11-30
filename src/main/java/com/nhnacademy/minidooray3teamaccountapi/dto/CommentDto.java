package com.nhnacademy.minidooray3teamaccountapi.dto;

import com.nhnacademy.minidooray3teamaccountapi.dto.project.ProjectMemberDTO;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CommentDto {
    private Long commentId;
    private ProjectMemberDTO projectMember;
    private String content;
    private LocalDateTime createdAt;
}
