package com.eimp.controller;

import com.eimp.dto.MetricsDTO;
import com.eimp.service.MetricsService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/metrics")
@PreAuthorize("hasAnyRole('USER')")
public class MetricController {

    private final MetricsService metricsService;

    public MetricController(MetricsService metricsService) {
        this.metricsService = metricsService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<MetricsDTO>> getAllMetrics(){
        List<MetricsDTO> allMetrics = metricsService.getAllMetrics();
        return ResponseEntity.ok(allMetrics);
    }

    @GetMapping("/page")
    public ResponseEntity<Page<MetricsDTO>> getMetricsByPage(@RequestParam(name = "page", defaultValue = "0") int page,
                                                             @RequestParam(name = "size", defaultValue = "10") int size) {
        Page<MetricsDTO> metricsPage = metricsService.getMetricsPage(page, size);
        return ResponseEntity.ok(metricsPage);
    }

    @GetMapping("/sorted")
    public ResponseEntity<List<MetricsDTO>> getAllMetricsBySort(@RequestParam(name = "sort", defaultValue = "timestamp") String sort,
                                                                @RequestParam(name = "direction", defaultValue = "desc") String direction){
        List<MetricsDTO> metricsSorted = metricsService.getMetricsSorted(sort, direction);
        return ResponseEntity.ok(metricsSorted);

    }

//    @PostMapping("/saveMetrics")
//    @PreAuthorize("hasAnyRole('ADMIN','USER')")
//    public ResponseEntity<MetricsDTO> saveMetrics(@RequestBody MetricsDTO metricsDTO){
//        MetricsDTO savedMetrics = metricsService.saveMetrics(metricsDTO);
//        return ResponseEntity.ok(savedMetrics);
//    }
}
