package com.nhnacademy.minidooray3teamaccountapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CommentFindDTO {
    private List<Comment2DTO> comment1;
    private List<Comment2DTO> comment2;
}
