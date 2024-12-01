package com.nhnacademy.minidooray3teamaccountapi.milestone;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.nhnacademy.minidooray3teamaccountapi.dto.MileStoneRequestDTO;
import com.nhnacademy.minidooray3teamaccountapi.entity.MileStone;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MileStoneRequestDTOTest {

    @Test
    void testGettersAndSetters() {
        MileStoneRequestDTO dto = new MileStoneRequestDTO();
        dto.setName("Test Milestone");
        dto.setStatus(MileStone.Status.START);

        Assertions.assertEquals("Test Milestone", dto.getName());
        Assertions.assertEquals(MileStone.Status.START, dto.getStatus());
    }

    @Test
    void testAllArgsConstructor() {
        MileStoneRequestDTO dto = new MileStoneRequestDTO("Test Milestone", MileStone.Status.START);

        assertEquals("Test Milestone", dto.getName());
        assertEquals(MileStone.Status.START, dto.getStatus());
    }
}
