package com.eimp.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class MetricsDTO {
    private Long id;
    private Long deviceId;       // reference to device
    private String metricType;
    private BigDecimal metricValue;
    private BigDecimal thresholdValue;
    private Timestamp collectedAt;
}
