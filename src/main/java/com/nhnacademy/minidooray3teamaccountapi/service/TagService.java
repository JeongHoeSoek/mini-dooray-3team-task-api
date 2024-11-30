package com.nhnacademy.minidooray3teamaccountapi.service;

import com.nhnacademy.minidooray3teamaccountapi.dto.TagRequestDTO;
import com.nhnacademy.minidooray3teamaccountapi.dto.TagResponseDTO;
import com.nhnacademy.minidooray3teamaccountapi.entity.Tag;
import com.nhnacademy.minidooray3teamaccountapi.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TagService {
    @Autowired
    private TagRepository tagRepository;

    // DTO -> Entity 변환
    public Tag toEntity(TagRequestDTO requestDTO) {
        Tag tag = new Tag();

        // DTO를 Entity로 변환하는 작업
        tag.setName(requestDTO.getName());

        // Entity 반환
        return tag;
    }

    // Entity -> DTO 변환
    public TagResponseDTO toResponseDTO(Tag tag) {
        TagResponseDTO tagResponseDTO = new TagResponseDTO();

        tagResponseDTO.setTagId(tag.getTagId());
        tagResponseDTO.setName(tag.getName());

        return tagResponseDTO;
    }

    public TagResponseDTO createTag(Long projectId, TagRequestDTO tagRequestDTO) {
//        Project project = projectRepository.findById(projectId)
//                .orElseThrow(() -> new ResourceNotFoundException("프로젝트를 찾을 수 없습니다."));

        // DTO -> Entity
        Tag entity = toEntity(tagRequestDTO);

        if (entity == null) {
            return null;
        }

        Tag created = tagRepository.save(entity);

        return toResponseDTO(created);
    }

    public TagResponseDTO deleteTag(Long projectId, Long tagId) {
        //        Project project = projectRepository.findById(projectId)
//                .orElseThrow(() -> new ResourceNotFoundException("프로젝트를 찾을 수 없습니다."));

        // Tag 조회
        Tag target = tagRepository.findById(tagId).orElse(null);

        if (target == null) {
            return null;
        }

        tagRepository.delete(target);

        return toResponseDTO(target);
    }
}
