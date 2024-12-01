package com.nhnacademy.minidooray3teamaccountapi.tag;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.nhnacademy.minidooray3teamaccountapi.dto.TagRequestDTO;
import org.junit.jupiter.api.Test;

class TagRequestDTOTest {

    @Test
    public void testTagRequestDTOGettersAndSetters() {
        String name = "Test Tag";

        TagRequestDTO tagRequestDTO = new TagRequestDTO();
        tagRequestDTO.setName(name);

        assertEquals(name, tagRequestDTO.getName());
    }

    @Test
    public void testTagRequestDTOConstructor() {
        String name = "Test Tag";

        TagRequestDTO tagRequestDTO = new TagRequestDTO(name);

        assertEquals(name, tagRequestDTO.getName());
    }
}
