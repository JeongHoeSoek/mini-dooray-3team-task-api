package com.nhnacademy.minidooray3teamaccountapi.service;

import com.nhnacademy.minidooray3teamaccountapi.dto.CommentDTO;
import com.nhnacademy.minidooray3teamaccountapi.dto.MileStoneResponseDTO;
import com.nhnacademy.minidooray3teamaccountapi.dto.ProjectMemberDTO;
import com.nhnacademy.minidooray3teamaccountapi.dto.TagResponseDTO;
import com.nhnacademy.minidooray3teamaccountapi.dto.TaskRequest;
import com.nhnacademy.minidooray3teamaccountapi.dto.TaskResponse;
import com.nhnacademy.minidooray3teamaccountapi.entity.MileStone;
import com.nhnacademy.minidooray3teamaccountapi.entity.Project;
import com.nhnacademy.minidooray3teamaccountapi.entity.ProjectMember;
import com.nhnacademy.minidooray3teamaccountapi.entity.Tag;
import com.nhnacademy.minidooray3teamaccountapi.entity.Task;
import com.nhnacademy.minidooray3teamaccountapi.entity.TaskTag;
import com.nhnacademy.minidooray3teamaccountapi.exception.ResourceNotFoundException;
import com.nhnacademy.minidooray3teamaccountapi.repository.MilestoneRepository;
import com.nhnacademy.minidooray3teamaccountapi.repository.ProjectMemberRepository;
import com.nhnacademy.minidooray3teamaccountapi.repository.ProjectRepository;
import com.nhnacademy.minidooray3teamaccountapi.repository.TagRepository;
import com.nhnacademy.minidooray3teamaccountapi.repository.TaskRepository;
import com.nhnacademy.minidooray3teamaccountapi.repository.TaskTagRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final MilestoneRepository milestoneRepository;
    private final ProjectRepository projectRepository;
    private final TagRepository tagRepository;
    private final TaskTagRepository taskTagRepository;

    public TaskService(TaskRepository taskRepository,
                       ProjectMemberRepository projectMemberRepository,
                       MilestoneRepository milestoneRepository,
                       ProjectRepository projectRepository,
                       TagRepository tagRepository,
                       TaskTagRepository taskTagRepository) {
        this.taskRepository = taskRepository;
        this.projectMemberRepository = projectMemberRepository;
        this.milestoneRepository = milestoneRepository;
        this.projectRepository = projectRepository;
        this.tagRepository = tagRepository;
        this.taskTagRepository = taskTagRepository;
    }

    @Transactional
    public TaskResponse createTask(Long projectId, TaskRequest taskRequest) {
        Project project = projectRepository.findById(projectId) // 프로젝트 존재 확인
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        ProjectMember projectMember = projectMemberRepository.findById(taskRequest.getProjectMemberId()) // 프로젝트 멤버 확인
                .orElseThrow(() -> new ResourceNotFoundException("ProjectMember not found"));

        MileStone milestone = null; // 마일스톤 존재 확인
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

    // 태스크에 태그 추가
    @Transactional
    public TagResponseDTO addTagToTask(Long projectId, Long taskId, Long tagId, String userId) {
        Task task = taskRepository.findByProject_ProjectIdAndTaskId(projectId, taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found in the project"));

        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new ResourceNotFoundException("Tag not found"));

        boolean exists = taskTagRepository.existsByTaskAndTag(task, tag);
        if (exists) {
            throw new IllegalArgumentException("Tag already assigned to the task");
        }

        TaskTag taskTag = new TaskTag();
        taskTag.setTask(task);
        taskTag.setTag(tag);

        taskTagRepository.save(taskTag);

        TagResponseDTO tagResponseDTO = new TagResponseDTO(tag.getTagId(), tag.getName());

        return tagResponseDTO;
    }

    // 태스크에서 태그 제거
    @Transactional
    public void removeTagFromTask(Long projectId, Long taskId, Long tagId, String userId) {
        Task task = taskRepository.findByProject_ProjectIdAndTaskId(projectId, taskId).
                orElseThrow(() -> new ResourceNotFoundException("Task not found in the project"));

        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new ResourceNotFoundException("Tag not found"));

        TaskTag taskTag = taskTagRepository.findByTaskAndTag(task, tag)
                .orElseThrow(() -> new ResourceNotFoundException("Tag not assigned to the task"));

        taskTagRepository.delete(taskTag);
    }

    private TaskResponse toResponse(Task task) {
        TaskResponse response = new TaskResponse();
        response.setTaskId(task.getTaskId());
        response.setTitle(task.getTitle());
        response.setDescription(task.getDescription());
        response.setCreatedAt(task.getCreatedAt());

        // 마일스톤 매핑
        if (task.getMilestone() != null) {
            MileStoneResponseDTO milestoneDto = new MileStoneResponseDTO(
                    task.getMilestone().getMilestoneId(),
                    task.getMilestone().getName(),
                    task.getMilestone().getStatus().name()
            );
            response.setMileStoneDto(milestoneDto);
        }

        // 태그 매핑
        if (task.getTaskTags() != null) {
            List<TagResponseDTO> tags = task.getTaskTags().stream()
                    .map(taskTag -> new TagResponseDTO(taskTag.getTag().getTagId(), taskTag.getTag().getName()))
                    .collect(Collectors.toList());
            response.setTags(tags);
        }

        // 댓글 매핑
        if (task.getComments() != null) {
            List<CommentDTO> comments = task.getComments().stream()
                    .map(comment -> {
                        CommentDTO commentDto = new CommentDTO();
                        commentDto.setCommentId(comment.getCommentId());
                        commentDto.setContent(comment.getContent());
                        commentDto.setCreatedAt(comment.getCreatedAt());

                        ProjectMemberDTO memberDto = new ProjectMemberDTO();
                        memberDto.setProjectMemberId(comment.getProjectMember().getProjectMemberId());
                        memberDto.setUserId(comment.getProjectMember().getUser().getUserId());
                        memberDto.setRole(comment.getProjectMember().getRole().name());
                        commentDto.setProjectMember(memberDto);

                        return commentDto;
                    })
                    .collect(Collectors.toList());
            response.setComments(comments);
        }

        // 작성자 매핑
        ProjectMemberDTO createdBy = new ProjectMemberDTO();
        createdBy.setProjectMemberId(task.getProjectMember().getProjectMemberId());
        createdBy.setUserId(task.getProjectMember().getUser().getUserId());
        createdBy.setRole(task.getProjectMember().getRole().name());
        response.setCreatedBy(createdBy);

        return response;
    }

    public List<TaskResponse> getTasksByProjectId(Long projectId) {
        List<Task> tasks = taskRepository.findByProject_ProjectId(projectId);

        if (tasks.isEmpty()) {
            throw new ResourceNotFoundException("No tasks found for the given project");
        }

        return tasks.stream().map(this::toResponse).collect(Collectors.toList());
    }

}