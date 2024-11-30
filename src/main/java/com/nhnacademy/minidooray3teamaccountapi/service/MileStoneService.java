package com.nhnacademy.minidooray3teamaccountapi.service;

import com.nhnacademy.minidooray3teamaccountapi.dto.MileStoneRequestDTO;
import com.nhnacademy.minidooray3teamaccountapi.dto.MileStoneResponseDTO;
import com.nhnacademy.minidooray3teamaccountapi.entity.MileStone;
import com.nhnacademy.minidooray3teamaccountapi.repository.MilestoneRepository;
import com.nhnacademy.minidooray3teamaccountapi.repository.ProjectRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MileStoneService {
    @Autowired
    private MilestoneRepository milestoneRepository;

    @Autowired
    private ProjectRepository projectRepository;

    // DTO -> Entity 변환
    public MileStone toEntity(MileStoneRequestDTO requestDTO) {
        MileStone mileStone = new MileStone();

        // DTO를 Entity로 변환하는 작업
        mileStone.setName(requestDTO.getName());
        mileStone.setStatus(MileStone.Status.valueOf(requestDTO.getStatus()));
        mileStone.setCreatedAt(LocalDateTime.now());

        // Entity 반환
        return mileStone;
    }

    // Entity -> DTO 변환
    public MileStoneResponseDTO toResponseDTO(MileStone mileStone) {
        MileStoneResponseDTO responseDTO = new MileStoneResponseDTO();

        responseDTO.setId(mileStone.getMilestoneId());
        responseDTO.setName(mileStone.getName());
        responseDTO.setStatus(mileStone.getStatus().name());

        return responseDTO;
    }

    // MileStone 생성
    public MileStoneResponseDTO createMileStone(Long projectId, MileStoneRequestDTO requestDTO) {
        // 프로젝트 조회 (필요 시)
        // Project project = projectRepository.findById(projectId)
        //         .orElseThrow(() -> new ResourceNotFoundException("프로젝트를 찾을 수 없습니다."));

        // DTO -> Entity로 변환
        MileStone entity = toEntity(requestDTO);

        if (entity == null) {
            log.info("요청 값이 null 이거나 요청 프로젝트 아이디와 entity 아이디가 다릅니다.");
            return null;
        }

        // 엔티티 저장
        MileStone created = milestoneRepository.save(entity);

        return toResponseDTO(created);
    }

    // MileStone 업데이트
    public MileStoneResponseDTO updateMileStone(Long projectId, Long milestoneId, MileStoneRequestDTO requestDTO) {
        // 프로젝트 조회 (필요 시)
        // Project project = projectRepository.findById(projectId)
        //         .orElseThrow(() -> new ResourceNotFoundException("프로젝트를 찾을 수 없습니다."));

        // DTO -> Entity로 변환
        MileStone entity = toEntity(requestDTO);

        // MileStone 조회
        MileStone target = milestoneRepository.findById(milestoneId).orElse(null);

        if (target == null || target.getMilestoneId() != milestoneId) {
            return null;
        }

        // 엔티티 저장
        target.patch(entity);
        MileStone updated = milestoneRepository.save(target);

        return toResponseDTO(updated);
    }

    public MileStoneResponseDTO deleteMileStone(Long projectId, Long milestoneId) {
        // 프로젝트 조회 (필요 시)
        // Project project = projectRepository.findById(projectId)
        //         .orElseThrow(() -> new ResourceNotFoundException("프로젝트를 찾을 수 없습니다."));

        // MileStone 조회
        MileStone target = milestoneRepository.findById(milestoneId).orElse(null);

        if (target == null) {
            return null;
        }

        // 삭제
        milestoneRepository.delete(target);

        return toResponseDTO(target);
    }

    // 전체 조회
    public Iterable<MileStoneResponseDTO> show() {
        // 마일 스톤 조회해서 담기
        Iterable<MileStone> mileStones = milestoneRepository.findAll();

        // 마일스톤 DTO로 변환
        List<MileStoneResponseDTO> dtoList = new ArrayList<>();
        for (MileStone mileStone : mileStones) {
            dtoList.add(toResponseDTO(mileStone));
        }

        return dtoList;
    }

    public MileStoneResponseDTO index(Long milestoneId) {
        return toResponseDTO(milestoneRepository.findById(milestoneId).orElse(null));
    }
}
