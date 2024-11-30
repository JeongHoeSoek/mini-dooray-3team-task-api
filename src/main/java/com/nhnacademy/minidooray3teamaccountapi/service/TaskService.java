package com.nhnacademy.minidooray3teamaccountapi.service;

import com.nhnacademy.minidooray3teamaccountapi.dto.CommentDto;
import com.nhnacademy.minidooray3teamaccountapi.dto.MileStoneResponseDTO;
import com.nhnacademy.minidooray3teamaccountapi.dto.ProjectMemberDTO;
import com.nhnacademy.minidooray3teamaccountapi.dto.TagResponseDTO;
import com.nhnacademy.minidooray3teamaccountapi.dto.TaskRequest;
import com.nhnacademy.minidooray3teamaccountapi.dto.TaskResponse;
import com.nhnacademy.minidooray3teamaccountapi.entity.MileStone;
import com.nhnacademy.minidooray3teamaccountapi.entity.Project;
import com.nhnacademy.minidooray3teamaccountapi.entity.ProjectMember;
import com.nhnacademy.minidooray3teamaccountapi.entity.Task;
import com.nhnacademy.minidooray3teamaccountapi.exception.ResourceNotFoundException;
import com.nhnacademy.minidooray3teamaccountapi.repository.MilestoneRepository;
import com.nhnacademy.minidooray3teamaccountapi.repository.ProjectMemberRepository;
import com.nhnacademy.minidooray3teamaccountapi.repository.ProjectRepository;
import com.nhnacademy.minidooray3teamaccountapi.repository.TaskRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final MilestoneRepository milestoneRepository; // 변수명 수정
    private final ProjectRepository projectRepository;

    public TaskService(TaskRepository taskRepository,
                       ProjectMemberRepository projectMemberRepository,
                       MilestoneRepository milestoneRepository,
                       ProjectRepository projectRepository) {
        this.taskRepository = taskRepository;
        this.projectMemberRepository = projectMemberRepository;
        this.milestoneRepository = milestoneRepository;
        this.projectRepository = projectRepository;
    }

    @Transactional
    public TaskResponse createTask(Long projectId, TaskRequest taskRequest) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        ProjectMember projectMember = projectMemberRepository.findById(taskRequest.getProjectMemberId())
                .orElseThrow(() -> new ResourceNotFoundException("ProjectMember not found"));

        MileStone milestone = null;
        if (taskRequest.getMilestoneId() != null) {
            milestone = milestoneRepository.findById(taskRequest.getMilestoneId())
                    .orElseThrow(() -> new ResourceNotFoundException("Milestone not found"));
        }

        Task task = new Task();
        task.setProject(project);
        task.setProjectMember(projectMember);
        task.setTitle(taskRequest.getTitle());
        task.setDescription(taskRequest.getDescription());
        task.setMilestone(milestone);

        Task savedTask = taskRepository.save(task);

        return toResponse(savedTask);
    }

    public TaskResponse getTaskById(Long projectId, Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        if (!task.getProject().getProjectId().equals(projectId)) {
            throw new ResourceNotFoundException("Task not found in the project");
        }

        return toResponse(task);
    }

    @Transactional
    public TaskResponse updateTask(Long projectId, Long taskId, TaskRequest taskRequest) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        if (!task.getProject().getProjectId().equals(projectId)) {
            throw new ResourceNotFoundException("Task not found in the project");
        }

        if (taskRequest.getTitle() != null) {
            task.setTitle(taskRequest.getTitle());
        }

        if (taskRequest.getDescription() != null) {
            task.setDescription(taskRequest.getDescription());
        }

        if (taskRequest.getMilestoneId() != null) {
            MileStone milestone = milestoneRepository.findById(taskRequest.getMilestoneId())
                    .orElseThrow(() -> new ResourceNotFoundException("Milestone not found"));
            task.setMilestone(milestone);
        }

        return toResponse(task);
    }

    public void deleteTask(Long projectId, Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        if (!task.getProject().getProjectId().equals(projectId)) {
            throw new ResourceNotFoundException("Task not found in the project");
        }

        taskRepository.delete(task);
    }

    public List<TaskResponse> getTasksByMemberId(Long projectId, Long memberId) {
        List<Task> tasks = taskRepository.findByProject_ProjectId(projectId).stream()
                .filter(task -> task.getProjectMember().getProjectMemberId().equals(memberId))
                .collect(Collectors.toList());

        return tasks.stream().map(this::toResponse).collect(Collectors.toList());
    }

    private TaskResponse toResponse(Task task) {
        TaskResponse response = new TaskResponse();
        response.setTaskId(task.getTaskId()); // 필드 이름 수정
        response.setTitle(task.getTitle());
        response.setDescription(task.getDescription());
        response.setCreatedAt(task.getCreatedAt());

        if (task.getMilestone() != null) {
            MileStoneResponseDTO milestoneDto = new MileStoneResponseDTO();
            milestoneDto.setId(task.getMilestone().getMilestoneId());
            milestoneDto.setName(task.getMilestone().getName());
            milestoneDto.setStatus(task.getMilestone().getStatus().name());
            response.setMileStoneDto(milestoneDto); // 메서드 이름 수정
        }

        if (task.getTaskTags() != null) {
            List<TagResponseDTO> tags = task.getTaskTags().stream().map(taskTag -> {
                TagResponseDTO tagDto = new TagResponseDTO();
                tagDto.setTagId(taskTag.getTag().getTagId());
                tagDto.setName(taskTag.getTag().getName());
                return tagDto;
            }).collect(Collectors.toList());
            response.setTags(tags);
        }

        if (task.getComments() != null) {
            List<CommentDto> comments = task.getComments().stream().map(comment -> {
                CommentDto commentDto = new CommentDto();
                commentDto.setCommentId(comment.getCommentId());
                commentDto.setContent(comment.getContent());
                commentDto.setCreatedAt(comment.getCreatedAt());

                ProjectMemberDTO memberDto = new ProjectMemberDTO();
                memberDto.setProjectMemberId(comment.getProjectMember().getProjectMemberId());
                memberDto.setUserId(comment.getProjectMember().getUser().getUserId());
                memberDto.setRole(comment.getProjectMember().getRole().name());
                commentDto.setProjectMember(memberDto);

                return commentDto;
            }).collect(Collectors.toList());
            response.setComments(comments);
        }

        ProjectMemberDTO createdBy = new ProjectMemberDTO();
        createdBy.setProjectMemberId(task.getProjectMember().getProjectMemberId());
        createdBy.setUserId(task.getProjectMember().getUser().getUserId());
        createdBy.setRole(task.getProjectMember().getRole().name());
        response.setCreatedBy(createdBy);

        return response;
    }
}

