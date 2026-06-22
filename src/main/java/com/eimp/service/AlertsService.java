package com.eimp.service;

import com.eimp.dto.AlertsDTO;
import com.eimp.entity.AlertsEntity;
import com.eimp.enums.AlertStatus;
import com.eimp.enums.Severity;

import java.util.List;

public interface AlertsService {

    List<AlertsDTO> getAllAlerts();

    AlertsDTO assignAlert(Long alertId, Long userId);

    AlertsDTO resolveAlert(Long alertId);

    AlertsDTO generateAlert(AlertsDTO alertsDTO);

    List<AlertsDTO> getAlertByStatus(AlertStatus status);

    List<AlertsDTO> getAlertBySeverity(Severity severity);

    AlertsDTO closeAlert(Long alertId);

}
