package com.nhnacademy.minidooray3teamaccountapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class ProjectDetailsDTO {
    private Long projectId;
    private String name;
    private String status;
    private LocalDateTime createdAt;
    private List<ProjectMemberDTO> projectMembers;
    private List<TaskSummaryDTO> tasks;
    private List<MilestoneSummaryDTO> milestones;
    private List<TagSummaryDTO> tags;
}
