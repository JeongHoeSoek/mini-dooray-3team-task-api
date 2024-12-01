package com.nhnacademy.minidooray3teamaccountapi.tag;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.nhnacademy.minidooray3teamaccountapi.entity.Tag;
import org.junit.jupiter.api.Test;

class TagTest {

    @Test
    public void testTagGettersAndSetters() {
        Long tagId = 1L;
        String name = "Test Tag";

        Tag tag = new Tag();
        tag.setTagId(tagId);
        tag.setName(name);

        assertEquals(tagId, tag.getTagId());
        assertEquals(name, tag.getName());
    }

    @Test
    public void testTagConstructor() {
        Long tagId = 1L;
        String name = "Test Tag";

        Tag tag = new Tag(tagId, name, null);

        assertEquals(tagId, tag.getTagId());
        assertEquals(name, tag.getName());
    }
}
