package com.eimp.service.impl;

import com.eimp.dto.MetricsDTO;
import com.eimp.entity.MetricsEntity;
import com.eimp.repository.MetricsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MetricsImplTest {

    @Mock
    private MetricsRepository repository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private MetricsImpl metricsService;

    @Test
    void shouldReturnAllMetrics() {

        MetricsEntity entity = new MetricsEntity();
        MetricsDTO dto = new MetricsDTO();

        when(repository.findAll())
                .thenReturn(List.of(entity));

        when(modelMapper.map(entity, MetricsDTO.class))
                .thenReturn(dto);

        List<MetricsDTO> result =
                metricsService.getAllMetrics();

        assertEquals(1, result.size());

        verify(repository).findAll();
    }

    @Test
    void shouldReturnMetricsPage() {

        MetricsEntity entity = new MetricsEntity();
        MetricsDTO dto = new MetricsDTO();

        Page<MetricsEntity> entityPage =
                new PageImpl<>(List.of(entity));

        when(repository.findAllMetrics(any(Pageable.class)))
                .thenReturn(entityPage);

        when(modelMapper.map(entity, MetricsDTO.class))
                .thenReturn(dto);

        Page<MetricsDTO> result =
                metricsService.getMetricsPage(0, 10);

        assertEquals(1, result.getContent().size());

        verify(repository)
                .findAllMetrics(any(Pageable.class));
    }

    @Test
    void shouldReturnSortedMetrics() {

        MetricsEntity entity = new MetricsEntity();
        MetricsDTO dto = new MetricsDTO();

        when(repository.findAll(any(Sort.class)))
                .thenReturn(List.of(entity));

        when(modelMapper.map(entity, MetricsDTO.class))
                .thenReturn(dto);

        List<MetricsDTO> result =
                metricsService.getMetricsSorted(
                        "metricValue",
                        "asc");

        assertEquals(1, result.size());

        verify(repository)
                .findAll(any(Sort.class));
    }

//    @Test
//    void shouldSaveMetricsWithoutGeneratingAlert() {
//
//        MetricsDTO dto = new MetricsDTO();
//        dto.setMetricValue(BigDecimal.valueOf(50.0));
//        dto.setThresholdValue(BigDecimal.valueOf(80.0));
//
//        MetricsEntity entity = new MetricsEntity();
//
//        when(modelMapper.map(dto, MetricsEntity.class))
//                .thenReturn(entity);
//
//        when(repository.save(entity))
//                .thenReturn(entity);
//
//        when(modelMapper.map(entity, MetricsDTO.class))
//                .thenReturn(dto);
//
//        MetricsDTO result =
//                metricsService.saveMetrics(dto);
//
//        assertNotNull(result);
//
//        verify(repository).save(entity);
//    }

}