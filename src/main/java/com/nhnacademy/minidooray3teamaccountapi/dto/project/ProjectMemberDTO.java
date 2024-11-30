package com.nhnacademy.minidooray3teamaccountapi.dto.project;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectMemberDTO {
    private Long projectMemberId;
    private String userId;
    private String role;
}
