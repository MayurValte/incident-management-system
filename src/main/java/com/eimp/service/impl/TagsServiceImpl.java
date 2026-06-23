package com.eimp.service.impl;

import com.eimp.dto.TagsDTO;
import com.eimp.entity.TagsEntity;
import com.eimp.repository.TagsRepository;
import com.eimp.service.TagsService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class TagsServiceImpl implements TagsService {

    private final TagsRepository tagsRepository;
    private final ModelMapper modelMapper;

    public TagsServiceImpl(TagsRepository tagsRepository,
                           ModelMapper modelMapper) {
        this.tagsRepository = tagsRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public TagsDTO createTag(TagsDTO tagsDTO) {

        TagsEntity entity = modelMapper.map(tagsDTO, TagsEntity.class);

        TagsEntity savedEntity = tagsRepository.save(entity);

        return modelMapper.map(savedEntity, TagsDTO.class);
    }
}
