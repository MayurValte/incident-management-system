package com.eimp.service;

import com.eimp.dto.MetricsDTO;
import com.eimp.entity.MetricsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface MetricsService {

    public List<MetricsDTO> getAllMetrics();
    Page<MetricsDTO> getMetricsPage(int page, int size);
    List<MetricsDTO> getMetricsSorted(String sortField, String direction);
    MetricsDTO saveMetrics(MetricsDTO metricsDTO);
}
