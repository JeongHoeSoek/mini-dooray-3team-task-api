package com.nhnacademy.minidooray3teamaccountapi.controller;

import com.nhnacademy.minidooray3teamaccountapi.dto.MileStoneRequestDTO;
import com.nhnacademy.minidooray3teamaccountapi.dto.MileStoneResponseDTO;
import com.nhnacademy.minidooray3teamaccountapi.service.MileStoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/projects/{projectId}/milestones")
public class MileStoneController {

    @Autowired
    private MileStoneService mileStoneService;

    // 마일스톤 조회
    // 마일스톤 전체 조회
    @GetMapping
    public Iterable<MileStoneResponseDTO> show() {
        return mileStoneService.show();
    }

    // 마일스톤 아이디로 조회
    @GetMapping("/{milestoneId}")
    public MileStoneResponseDTO index(@PathVariable Long milestoneId) {
        return mileStoneService.index(milestoneId);
    }

    // 마일스톤 생성
    @PostMapping
    public ResponseEntity<MileStoneResponseDTO> createMileStone(@PathVariable Long projectId,
                                                                @RequestBody MileStoneRequestDTO mileStoneRequestDTO) {

        MileStoneResponseDTO created = mileStoneService.createMileStone(projectId, mileStoneRequestDTO);

        if (created == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(created);
    }

    // 마일스톤 수정 (`PATCH` → `POST`)
    @PostMapping("/{milestoneId}/update")
    public ResponseEntity<MileStoneResponseDTO> updateMileStone(@PathVariable Long projectId,
                                                                @PathVariable Long milestoneId,
                                                                @RequestBody MileStoneRequestDTO requestDTO) {
        MileStoneResponseDTO updated = mileStoneService.updateMileStone(projectId, milestoneId, requestDTO);

        if (updated == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }

    // 마일스톤 삭제 (`DELETE` → `POST`)
    @PostMapping("/{milestoneId}/delete")
    public ResponseEntity<Void> deleteMileStone(@PathVariable Long projectId,
                                                @PathVariable Long milestoneId) {
        MileStoneResponseDTO deleted = mileStoneService.deleteMileStone(projectId, milestoneId);

        if (deleted == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
