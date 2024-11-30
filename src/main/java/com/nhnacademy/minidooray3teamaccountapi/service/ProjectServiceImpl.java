package com.nhnacademy.minidooray3teamaccountapi.service;

import com.nhnacademy.minidooray3teamaccountapi.dto.*;
import com.nhnacademy.minidooray3teamaccountapi.entity.Project;
import com.nhnacademy.minidooray3teamaccountapi.entity.ProjectMember;
import com.nhnacademy.minidooray3teamaccountapi.entity.User;
import com.nhnacademy.minidooray3teamaccountapi.exception.*;
import com.nhnacademy.minidooray3teamaccountapi.repository.ProjectMemberRepository;
import com.nhnacademy.minidooray3teamaccountapi.repository.ProjectRepository;
import com.nhnacademy.minidooray3teamaccountapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ProjectDTO createProject(String userId, ProjectCreateRequestDTO requestDTO) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));

        Project project = new Project(null, requestDTO.getName(), Project.Status.valueOf(requestDTO.getStatus()), LocalDateTime.now(), null, null);
        project = projectRepository.save(project);

        // 프로젝트 생성자 추가
        ProjectMember creator = new ProjectMember(null, project, user, ProjectMember.Role.ADMIN, null, null);
        projectMemberRepository.save(creator);

        return new ProjectDTO(project.getProjectId(), project.getName(), project.getStatus().name(), project.getCreatedAt());
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectDetailsDTO getProjectDetails(String userId, Long projectId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new ProjectNotFoundException("Project not found"));

        // 검증: 사용자가 프로젝트 멤버인지 확인
        boolean isMember = project.getMembers().stream()
                .anyMatch(member -> member.getUser().getUserId().equals(userId));
        if (!isMember) {
            throw new UnauthorizedAccessException("User is not a member of the project");
        }

        List<ProjectMemberSummaryDTO> members = project.getMembers().stream()
                .map(member -> new ProjectMemberSummaryDTO(
                        member.getUser().getUserId(),
                        member.getRole().name()))
                .collect(Collectors.toList());


        List<TaskSummaryDTO> tasks = project.getTasks().stream()
                .map(task -> new TaskSummaryDTO(task.getTaskId(), task.getTitle()))
                .collect(Collectors.toList());

        List<MilestoneSummaryDTO> milestones = project.getTasks().stream()
                .filter(task -> task.getMilestone() != null)
                .map(task -> new MilestoneSummaryDTO(
                        task.getMilestone().getMilestoneId(),
                        task.getMilestone().getName(),
                        task.getMilestone().getStatus().name()))
                .distinct()
                .collect(Collectors.toList());

        List<TagSummaryDTO> tags = project.getTasks().stream()
                .flatMap(task -> task.getTaskTags().stream())
                .map(taskTag -> new TagSummaryDTO(taskTag.getTag().getTagId(), taskTag.getTag().getName()))
                .distinct()
                .collect(Collectors.toList());

        return new ProjectDetailsDTO(
                project.getProjectId(),
                project.getName(),
                project.getStatus().name(),
                project.getCreatedAt(),
                members,
                tasks,
                milestones,
                tags
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserProjectDTO> getUserProjects(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
        return projectRepository.findByMembers_User_UserId(userId).stream()
                .map(project -> new UserProjectDTO(project.getProjectId(), project.getName(), project.getStatus().name()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteProject(String userId, Long projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new ProjectNotFoundException("Project not found"));

        // 검증: 사용자가 ADMIN인지 확인
        boolean isAdmin = project.getMembers().stream()
                .anyMatch(member -> member.getUser().getUserId().equals(userId) && member.getRole() == ProjectMember.Role.ADMIN);
        if (!isAdmin) {
            throw new UnauthorizedAccessException("User is not authorized to delete the project");
        }

        projectRepository.deleteById(projectId);
    }

    @Override
    @Transactional
    public ProjectMemberDTO addMemberToProject(String userId, Long projectId, ProjectMemberSummaryDTO memberDTO) {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new ProjectNotFoundException("Project not found"));
        User user = userRepository.findById(memberDTO.getUserId()).orElseThrow(() -> new UserNotFoundException("User not found"));

        // 변경된 메서드 이름 사용
        if (projectMemberRepository.existsByProject_ProjectIdAndUser_UserId(projectId, memberDTO.getUserId())) {
            throw new MemberAlreadyExistsException("Member already exists in the project");
        }

        ProjectMember member = new ProjectMember(null, project, user, ProjectMember.Role.valueOf(memberDTO.getRole()), null, null);
        projectMemberRepository.save(member);

        return new ProjectMemberDTO(projectId, memberDTO.getUserId(), memberDTO.getRole());
    }

    @Override
    @Transactional
    public void removeMemberFromProject(String userId, Long projectId, String memberUserId) {
        // 변경된 메서드 이름 사용
        ProjectMember member = projectMemberRepository.findByProject_ProjectIdAndUser_UserId(projectId, memberUserId)
                .orElseThrow(() -> new MemberNotFoundException("Member not found in the project"));

        projectMemberRepository.delete(member);
    }

}
