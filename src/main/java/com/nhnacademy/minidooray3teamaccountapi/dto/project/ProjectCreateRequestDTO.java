package com.nhnacademy.minidooray3teamaccountapi.dto.project;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProjectCreateRequestDTO {
    private String name;
    private String status;
}
