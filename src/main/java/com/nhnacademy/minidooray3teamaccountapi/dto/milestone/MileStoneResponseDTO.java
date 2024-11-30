package com.nhnacademy.minidooray3teamaccountapi.dto.milestone;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MileStoneResponseDTO {
    @JsonProperty("milestoneId")
    private Long id;

    private String name;

    private String status;
}
