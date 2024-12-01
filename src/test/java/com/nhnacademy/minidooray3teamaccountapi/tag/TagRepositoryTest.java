package com.nhnacademy.minidooray3teamaccountapi.tag;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.nhnacademy.minidooray3teamaccountapi.entity.Tag;
import com.nhnacademy.minidooray3teamaccountapi.repository.TagRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@DataJpaTest
@ExtendWith(SpringExtension.class)
public class TagRepositoryTest {

    @Autowired
    private TagRepository tagRepository;

    private Tag tag;

    @BeforeEach
    public void setUp() {
        tag = new Tag();
        tag.setName("Test Tag");
    }

    @Test
    public void testSaveAndFindById() {
        Tag savedTag = tagRepository.save(tag);

        Optional<Tag> retrievedTag = tagRepository.findById(savedTag.getTagId());

        assertTrue(retrievedTag.isPresent());
        assertEquals(savedTag.getTagId(), retrievedTag.get().getTagId());
        assertEquals("Test Tag", retrievedTag.get().getName());
    }

    @Test
    public void testDelete() {
        Tag savedTag = tagRepository.save(tag);
        tagRepository.delete(savedTag);

        Optional<Tag> retrievedTag = tagRepository.findById(savedTag.getTagId());

        assertFalse(retrievedTag.isPresent());
    }
}
