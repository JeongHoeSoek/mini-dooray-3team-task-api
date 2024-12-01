package com.nhnacademy.minidooray3teamaccountapi.milestone;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.minidooray3teamaccountapi.controller.MileStoneController;
import com.nhnacademy.minidooray3teamaccountapi.dto.MileStoneRequestDTO;
import com.nhnacademy.minidooray3teamaccountapi.dto.MileStoneResponseDTO;
import com.nhnacademy.minidooray3teamaccountapi.exception.GlobalExceptionHandler;
import com.nhnacademy.minidooray3teamaccountapi.exception.ResourceNotFoundException;
import com.nhnacademy.minidooray3teamaccountapi.service.MileStoneService;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@SpringBootTest
@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class MileStoneControllerTest {

    private MockMvc mockMvc;

    @Mock
    private MileStoneService mileStoneService;

    @InjectMocks
    private MileStoneController mileStoneController;

    private ObjectMapper objectMapper;

    private Long projectId;
    private MileStoneRequestDTO requestDTO;
    private MileStoneResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        // MockMvc 설정: StandaloneSetup을 사용하여 컨트롤러를 직접 설정
        mockMvc = MockMvcBuilders.standaloneSetup(mileStoneController)
                .setControllerAdvice(new GlobalExceptionHandler()) // 예외 핸들러 추가
                .build();

        objectMapper = new ObjectMapper();

        projectId = 1L;
        requestDTO = new MileStoneRequestDTO("Milestone 1", "START");
        responseDTO = new MileStoneResponseDTO(1L, "Milestone 1", "START");
    }

    @Test
    void testShow() throws Exception {
        // Given
        MileStoneResponseDTO dto1 = new MileStoneResponseDTO(1L, "Milestone 1", "START");
        MileStoneResponseDTO dto2 = new MileStoneResponseDTO(2L, "Milestone 2", "PROGRESS");

        when(mileStoneService.show()).thenReturn(Arrays.asList(dto1, dto2));

        // When & Then
        mockMvc.perform(get("/projects/{projectId}/milestones", projectId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].milestoneId", is(1)))
                .andExpect(jsonPath("$[0].name", is("Milestone 1")))
                .andExpect(jsonPath("$[0].status", is("START")))
                .andExpect(jsonPath("$[1].milestoneId", is(2)))
                .andExpect(jsonPath("$[1].name", is("Milestone 2")))
                .andExpect(jsonPath("$[1].status", is("PROGRESS")));

        verify(mileStoneService, times(1)).show();
    }

    @Test
    void testIndex_Success() throws Exception {
        // Given
        Long milestoneId = 1L;
        when(mileStoneService.index(milestoneId)).thenReturn(responseDTO);

        // When & Then
        mockMvc.perform(get("/projects/{projectId}/milestones/{milestoneId}", projectId, milestoneId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.milestoneId", is(1)))
                .andExpect(jsonPath("$.name", is("Milestone 1")))
                .andExpect(jsonPath("$.status", is("START")));

        verify(mileStoneService, times(1)).index(milestoneId);
    }

    @Test
    void testIndex_NotFound() throws Exception {
        // Given
        Long milestoneId = 1L;
        when(mileStoneService.index(milestoneId)).thenThrow(new ResourceNotFoundException("Milestone not found"));

        // When & Then
        mockMvc.perform(get("/projects/{projectId}/milestones/{milestoneId}", projectId, milestoneId))
                .andExpect(status().isNotFound());
        // .andExpect(jsonPath("$.title", is("Milestone not found")))
        // .andExpect(jsonPath("$.status", is(404)))
        // .andExpect(jsonPath("$.timestamp", notNullValue()));

        verify(mileStoneService, times(1)).index(milestoneId);
    }

    @Test
    void testCreateMileStone_Success() throws Exception {
        // Given
        when(mileStoneService.createMileStone(eq(projectId), any(MileStoneRequestDTO.class))).thenReturn(responseDTO);

        // When & Then
        mockMvc.perform(post("/projects/{projectId}/milestones", projectId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.milestoneId", is(1)))
                .andExpect(jsonPath("$.name", is("Milestone 1")))
                .andExpect(jsonPath("$.status", is("START")));

        verify(mileStoneService, times(1)).createMileStone(eq(projectId), any(MileStoneRequestDTO.class));
    }

    @Test
    void testCreateMileStone_BadRequest() throws Exception {
        // Given
        when(mileStoneService.createMileStone(eq(projectId), any(MileStoneRequestDTO.class))).thenReturn(null);

        // When & Then
        mockMvc.perform(post("/projects/{projectId}/milestones", projectId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest());

        verify(mileStoneService, times(1)).createMileStone(eq(projectId), any(MileStoneRequestDTO.class));
    }

    @Test
    void testUpdateMileStone_Success() throws Exception {
        // Given
        Long milestoneId = 1L;
        MileStoneRequestDTO updateRequestDTO = new MileStoneRequestDTO("Updated Milestone", "PROGRESS");
        MileStoneResponseDTO updatedResponseDTO = new MileStoneResponseDTO(1L, "Updated Milestone", "PROGRESS");

        when(mileStoneService.updateMileStone(eq(projectId), eq(milestoneId), any(MileStoneRequestDTO.class)))
                .thenReturn(updatedResponseDTO);

        // When & Then
        mockMvc.perform(post("/projects/{projectId}/milestones/{milestoneId}/update", projectId, milestoneId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.milestoneId", is(1)))
                .andExpect(jsonPath("$.name", is("Updated Milestone")))
                .andExpect(jsonPath("$.status", is("PROGRESS")));

        verify(mileStoneService, times(1))
                .updateMileStone(eq(projectId), eq(milestoneId), any(MileStoneRequestDTO.class));
    }

    @Test
    void testUpdateMileStone_BadRequest() throws Exception {
        // Given
        Long milestoneId = 1L;
        MileStoneRequestDTO updateRequestDTO = new MileStoneRequestDTO("Updated Milestone", "PROGRESS");

        when(mileStoneService.updateMileStone(eq(projectId), eq(milestoneId), any(MileStoneRequestDTO.class)))
                .thenReturn(null);

        // When & Then
        mockMvc.perform(post("/projects/{projectId}/milestones/{milestoneId}/update", projectId, milestoneId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequestDTO)))
                .andExpect(status().isBadRequest());

        verify(mileStoneService, times(1))
                .updateMileStone(eq(projectId), eq(milestoneId), any(MileStoneRequestDTO.class));
    }

    @Test
    void testDeleteMileStone_Success() throws Exception {
        // Given
        Long milestoneId = 1L;
        when(mileStoneService.deleteMileStone(projectId, milestoneId)).thenReturn(responseDTO);

        // When & Then
        mockMvc.perform(post("/projects/{projectId}/milestones/{milestoneId}/delete", projectId, milestoneId))
                .andExpect(status().isNoContent());

        verify(mileStoneService, times(1)).deleteMileStone(projectId, milestoneId);
    }

    @Test
    void testDeleteMileStone_BadRequest() throws Exception {
        // Given
        Long milestoneId = 1L;
        when(mileStoneService.deleteMileStone(projectId, milestoneId)).thenReturn(null);

        // When & Then
        mockMvc.perform(post("/projects/{projectId}/milestones/{milestoneId}/delete", projectId, milestoneId))
                .andExpect(status().isBadRequest());

        verify(mileStoneService, times(1)).deleteMileStone(projectId, milestoneId);
    }
}