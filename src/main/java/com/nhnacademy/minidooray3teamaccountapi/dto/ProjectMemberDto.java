package com.nhnacademy.minidooray3teamaccountapi.dto;

import lombok.Data;

@Data
public class ProjectMemberDto {
    private Long projectMemberId;
    private String userId;
    private String role;
}
