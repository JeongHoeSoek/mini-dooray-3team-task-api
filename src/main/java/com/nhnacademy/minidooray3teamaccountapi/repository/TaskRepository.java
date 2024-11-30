package com.nhnacademy.minidooray3teamaccountapi.repository;

import com.nhnacademy.minidooray3teamaccountapi.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByProject_ProjectId(Long projectId);
    List<Task> findByProjectMember_ProjectMemberId(Long projectMemberId);

    // 단일 태스크 조회를 위한 Optional 반환 타입으로 변경
    Optional<Task> findByProject_ProjectIdAndTaskId(Long projectId, Long taskId);
}

