package com.nhnacademy.minidooray3teamaccountapi.milestone;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nhnacademy.minidooray3teamaccountapi.dto.MileStoneRequestDTO;
import com.nhnacademy.minidooray3teamaccountapi.dto.MileStoneResponseDTO;
import com.nhnacademy.minidooray3teamaccountapi.entity.MileStone;
import com.nhnacademy.minidooray3teamaccountapi.repository.MilestoneRepository;
import com.nhnacademy.minidooray3teamaccountapi.service.MileStoneService;
import java.util.Arrays;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class MileStoneServiceTest {
    @InjectMocks
    private MileStoneService mileStoneService;

    @Mock
    private MilestoneRepository milestoneRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testToEntity() {
        // Given
        MileStoneRequestDTO requestDTO = new MileStoneRequestDTO("Milestone 1", "START");

        // When
        MileStone mileStone = mileStoneService.toEntity(requestDTO);

        // Then
        Assertions.assertNotNull(mileStone);
        Assertions.assertEquals("Milestone 1", mileStone.getName());
        Assertions.assertEquals(MileStone.Status.START, mileStone.getStatus());
        Assertions.assertNotNull(mileStone.getCreatedAt());
    }

    @Test
    void testToResponseDTO() {
        // Given
        MileStone mileStone = new MileStone();
        mileStone.setMilestoneId(1L);
        mileStone.setName("Milestone 1");
        mileStone.setStatus(MileStone.Status.START);

        // When
        MileStoneResponseDTO responseDTO = mileStoneService.toResponseDTO(mileStone);

        // Then
        Assertions.assertNotNull(responseDTO);
        Assertions.assertEquals(1L, responseDTO.getId());
        Assertions.assertEquals("Milestone 1", responseDTO.getName());
        Assertions.assertEquals("START", responseDTO.getStatus());
    }

    @Test
    void testCreateMileStone() {
        // Given
        Long projectId = 1L;
        MileStoneRequestDTO requestDTO = new MileStoneRequestDTO("Milestone 1", "START");
        MileStone savedMileStone = new MileStone();
        savedMileStone.setMilestoneId(1L);
        savedMileStone.setName("Milestone 1");
        savedMileStone.setStatus(MileStone.Status.START);

        when(milestoneRepository.save(any(MileStone.class))).thenReturn(savedMileStone);

        // When
        MileStoneResponseDTO responseDTO = mileStoneService.createMileStone(projectId, requestDTO);

        // Then
        Assertions.assertNotNull(responseDTO);
        Assertions.assertEquals(1L, responseDTO.getId());
        Assertions.assertEquals("Milestone 1", responseDTO.getName());
        Assertions.assertEquals("START", responseDTO.getStatus());

        verify(milestoneRepository, times(1)).save(any(MileStone.class));
    }

    @Test
    void testCreateMileStone_NullEntity() {
        // Given
        Long projectId = 1L;
        MileStoneRequestDTO requestDTO = new MileStoneRequestDTO(null, null);

        // When & Then
        Assertions.assertThrows(NullPointerException.class, () -> {
            mileStoneService.createMileStone(projectId, requestDTO);
        });
    }


    @Test
    void testUpdateMileStone() {
        // Given
        Long projectId = 1L;
        Long milestoneId = 1L;
        MileStoneRequestDTO requestDTO = new MileStoneRequestDTO("Updated Milestone", "PROGRESS");

        MileStone existingMileStone = new MileStone();
        existingMileStone.setMilestoneId(milestoneId);
        existingMileStone.setName("Milestone 1");
        existingMileStone.setStatus(MileStone.Status.START);

        when(milestoneRepository.findById(milestoneId)).thenReturn(Optional.of(existingMileStone));
        when(milestoneRepository.save(any(MileStone.class))).thenReturn(existingMileStone);

        // When
        MileStoneResponseDTO responseDTO = mileStoneService.updateMileStone(projectId, milestoneId, requestDTO);

        // Then
        Assertions.assertNotNull(responseDTO);
        Assertions.assertEquals(milestoneId, responseDTO.getId());
        Assertions.assertEquals("Updated Milestone", responseDTO.getName());
        Assertions.assertEquals("PROGRESS", responseDTO.getStatus());

        verify(milestoneRepository, times(1)).findById(milestoneId);
        verify(milestoneRepository, times(1)).save(any(MileStone.class));
    }

    @Test
    void testUpdateMileStone_NotFound() {
        // Given
        Long projectId = 1L;
        Long milestoneId = 1L;
        MileStoneRequestDTO requestDTO = new MileStoneRequestDTO("Updated Milestone", "PROGRESS");

        when(milestoneRepository.findById(milestoneId)).thenReturn(Optional.empty());

        // When
        MileStoneResponseDTO responseDTO = mileStoneService.updateMileStone(projectId, milestoneId, requestDTO);

        // Then
        Assertions.assertNull(responseDTO);

        verify(milestoneRepository, times(1)).findById(milestoneId);
        verify(milestoneRepository, never()).save(any(MileStone.class));
    }

    @Test
    void testDeleteMileStone() {
        // Given
        Long projectId = 1L;
        Long milestoneId = 1L;

        MileStone existingMileStone = new MileStone();
        existingMileStone.setMilestoneId(milestoneId);
        existingMileStone.setName("Milestone 1");
        existingMileStone.setStatus(MileStone.Status.START);

        when(milestoneRepository.findById(milestoneId)).thenReturn(Optional.of(existingMileStone));

        // When
        MileStoneResponseDTO responseDTO = mileStoneService.deleteMileStone(projectId, milestoneId);

        // Then
        Assertions.assertNotNull(responseDTO);
        Assertions.assertEquals(milestoneId, responseDTO.getId());

        verify(milestoneRepository, times(1)).findById(milestoneId);
        verify(milestoneRepository, times(1)).delete(existingMileStone);
    }

    @Test
    void testDeleteMileStone_NotFound() {
        // Given
        Long projectId = 1L;
        Long milestoneId = 1L;

        when(milestoneRepository.findById(milestoneId)).thenReturn(Optional.empty());

        // When
        MileStoneResponseDTO responseDTO = mileStoneService.deleteMileStone(projectId, milestoneId);

        // Then
        Assertions.assertNull(responseDTO);

        verify(milestoneRepository, times(1)).findById(milestoneId);
        verify(milestoneRepository, never()).delete(any(MileStone.class));
    }

    @Test
    void testShow() {
        // Given
        MileStone mileStone1 = new MileStone();
        mileStone1.setMilestoneId(1L);
        mileStone1.setName("Milestone 1");
        mileStone1.setStatus(MileStone.Status.START);

        MileStone mileStone2 = new MileStone();
        mileStone2.setMilestoneId(2L);
        mileStone2.setName("Milestone 2");
        mileStone2.setStatus(MileStone.Status.PROGRESS);

        when(milestoneRepository.findAll()).thenReturn(Arrays.asList(mileStone1, mileStone2));

        // When
        Iterable<MileStoneResponseDTO> result = mileStoneService.show();

        // Then
        Assertions.assertNotNull(result);
        int count = 0;
        for (MileStoneResponseDTO dto : result) {
            Assertions.assertNotNull(dto);
            count++;
        }
        Assertions.assertEquals(2, count);

        verify(milestoneRepository, times(1)).findAll();
    }

    @Test
    void testIndex() {
        // Given
        Long milestoneId = 1L;
        MileStone mileStone = new MileStone();
        mileStone.setMilestoneId(milestoneId);
        mileStone.setName("Milestone 1");
        mileStone.setStatus(MileStone.Status.START);

        when(milestoneRepository.findById(milestoneId)).thenReturn(Optional.of(mileStone));

        // When
        MileStoneResponseDTO responseDTO = mileStoneService.index(milestoneId);

        // Then
        Assertions.assertNotNull(responseDTO);
        Assertions.assertEquals(milestoneId, responseDTO.getId());

        verify(milestoneRepository, times(1)).findById(milestoneId);
    }

    @Test
    void testIndex_NotFound() {
        // Given
        Long milestoneId = 1L;

        when(milestoneRepository.findById(milestoneId)).thenReturn(Optional.empty());

        // When & Then
        Assertions.assertThrows(NullPointerException.class, () -> {
            mileStoneService.index(milestoneId);
        });

        verify(milestoneRepository, times(1)).findById(milestoneId);
    }


}
