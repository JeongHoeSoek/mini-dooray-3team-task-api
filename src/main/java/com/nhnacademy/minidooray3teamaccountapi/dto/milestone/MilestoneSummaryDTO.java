package com.nhnacademy.minidooray3teamaccountapi.dto.milestone;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MilestoneSummaryDTO {
    private Long id;
    private String name;
    private String status;
}
