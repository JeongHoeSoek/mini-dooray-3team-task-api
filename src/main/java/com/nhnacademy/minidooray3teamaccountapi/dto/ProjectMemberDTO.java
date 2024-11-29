package com.nhnacademy.minidooray3teamaccountapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProjectMemberDTO {
    private String userId;
    private String role;
}
