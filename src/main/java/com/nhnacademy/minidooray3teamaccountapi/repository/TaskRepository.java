package com.nhnacademy.minidooray3teamaccountapi.repository;

import com.nhnacademy.minidooray3teamaccountapi.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByProject_ProjectId(Long projectId);
    List<Task> findByProjectMember_ProjectMemberId(Long projectMemberId);
    List<Task> findByProject_ProjectIdAndTaskId(Long projectId, Long taskId);
}
