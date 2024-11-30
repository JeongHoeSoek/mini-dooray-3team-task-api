package com.nhnacademy.minidooray3teamaccountapi.dto.project;

import com.nhnacademy.minidooray3teamaccountapi.dto.milestone.MilestoneSummaryDTO;
import com.nhnacademy.minidooray3teamaccountapi.dto.project.ProjectMemberDTO;
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
    private List<com.nhnacademy.minidooray3teamaccountapi.dto.TagSummaryDTO> tags;
}
