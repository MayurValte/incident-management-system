package com.eimp.dto;

import com.eimp.enums.AlertStatus;
import com.eimp.enums.Severity;
import lombok.Data;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@Data
public class AlertsDTO {
    private Long id;
    private String title;
    private String description;
    private Severity severity;
    private AlertStatus status;
    private Long assignedTo;
    private Long deviceId;
    private Timestamp createdAt;
    private Timestamp resolvedAt;
    private Set<Long> tagIds;

    public String getCreatedAt() {
        if (createdAt == null) {
            return null;
        }

        return createdAt.toLocalDateTime()
                .format(
                        DateTimeFormatter.ofPattern(
                                "dd MMM yyyy, hh:mm:ss a"));
    }
}
