package com.nhnacademy.minidooray3teamaccountapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class CommentFindDTO {
    private List<CommentDTO> comment1;
    private List<CommentDTO> comment2;
}
