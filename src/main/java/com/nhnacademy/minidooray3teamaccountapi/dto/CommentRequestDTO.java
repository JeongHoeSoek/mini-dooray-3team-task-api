package com.nhnacademy.minidooray3teamaccountapi.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CommentRequestDTO {
    @NotBlank(message = "코멘트 내용은 필수입니다.")
    private String content;
}
