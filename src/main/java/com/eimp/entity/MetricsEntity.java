package com.eimp.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "metrics")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MetricsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Foreign key to devices table
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id", nullable = false)
    private DevicesEntity device;

    @Column(name = "metric_type", length = 50, nullable = false)
    private String metricType;

    @Column(name = "metric_value", precision = 10, scale = 2, nullable = false)
    private BigDecimal metricValue;

    @Column(name = "threshold_value", precision = 10, scale = 2)
    private BigDecimal thresholdValue;

    @Column(name = "collected_at", nullable = false)
    @CreationTimestamp
    private Timestamp collectedAt;
}
