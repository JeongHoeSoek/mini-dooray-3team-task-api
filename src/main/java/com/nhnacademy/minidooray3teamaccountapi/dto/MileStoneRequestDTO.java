package com.nhnacademy.minidooray3teamaccountapi.dto;

import com.nhnacademy.minidooray3teamaccountapi.entity.MileStone;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MileStoneRequestDTO {
    private String name;
    private MileStone.Status status;
}
