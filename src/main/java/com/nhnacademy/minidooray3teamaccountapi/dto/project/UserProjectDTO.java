package com.nhnacademy.minidooray3teamaccountapi.dto.project;

import com.nhnacademy.minidooray3teamaccountapi.entity.Project.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserProjectDTO {
    private Long projectId;
    private String name;
    private String status;
}
