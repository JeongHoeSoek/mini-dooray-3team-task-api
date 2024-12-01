package com.nhnacademy.minidooray3teamaccountapi.tag;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.nhnacademy.minidooray3teamaccountapi.dto.TagResponseDTO;
import org.junit.jupiter.api.Test;

public class TagResponseDTOTest {

    @Test
    public void testTagResponseDTOGettersAndSetters() {
        Long tagId = 1L;
        String name = "Test Tag";

        TagResponseDTO tagResponseDTO = new TagResponseDTO();
        tagResponseDTO.setTagId(tagId);
        tagResponseDTO.setName(name);

        assertEquals(tagId, tagResponseDTO.getTagId());
        assertEquals(name, tagResponseDTO.getName());
    }

    @Test
    public void testTagResponseDTOConstructor() {
        Long tagId = 1L;
        String name = "Test Tag";

        TagResponseDTO tagResponseDTO = new TagResponseDTO(tagId, name);

        assertEquals(tagId, tagResponseDTO.getTagId());
        assertEquals(name, tagResponseDTO.getName());
    }
}
