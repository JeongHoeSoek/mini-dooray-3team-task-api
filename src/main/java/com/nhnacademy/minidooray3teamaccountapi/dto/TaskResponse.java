package com.nhnacademy.minidooray3teamaccountapi.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class TaskResponse {
    private Long taskId;
    private String title;
    private String description;
    private MileStoneResponseDTO mileStoneDto;
    private List<TagResponseDTO> tags;
    private List<CommentDTO> comments;
    private ProjectMemberDTO createdBy;
    private LocalDateTime createdAt;
}
