package com.eimp.dto;

import com.eimp.entity.AlertsEntity;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class TagsDTO {
    private Long id;
    private String name;
    private Set<AlertsEntity> alerts;
}
