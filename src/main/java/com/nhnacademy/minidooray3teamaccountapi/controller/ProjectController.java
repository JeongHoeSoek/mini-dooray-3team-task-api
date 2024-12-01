package com.nhnacademy.minidooray3teamaccountapi.controller;

import com.nhnacademy.minidooray3teamaccountapi.dto.ProjectCreateRequestDTO;
import com.nhnacademy.minidooray3teamaccountapi.dto.ProjectDTO;
import com.nhnacademy.minidooray3teamaccountapi.dto.ProjectDetailsDTO;
import com.nhnacademy.minidooray3teamaccountapi.dto.ProjectMemberDTO;
import com.nhnacademy.minidooray3teamaccountapi.dto.ProjectMemberSummaryDTO;
import com.nhnacademy.minidooray3teamaccountapi.dto.UserProjectDTO;
import com.nhnacademy.minidooray3teamaccountapi.service.ProjectService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<ProjectDTO> createProject(@RequestHeader("X-User-Id") String userId,
                                                    @RequestBody ProjectCreateRequestDTO requestDTO) {
        ProjectDTO project = projectService.createProject(userId, requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(project);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDetailsDTO> getProjectDetails(@RequestHeader("X-User-Id") String userId,
                                                               @PathVariable Long id) {
        ProjectDetailsDTO projectDetails = projectService.getProjectDetails(userId, id);
        return ResponseEntity.ok(projectDetails);
    }
    
    // SonarQube에서 Bind path variable "userId" to a method parameter. 이런 오류로 수정
    @GetMapping("/users/{userId}/projects")
    public ResponseEntity<List<UserProjectDTO>> getUserProjects(
            @RequestHeader("X-User-Id") String headerUserId,
            @PathVariable String userId) {

        // 필요에 따라 헤더의 userId와 경로 변수의 userId를 비교하거나 사용합니다.
        List<UserProjectDTO> userProjects = projectService.getUserProjects(userId);
        return ResponseEntity.ok(userProjects);
    }


    // 프로젝트 삭제 (DELETE → POST)
    @PostMapping("/{id}/delete")
    public ResponseEntity<Void> deleteProject(@RequestHeader("X-User-Id") String userId, @PathVariable Long id) {
        projectService.deleteProject(userId, id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/members")
    public ResponseEntity<ProjectMemberDTO> addMemberToProject(@RequestHeader("X-User-Id") String userId,
                                                               @PathVariable Long id,
                                                               @RequestBody ProjectMemberSummaryDTO memberDTO) {
        ProjectMemberDTO projectMember = projectService.addMemberToProject(userId, id, memberDTO);
        return ResponseEntity.ok(projectMember);
    }

    // 프로젝트 멤버 삭제 (DELETE → POST)
    @PostMapping("/{id}/members/{memberUserId}/delete")
    public ResponseEntity<Void> removeMemberFromProject(@RequestHeader("X-User-Id") String userId,
                                                        @PathVariable Long id,
                                                        @PathVariable String memberUserId) {
        projectService.removeMemberFromProject(userId, id, memberUserId);
        return ResponseEntity.noContent().build();
    }
}
