package com.nhnacademy.minidooray3teamaccountapi.service;

import com.nhnacademy.minidooray3teamaccountapi.dto.*;
import java.util.List;

public interface ProjectService {
    ProjectDTO createProject(String userId, ProjectCreateRequestDTO requestDTO);

    ProjectDetailsDTO getProjectDetails(String userId, Long projectId);

    List<UserProjectDTO> getUserProjects(String userId);

    void deleteProject(String userId, Long projectId);

    ProjectMemberDTO addMemberToProject(String userId, Long projectId, ProjectMemberSummaryDTO memberDTO);

    void removeMemberFromProject(String userId, Long projectId, String memberUserId);
}
