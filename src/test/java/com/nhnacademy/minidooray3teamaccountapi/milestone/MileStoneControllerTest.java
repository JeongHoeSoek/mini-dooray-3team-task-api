package com.nhnacademy.minidooray3teamaccountapi.milestone;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.minidooray3teamaccountapi.controller.MileStoneController;
import com.nhnacademy.minidooray3teamaccountapi.dto.MileStoneRequestDTO;
import com.nhnacademy.minidooray3teamaccountapi.dto.MileStoneResponseDTO;
import com.nhnacademy.minidooray3teamaccountapi.entity.MileStone;
import com.nhnacademy.minidooray3teamaccountapi.exception.GlobalExceptionHandler;
import com.nhnacademy.minidooray3teamaccountapi.exception.ResourceNotFoundException;
import com.nhnacademy.minidooray3teamaccountapi.service.MileStoneService;
import java.util.List;
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
class MileStoneControllerTest {

    private MockMvc mockMvc;

    @Mock
    private MileStoneService mileStoneService;

    @InjectMocks
    private MileStoneController mileStoneController;

    private ObjectMapper objectMapper;

    private Long projectId;
    private Long milestoneId;
    private MileStoneRequestDTO mileStoneRequestDTO;
    private MileStoneResponseDTO mileStoneResponseDTO;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();

        projectId = 1L;
        milestoneId = 1L;
        mileStoneRequestDTO = new MileStoneRequestDTO("Test Milestone", MileStone.Status.START);
        mileStoneResponseDTO = new MileStoneResponseDTO(milestoneId, "Test Milestone", "START");

        mockMvc = MockMvcBuilders.standaloneSetup(mileStoneController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void testShow() throws Exception {
        when(mileStoneService.show()).thenReturn(List.of(mileStoneResponseDTO));

        mockMvc.perform(get("/projects/{projectId}/milestones", projectId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].milestoneId", is(milestoneId.intValue())))
                .andExpect(jsonPath("$[0].name", is("Test Milestone")))
                .andExpect(jsonPath("$[0].status", is("START")));

        verify(mileStoneService, times(1)).show();
    }

    @Test
    void testIndex() throws Exception {
        when(mileStoneService.index(milestoneId)).thenReturn(mileStoneResponseDTO);

        mockMvc.perform(get("/projects/{projectId}/milestones/{milestoneId}", projectId, milestoneId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.milestoneId", is(milestoneId.intValue())))
                .andExpect(jsonPath("$.name", is("Test Milestone")))
                .andExpect(jsonPath("$.status", is("START")));

        verify(mileStoneService, times(1)).index(milestoneId);
    }

    @Test
    void testCreateMileStone_Success() throws Exception {
        when(mileStoneService.createMileStone(eq(projectId), any(MileStoneRequestDTO.class)))
                .thenReturn(mileStoneResponseDTO);

        mockMvc.perform(post("/projects/{projectId}/milestones", projectId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mileStoneRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.milestoneId", is(milestoneId.intValue())))
                .andExpect(jsonPath("$.name", is("Test Milestone")))
                .andExpect(jsonPath("$.status", is("START")));

        verify(mileStoneService, times(1)).createMileStone(eq(projectId), any(MileStoneRequestDTO.class));
    }

    @Test
    void testCreateMileStone_BadRequest() throws Exception {
        when(mileStoneService.createMileStone(eq(projectId), any(MileStoneRequestDTO.class))).thenReturn(null);

        mockMvc.perform(post("/projects/{projectId}/milestones", projectId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mileStoneRequestDTO)))
                .andExpect(status().isBadRequest());

        verify(mileStoneService, times(1)).createMileStone(eq(projectId), any(MileStoneRequestDTO.class));
    }

    @Test
    void testCreateMileStone_ProjectNotFound() throws Exception {
        when(mileStoneService.createMileStone(eq(projectId), any(MileStoneRequestDTO.class)))
                .thenThrow(new ResourceNotFoundException("프로젝트를 찾을 수 없습니다."));

        mockMvc.perform(post("/projects/{projectId}/milestones", projectId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mileStoneRequestDTO)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("프로젝트를 찾을 수 없습니다."));

        verify(mileStoneService, times(1)).createMileStone(eq(projectId), any(MileStoneRequestDTO.class));
    }

    @Test
    void testUpdateMileStone_Success() throws Exception {
        when(mileStoneService.updateMileStone(eq(projectId), eq(milestoneId),
                any(MileStoneRequestDTO.class))).thenReturn(mileStoneResponseDTO);

        mockMvc.perform(post("/projects/{projectId}/milestones/{milestoneId}/update", projectId, milestoneId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mileStoneRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.milestoneId", is(milestoneId.intValue())))
                .andExpect(jsonPath("$.name", is("Test Milestone")))
                .andExpect(jsonPath("$.status", is("START")));

        verify(mileStoneService, times(1)).updateMileStone(eq(projectId), eq(milestoneId),
                any(MileStoneRequestDTO.class));
    }

    @Test
    void testUpdateMileStone_BadRequest() throws Exception {
        when(mileStoneService.updateMileStone(eq(projectId), eq(milestoneId),
                any(MileStoneRequestDTO.class))).thenReturn(null);

        mockMvc.perform(post("/projects/{projectId}/milestones/{milestoneId}/update", projectId, milestoneId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mileStoneRequestDTO)))
                .andExpect(status().isBadRequest());

        verify(mileStoneService, times(1)).updateMileStone(eq(projectId), eq(milestoneId),
                any(MileStoneRequestDTO.class));
    }

    @Test
    void testUpdateMileStone_ProjectNotFound() throws Exception {
        when(mileStoneService.updateMileStone(eq(projectId), eq(milestoneId), any(MileStoneRequestDTO.class)))
                .thenThrow(new ResourceNotFoundException("프로젝트를 찾을 수 없습니다."));

        mockMvc.perform(post("/projects/{projectId}/milestones/{milestoneId}/update", projectId, milestoneId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mileStoneRequestDTO)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("프로젝트를 찾을 수 없습니다."));

        verify(mileStoneService, times(1)).updateMileStone(eq(projectId), eq(milestoneId),
                any(MileStoneRequestDTO.class));
    }

    @Test
    void testDeleteMileStone_Success() throws Exception {
        when(mileStoneService.deleteMileStone(projectId, milestoneId)).thenReturn(mileStoneResponseDTO);

        mockMvc.perform(post("/projects/{projectId}/milestones/{milestoneId}/delete", projectId, milestoneId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(mileStoneService, times(1)).deleteMileStone(projectId, milestoneId);
    }

    @Test
    void testDeleteMileStone_BadRequest() throws Exception {
        when(mileStoneService.deleteMileStone(projectId, milestoneId)).thenReturn(null);

        mockMvc.perform(post("/projects/{projectId}/milestones/{milestoneId}/delete", projectId, milestoneId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(mileStoneService, times(1)).deleteMileStone(projectId, milestoneId);
    }

    @Test
    void testDeleteMileStone_ProjectNotFound() throws Exception {
        when(mileStoneService.deleteMileStone(projectId, milestoneId))
                .thenThrow(new ResourceNotFoundException("프로젝트를 찾을 수 없습니다."));

        mockMvc.perform(post("/projects/{projectId}/milestones/{milestoneId}/delete", projectId, milestoneId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("프로젝트를 찾을 수 없습니다."));

        verify(mileStoneService, times(1)).deleteMileStone(projectId, milestoneId);
    }
}
