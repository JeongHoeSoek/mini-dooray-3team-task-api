package com.nhnacademy.minidooray3teamaccountapi.repository;

import com.nhnacademy.minidooray3teamaccountapi.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {
//    List<Tag> findByProject_Id(Long projectId); // 수정: projectId 대신 project 필드를 참조
}
