package com.nhnacademy.minidooray3teamaccountapi.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TaskRequest {
    @NotNull
    private Long projectMemberId;

    @NotNull
    @Size(min = 1 , max = 255)
    private String title;
    
    private String description;
    private Long milestoneId;

}
