package com.nhnacademy.minidooray3teamaccountapi.controller;

import com.nhnacademy.minidooray3teamaccountapi.dto.TagRequestDTO;
import com.nhnacademy.minidooray3teamaccountapi.dto.TagResponseDTO;
import com.nhnacademy.minidooray3teamaccountapi.service.TagService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/projects/{projectId}/tags")
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    // Tag 생성
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TagResponseDTO> cratedTag(@PathVariable Long projectId,
                                                    @RequestBody TagRequestDTO tagRequestDTO) {

        TagResponseDTO created = tagService.createTag(projectId, tagRequestDTO);

        if (created == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(created);
    }

    // Tag 삭제 (`DELETE` → `POST`)
    @PostMapping("/{tagId}/delete")
    public ResponseEntity<Void> deleteTag(@PathVariable Long projectId, @PathVariable Long tagId) {
        TagResponseDTO deleted = tagService.deleteTag(projectId, tagId);

        if (deleted == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
