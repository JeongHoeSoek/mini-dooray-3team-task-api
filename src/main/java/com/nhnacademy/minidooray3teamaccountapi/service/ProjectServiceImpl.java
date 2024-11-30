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
        // 사용자 확인 및 프로젝트 생성
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + userId));

        Project project = new Project(null, requestDTO.getName(),
                Project.Status.valueOf(requestDTO.getStatus()),
                LocalDateTime.now(), null, null);
        project = projectRepository.save(project);

        // ADMIN 권한으로 생성자 추가
        ProjectMember creator = new ProjectMember(null, project, user,
                ProjectMember.Role.ADMIN, null, null);
        projectMemberRepository.save(creator);

        return new ProjectDTO(project.getProjectId(), project.getName(),
                project.getStatus().name(), project.getCreatedAt());
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectDetailsDTO getProjectDetails(String userId, Long projectId) {
        // 사용자 및 프로젝트 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + userId));
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found: ID=" + projectId));

        // 사용자 권한 확인
        boolean isMember = project.getMembers().stream()
                .anyMatch(member -> member.getUser().getUserId().equals(userId));
        if (!isMember) {
            throw new UnauthorizedAccessException("User is not a member of the project");
        }

        return createProjectDetailsDTO(project);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserProjectDTO> getUserProjects(String userId) {
        // 사용자 확인 및 프로젝트 목록 반환
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + userId));
        return projectRepository.findByMembers_User_UserId(userId).stream()
                .map(project -> new UserProjectDTO(project.getProjectId(),
                        project.getName(), project.getStatus().name()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteProject(String userId, Long projectId) {
        // 프로젝트 확인 및 ADMIN 권한 검증
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found: ID=" + projectId));

        boolean isAdmin = project.getMembers().stream()
                .anyMatch(member -> member.getUser().getUserId().equals(userId) &&
                        member.getRole() == ProjectMember.Role.ADMIN);
        if (!isAdmin) {
            throw new UnauthorizedAccessException("User is not authorized to delete the project");
        }

        projectRepository.deleteById(projectId);
    }

    @Override
    @Transactional
    public ProjectMemberDTO addMemberToProject(String userId, Long projectId, ProjectMemberSummaryDTO memberDTO) {
        // 프로젝트 및 사용자 확인
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found: ID=" + projectId));
        User user = userRepository.findById(memberDTO.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found: " + memberDTO.getUserId()));

        // 중복 멤버 확인
        if (projectMemberRepository.existsByProject_ProjectIdAndUser_UserId(projectId, memberDTO.getUserId())) {
            throw new MemberAlreadyExistsException("Member already exists in the project");
        }

        // 멤버 추가
        ProjectMember member = new ProjectMember(null, project, user,
                ProjectMember.Role.valueOf(memberDTO.getRole()), null, null);
        projectMemberRepository.save(member);

        return new ProjectMemberDTO(projectId, memberDTO.getUserId(), memberDTO.getRole());
    }


    @Override
    @Transactional
    public void removeMemberFromProject(String userId, Long projectId, String memberUserId) {
        // 프로젝트 멤버 확인
        ProjectMember member = projectMemberRepository
                .findByProject_ProjectIdAndUser_UserId(projectId, memberUserId)
                .orElseThrow(() -> new MemberNotFoundException("Member not found in the project"));

        // 멤버 삭제
        projectMemberRepository.delete(member);
    }

    /**
     * 프로젝트의 상세 정보를 반환하는 내부 메서드
     */
    private ProjectDetailsDTO createProjectDetailsDTO(Project project) {
        List<ProjectMemberSummaryDTO> members = project.getMembers().stream()
                .map(member -> new ProjectMemberSummaryDTO(member.getUser().getUserId(),
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
                .map(taskTag -> new TagSummaryDTO(taskTag.getTag().getTagId(),
                        taskTag.getTag().getName()))
                .distinct()
                .collect(Collectors.toList());

        return new ProjectDetailsDTO(project.getProjectId(),
                project.getName(),
                project.getStatus().name(),
                project.getCreatedAt(),
                members,
                tasks,
                milestones,
                tags);
    }
}
