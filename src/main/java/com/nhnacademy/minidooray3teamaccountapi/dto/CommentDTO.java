package com.nhnacademy.minidooray3teamaccountapi.dto;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class CommentDTO {
    private Long commentId;
    private ProjectMemberDTO projectMember;
    private String content;
    private LocalDateTime createdAt;
}
