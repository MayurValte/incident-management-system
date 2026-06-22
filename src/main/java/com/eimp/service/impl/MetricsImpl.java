package com.eimp.service.impl;

import com.eimp.dto.AlertsDTO;
import com.eimp.dto.MetricsDTO;
import com.eimp.entity.AlertsEntity;
import com.eimp.entity.MetricsEntity;
import com.eimp.enums.AlertStatus;
import com.eimp.enums.Severity;
import com.eimp.repository.AlertsRepository;
import com.eimp.repository.MetricsRepository;
import com.eimp.service.MetricsService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MetricsImpl implements MetricsService {
    private final MetricsRepository repository;
    private final ModelMapper modelMapper;
    private final AlertsRepository alertsRepository;

    public MetricsImpl(MetricsRepository repository, ModelMapper modelMapper, AlertsRepository alertsRepository) {
        this.repository = repository;
        this.modelMapper = modelMapper;
        this.alertsRepository = alertsRepository;
    }

    @Override
    public List<MetricsDTO> getAllMetrics() {
        List<MetricsEntity> allMetrics = repository.findAll();

        return allMetrics
                .stream()
                .map(allMetric -> modelMapper.map(allMetric, MetricsDTO.class))
                .collect(Collectors.toList());

    }

    @Override
    public Page<MetricsDTO> getMetricsPage(int page, int size) {

        Pageable pageable= PageRequest.of(page,size);
        Page<MetricsEntity> allMetrics = repository.findAllMetrics(pageable);

        return allMetrics.map(entity -> modelMapper.map(entity, MetricsDTO.class));
    }

    @Override
    public List<MetricsDTO> getMetricsSorted(String sortField, String direction) {
        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;
        List<MetricsEntity> allBySort = repository.findAll(Sort.by(sortDirection, sortField));
        return allBySort.stream().map(metricsEntity -> modelMapper.map(metricsEntity,MetricsDTO.class)).toList();
    }

    @Override
    public MetricsDTO saveMetrics(MetricsDTO metricsDTO) {
//        MetricsEntity metricsEntity = modelMapper.map(metricsDTO, MetricsEntity.class);
//        MetricsEntity savedMetricsEntity = repository.save(metricsEntity);
////        if(metricsDTO.getMetricValue().compareTo(metricsDTO.getThresholdValue())>0){
////            generateAlert(metricsDTO);
////        }
//        return modelMapper.map(savedMetricsEntity, MetricsDTO.class);
        return null;
    }


}
