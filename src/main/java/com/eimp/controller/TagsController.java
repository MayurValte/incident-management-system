package com.eimp.controller;

import com.eimp.dto.TagsDTO;
import com.eimp.service.TagsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tags")
@PreAuthorize("hasRole('ADMIN')")
public class TagsController {

    private final TagsService tagsService;

    public TagsController(TagsService tagsService) {
        this.tagsService = tagsService;
    }

    @PostMapping
    public ResponseEntity<TagsDTO> createTag(
            @RequestBody TagsDTO tagsDTO) {

        TagsDTO savedTag = tagsService.createTag(tagsDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedTag);
    }
}