package com.nhnacademy.minidooray3teamaccountapi.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CommentDto {
    private Long commentId;
    private ProjectMemberDto projectMember;
    private String content;
    private LocalDateTime createdAt;
}
