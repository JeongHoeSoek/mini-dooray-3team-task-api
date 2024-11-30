package com.nhnacademy.minidooray3teamaccountapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class Comment2DTO {
    private Long commentId;
    private List<ProjectMemberSummaryDTO> projectMember;
    private String content;
    private LocalDateTime createdAt;
}
