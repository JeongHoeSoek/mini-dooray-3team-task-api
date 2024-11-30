package com.nhnacademy.minidooray3teamaccountapi.controller;

import com.nhnacademy.minidooray3teamaccountapi.dto.*;
import com.nhnacademy.minidooray3teamaccountapi.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/users/{userId}/projects")
    public ResponseEntity<List<UserProjectDTO>> getUserProjects(@RequestHeader("X-User-Id") String userId) {
        List<UserProjectDTO> userProjects = projectService.getUserProjects(userId);
        return ResponseEntity.ok(userProjects);
    }

    @DeleteMapping("/{id}")
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

    @DeleteMapping("/{id}/members/{memberUserId}")
    public ResponseEntity<Void> removeMemberFromProject(@RequestHeader("X-User-Id") String userId,
                                                        @PathVariable Long id,
                                                        @PathVariable String memberUserId) {
        projectService.removeMemberFromProject(userId, id, memberUserId);
        return ResponseEntity.noContent().build();
    }
}
