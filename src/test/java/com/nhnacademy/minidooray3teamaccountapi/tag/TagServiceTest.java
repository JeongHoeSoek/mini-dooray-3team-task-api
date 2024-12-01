package com.nhnacademy.minidooray3teamaccountapi.tag;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nhnacademy.minidooray3teamaccountapi.dto.TagRequestDTO;
import com.nhnacademy.minidooray3teamaccountapi.dto.TagResponseDTO;
import com.nhnacademy.minidooray3teamaccountapi.entity.Tag;
import com.nhnacademy.minidooray3teamaccountapi.repository.TagRepository;
import com.nhnacademy.minidooray3teamaccountapi.service.TagService;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TagServiceTest {

    @Mock
    private TagRepository tagRepository;

    @Spy
    @InjectMocks
    private TagService tagService;

    private Long projectId;
    private Long tagId;
    private TagRequestDTO tagRequestDTO;
    private Tag tag;

    @BeforeEach
    public void setUp() {
        projectId = 1L;
        tagId = 1L;
        tagRequestDTO = new TagRequestDTO("Test Tag");
        tag = new Tag(tagId, "Test Tag", null);
    }

    @Test
    public void testCreateTag_Success() {
        when(tagRepository.save(any(Tag.class))).thenReturn(tag);

        TagResponseDTO responseDTO = tagService.createTag(projectId, tagRequestDTO);

        assertNotNull(responseDTO);
        assertEquals(tagId, responseDTO.getTagId());
        assertEquals("Test Tag", responseDTO.getName());

        verify(tagRepository, times(1)).save(any(Tag.class));
    }

    @Test
    public void testCreateTag_NullEntity() {
        doReturn(null).when(tagService).toEntity(any(TagRequestDTO.class));

        TagResponseDTO responseDTO = tagService.createTag(projectId, tagRequestDTO);

        assertNull(responseDTO);

        verify(tagService, times(1)).toEntity(any(TagRequestDTO.class));
        verify(tagRepository, never()).save(any(Tag.class));
    }

    @Test
    public void testDeleteTag_Success() {
        when(tagRepository.findById(tagId)).thenReturn(Optional.of(tag));

        TagResponseDTO responseDTO = tagService.deleteTag(projectId, tagId);

        assertNotNull(responseDTO);
        assertEquals(tagId, responseDTO.getTagId());
        assertEquals("Test Tag", responseDTO.getName());

        verify(tagRepository, times(1)).findById(tagId);
        verify(tagRepository, times(1)).delete(tag);
    }

    @Test
    public void testDeleteTag_NotFound() {
        when(tagRepository.findById(tagId)).thenReturn(Optional.empty());

        TagResponseDTO responseDTO = tagService.deleteTag(projectId, tagId);

        assertNull(responseDTO);

        verify(tagRepository, times(1)).findById(tagId);
        verify(tagRepository, never()).delete(any(Tag.class));
    }
}
