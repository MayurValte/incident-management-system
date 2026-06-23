package com.eimp.controller;

import com.eimp.dto.AlertsDTO;
import com.eimp.enums.AlertStatus;
import com.eimp.enums.Severity;
import com.eimp.service.AlertsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/alerts")
@PreAuthorize("hasAnyRole('ADMIN','USER')")
public class AlertController {

    private final AlertsService alertsService;

    public AlertController(AlertsService alertsService) {
        this.alertsService = alertsService;
    }

    @PatchMapping("/{alertId}/assign")
    public ResponseEntity<AlertsDTO> assignAlert(
            @PathVariable Long alertId,
            @RequestParam Long userId) {

        AlertsDTO updated = alertsService.assignAlert(alertId, userId);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{alertId}/resolve")
    public ResponseEntity<AlertsDTO> resolveAlert(@PathVariable Long alertId) {
        AlertsDTO updated = alertsService.resolveAlert(alertId);
        return ResponseEntity.ok(updated);
    }

        @PatchMapping("/{alertId}/close")
    public ResponseEntity<AlertsDTO> closeAlert(@PathVariable Long alertId) {
        AlertsDTO updated = alertsService.closeAlert(alertId);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<AlertsDTO>> getAllAlerts(){
        List<AlertsDTO> allAlerts = alertsService.getAllAlerts();
        return ResponseEntity.ok(allAlerts);
    }

    @PostMapping("/generateAlert")
    public ResponseEntity<AlertsDTO> submitAlert(@RequestBody AlertsDTO alertsDTO){
        AlertsDTO savedAlert = alertsService.generateAlert(alertsDTO);
        return ResponseEntity.ok(savedAlert);
    }

    @GetMapping("/byStatus")
    public ResponseEntity<List<AlertsDTO>> getAlertByStatus(@RequestParam(name = "status") AlertStatus status){
        List<AlertsDTO> alertByStatus = alertsService.getAlertByStatus(status);
        return ResponseEntity.ok(alertByStatus);

    }

    @GetMapping("/bySeverity")
    public ResponseEntity<List<AlertsDTO>> getAlertBySeverity(@RequestParam(name = "severity") Severity severity){
        List<AlertsDTO> alertBySeverity = alertsService.getAlertBySeverity(severity);
        return ResponseEntity.ok(alertBySeverity);
    }




}
