package com.nhnacademy.minidooray3teamaccountapi.repository;

import com.nhnacademy.minidooray3teamaccountapi.entity.Tag;
import com.nhnacademy.minidooray3teamaccountapi.entity.Task;
import com.nhnacademy.minidooray3teamaccountapi.entity.TaskTag;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskTagRepository extends JpaRepository<TaskTag, Long> {
    Optional<TaskTag> findByTaskAndTag(Task task, Tag tag);
    boolean existsByTaskAndTag(Task task, Tag tag);
}
