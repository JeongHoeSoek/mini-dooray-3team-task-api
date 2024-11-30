package com.nhnacademy.minidooray3teamaccountapi.repository;

import com.nhnacademy.minidooray3teamaccountapi.entity.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {
    Optional<ProjectMember> findByProjectIdAndUserId(Long projectId, String userId);
    boolean existsByProjectIdAndUserId(Long projectId, String userId);
}

