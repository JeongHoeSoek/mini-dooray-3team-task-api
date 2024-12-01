package com.nhnacademy.minidooray3teamaccountapi.milestone;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.nhnacademy.minidooray3teamaccountapi.dto.MileStoneResponseDTO;
import org.junit.jupiter.api.Test;

class MileStoneResponseDTOTest {

    @Test
    void testGettersAndSetters() {
        MileStoneResponseDTO dto = new MileStoneResponseDTO();
        dto.setId(1L);
        dto.setName("Test Milestone");
        dto.setStatus("START");

        assertEquals(1L, dto.getId());
        assertEquals("Test Milestone", dto.getName());
        assertEquals("START", dto.getStatus());
    }

    @Test
    void testAllArgsConstructor() {
        MileStoneResponseDTO dto = new MileStoneResponseDTO(1L, "Test Milestone", "START");

        assertEquals(1L, dto.getId());
        assertEquals("Test Milestone", dto.getName());
        assertEquals("START", dto.getStatus());
    }
}
