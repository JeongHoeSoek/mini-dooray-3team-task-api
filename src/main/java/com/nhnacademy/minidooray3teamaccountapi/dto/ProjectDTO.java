package com.nhnacademy.minidooray3teamaccountapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ProjectDTO {
    private Long projectId;
    private String name;
    private String status;
    private LocalDateTime createdAt;
}
