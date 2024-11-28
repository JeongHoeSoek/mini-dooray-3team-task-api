package com.nhnacademy.minidooray3teamaccountapi.repository;

import com.nhnacademy.minidooray3teamaccountapi.entity.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {
    List<ProjectMember> findByUserId(Long userId); // 특정 사용자의 프로젝트 멤버십 조회
    List<ProjectMember> findByProjectId(Long projectId); // 특정 프로젝트의 멤버 목록 조회
}
