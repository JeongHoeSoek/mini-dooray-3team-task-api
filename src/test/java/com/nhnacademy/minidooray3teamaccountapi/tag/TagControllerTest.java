package com.nhnacademy.minidooray3teamaccountapi.tag;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.minidooray3teamaccountapi.controller.TagController;
import com.nhnacademy.minidooray3teamaccountapi.dto.TagRequestDTO;
import com.nhnacademy.minidooray3teamaccountapi.dto.TagResponseDTO;
import com.nhnacademy.minidooray3teamaccountapi.service.TagService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(SpringExtension.class)
public class TagControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TagService tagService;

    @InjectMocks
    private TagController tagController;

    private ObjectMapper objectMapper;

    private Long projectId;
    private Long tagId;
    private TagRequestDTO tagRequestDTO;
    private TagResponseDTO tagResponseDTO;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(tagController).build();
        objectMapper = new ObjectMapper();

        projectId = 1L;
        tagId = 1L;
        tagRequestDTO = new TagRequestDTO("Test Tag");
        tagResponseDTO = new TagResponseDTO(tagId, "Test Tag");
    }

    @Test
    public void testCreateTag_Success() throws Exception {
        when(tagService.createTag(eq(projectId), any(TagRequestDTO.class))).thenReturn(tagResponseDTO);

        mockMvc.perform(post("/projects/{projectId}/tags", projectId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tagRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tagId", is(tagId.intValue())))
                .andExpect(jsonPath("$.name", is("Test Tag")));

        verify(tagService, times(1)).createTag(eq(projectId), any(TagRequestDTO.class));
    }

    @Test
    public void testCreateTag_BadRequest() throws Exception {
        when(tagService.createTag(eq(projectId), any(TagRequestDTO.class))).thenReturn(null);

        mockMvc.perform(post("/projects/{projectId}/tags", projectId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tagRequestDTO)))
                .andExpect(status().isBadRequest());

        verify(tagService, times(1)).createTag(eq(projectId), any(TagRequestDTO.class));
    }

    @Test
    public void testDeleteTag_Success() throws Exception {
        when(tagService.deleteTag(projectId, tagId)).thenReturn(tagResponseDTO);

        mockMvc.perform(post("/projects/{projectId}/tags/{tagId}/delete", projectId, tagId))
                .andExpect(status().isOk());

        verify(tagService, times(1)).deleteTag(projectId, tagId);
    }

    @Test
    public void testDeleteTag_BadRequest() throws Exception {
        when(tagService.deleteTag(projectId, tagId)).thenReturn(null);

        mockMvc.perform(post("/projects/{projectId}/tags/{tagId}/delete", projectId, tagId))
                .andExpect(status().isBadRequest());

        verify(tagService, times(1)).deleteTag(projectId, tagId);
    }
}
