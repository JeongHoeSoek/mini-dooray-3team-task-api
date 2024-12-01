package com.nhnacademy.minidooray3teamaccountapi.milestone;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nhnacademy.minidooray3teamaccountapi.dto.MileStoneRequestDTO;
import com.nhnacademy.minidooray3teamaccountapi.dto.MileStoneResponseDTO;
import com.nhnacademy.minidooray3teamaccountapi.entity.MileStone;
import com.nhnacademy.minidooray3teamaccountapi.entity.Project;
import com.nhnacademy.minidooray3teamaccountapi.exception.ResourceNotFoundException;
import com.nhnacademy.minidooray3teamaccountapi.repository.MilestoneRepository;
import com.nhnacademy.minidooray3teamaccountapi.repository.ProjectRepository;
import com.nhnacademy.minidooray3teamaccountapi.service.MileStoneService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MileStoneServiceTest {

    @Mock
    private MilestoneRepository milestoneRepository;

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private MileStoneService mileStoneService;

    private Long projectId;
    private Long milestoneId;
    private MileStoneRequestDTO mileStoneRequestDTO;
    private MileStone mileStone;
    private Project project;

    @BeforeEach
    public void setUp() {
        projectId = 1L;
        milestoneId = 1L;
        mileStoneRequestDTO = new MileStoneRequestDTO("Test Milestone", MileStone.Status.START);
        mileStone = new MileStone();
        mileStone.setMilestoneId(milestoneId);
        mileStone.setName("Test Milestone");
        mileStone.setStatus(MileStone.Status.START);

        project = new Project();
        project.setProjectId(projectId);
        project.setName("Test Project");
        project.setStatus(Project.Status.ACTIVE);
    }

    @Test
    void testCreateMileStone_Success() {
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(milestoneRepository.save(any(MileStone.class))).thenReturn(mileStone);

        MileStoneResponseDTO responseDTO = mileStoneService.createMileStone(projectId, mileStoneRequestDTO);

        assertNotNull(responseDTO);
        assertEquals(milestoneId, responseDTO.getId());
        assertEquals("Test Milestone", responseDTO.getName());
        assertEquals("START", responseDTO.getStatus());

        verify(projectRepository, times(1)).findById(projectId);
        verify(milestoneRepository, times(1)).save(any(MileStone.class));
    }

    @Test
    void testCreateMileStone_ProjectNotFound() {
        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            mileStoneService.createMileStone(projectId, mileStoneRequestDTO);
        });

        verify(projectRepository, times(1)).findById(projectId);
        verify(milestoneRepository, never()).save(any(MileStone.class));
    }

    @Test
    void testUpdateMileStone_Success() {
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(milestoneRepository.findById(milestoneId)).thenReturn(Optional.of(mileStone));
        when(milestoneRepository.save(any(MileStone.class))).thenReturn(mileStone);

        MileStoneResponseDTO responseDTO =
                mileStoneService.updateMileStone(projectId, milestoneId, mileStoneRequestDTO);

        assertNotNull(responseDTO);
        assertEquals(milestoneId, responseDTO.getId());
        assertEquals("Test Milestone", responseDTO.getName());
        assertEquals("START", responseDTO.getStatus());

        verify(projectRepository, times(1)).findById(projectId);
        verify(milestoneRepository, times(1)).findById(milestoneId);
        verify(milestoneRepository, times(1)).save(any(MileStone.class));
    }

    @Test
    void testUpdateMileStone_ProjectNotFound() {
        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            mileStoneService.updateMileStone(projectId, milestoneId, mileStoneRequestDTO);
        });

        verify(projectRepository, times(1)).findById(projectId);
        verify(milestoneRepository, never()).findById(anyLong());
        verify(milestoneRepository, never()).save(any(MileStone.class));
    }

    @Test
    void testUpdateMileStone_MileStoneNotFound() {
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(milestoneRepository.findById(milestoneId)).thenReturn(Optional.empty());

        MileStoneResponseDTO responseDTO =
                mileStoneService.updateMileStone(projectId, milestoneId, mileStoneRequestDTO);

        assertNull(responseDTO);

        verify(projectRepository, times(1)).findById(projectId);
        verify(milestoneRepository, times(1)).findById(milestoneId);
        verify(milestoneRepository, never()).save(any(MileStone.class));
    }

    @Test
    void testDeleteMileStone_Success() {
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(milestoneRepository.findById(milestoneId)).thenReturn(Optional.of(mileStone));

        MileStoneResponseDTO responseDTO = mileStoneService.deleteMileStone(projectId, milestoneId);

        assertNotNull(responseDTO);
        assertEquals(milestoneId, responseDTO.getId());
        assertEquals("Test Milestone", responseDTO.getName());

        verify(projectRepository, times(1)).findById(projectId);
        verify(milestoneRepository, times(1)).findById(milestoneId);
        verify(milestoneRepository, times(1)).delete(mileStone);
    }

    @Test
    void testDeleteMileStone_ProjectNotFound() {
        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            mileStoneService.deleteMileStone(projectId, milestoneId);
        });

        verify(projectRepository, times(1)).findById(projectId);
        verify(milestoneRepository, never()).findById(anyLong());
        verify(milestoneRepository, never()).delete(any(MileStone.class));
    }

    @Test
    void testDeleteMileStone_MileStoneNotFound() {
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(milestoneRepository.findById(milestoneId)).thenReturn(Optional.empty());

        MileStoneResponseDTO responseDTO = mileStoneService.deleteMileStone(projectId, milestoneId);

        assertNull(responseDTO);

        verify(projectRepository, times(1)).findById(projectId);
        verify(milestoneRepository, times(1)).findById(milestoneId);
        verify(milestoneRepository, never()).delete(any(MileStone.class));
    }

    @Test
    void testShow() {
        List<MileStone> mileStones = new ArrayList<>();
        mileStones.add(mileStone);

        when(milestoneRepository.findAll()).thenReturn(mileStones);

        Iterable<MileStoneResponseDTO> responseDTOs = mileStoneService.show();

        assertNotNull(responseDTOs);
        List<MileStoneResponseDTO> dtoList = new ArrayList<>();
        responseDTOs.forEach(dtoList::add);

        assertEquals(1, dtoList.size());
        assertEquals(milestoneId, dtoList.get(0).getId());
        assertEquals("Test Milestone", dtoList.get(0).getName());
        assertEquals("START", dtoList.get(0).getStatus());

        verify(milestoneRepository, times(1)).findAll();
    }

    @Test
    void testIndex() {
        when(milestoneRepository.findById(milestoneId)).thenReturn(Optional.of(mileStone));

        MileStoneResponseDTO responseDTO = mileStoneService.index(milestoneId);

        assertNotNull(responseDTO);
        assertEquals(milestoneId, responseDTO.getId());
        assertEquals("Test Milestone", responseDTO.getName());
        assertEquals("START", responseDTO.getStatus());

        verify(milestoneRepository, times(1)).findById(milestoneId);
    }
}