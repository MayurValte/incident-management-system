package com.eimp.service.impl;

import com.eimp.dto.AlertsDTO;
import com.eimp.entity.AlertsEntity;
import com.eimp.entity.DevicesEntity;
import com.eimp.entity.TagsEntity;
import com.eimp.entity.UsersEntity;
import com.eimp.enums.AlertStatus;
import com.eimp.enums.Severity;
import com.eimp.exception.AlertStatusException;
import com.eimp.exception.ResourceNotFoundException;
import com.eimp.repository.AlertsRepository;
import com.eimp.repository.DeviceRepository;
import com.eimp.repository.TagRepository;
import com.eimp.repository.UserRepository;
import com.eimp.service.AlertsService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AlertsImpl implements AlertsService {

    private static final Logger log =
            LoggerFactory.getLogger(AlertsImpl.class);

    private final AlertsRepository alertsRepository;
    private final UserRepository userRepository;
    private final DeviceRepository deviceRepository;
    private final TagRepository tagsRepository;


    public AlertsImpl(AlertsRepository alertsRepository, ModelMapper modelMapper, UserRepository userRepository, DeviceRepository deviceRepository, TagRepository tagRepository) {
        this.alertsRepository = alertsRepository;
        this.userRepository = userRepository;
        this.deviceRepository = deviceRepository;
        this.tagsRepository = tagRepository;
    }

    @Override
    public List<AlertsDTO> getAllAlerts() {
        String fncName = "getAllAlerts";
        log.info("{} Getting All Alerts : ", fncName);
        List<AlertsEntity> allAlerts =
                alertsRepository.findAll(
                        Sort.by(Sort.Direction.DESC, "severity"));

        return allAlerts.stream()
                .map(this::convertToDto)
                .toList();
    }

    @Override
    public AlertsDTO assignAlert(Long alertId, Long userId) {
        log.info("Assigning Alerts {} to user {}",alertId,userId);
        AlertsEntity alertsEntity = alertsRepository.findById(alertId).orElseThrow(() -> new ResourceNotFoundException("Alert not found with id: " + alertId));
        UsersEntity user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User with id " + userId + " Not present"));

        alertsEntity.setAssignedTo(user);
        AlertsEntity savedAlertEntity = alertsRepository.save(alertsEntity);
        log.info("Alert Assigned");
        return convertToDto(savedAlertEntity);
    }

    @Override
    public AlertsDTO resolveAlert(Long alertId) {
        log.info("Resolving Alert with id {}",alertId);
        AlertsEntity alert = alertsRepository.findById(alertId)
                .orElseThrow(() -> new ResourceNotFoundException("Alert not found with id: " + alertId));

        if (alert.getStatus().equals(AlertStatus.RESOLVED)) {
            log.info("Alert with id {} Already RESOLVED",alert);
            throw new AlertStatusException("Alert with id " + alertId + " Already RESOLVED");
        }

        alert.setStatus(AlertStatus.RESOLVED);
        AlertsEntity saved = alertsRepository.save(alert);
        log.info("Alert RESOLVED");

        return convertToDto(saved);
    }

    @Override
    public AlertsDTO generateAlert(AlertsDTO alertsDTO) {
        log.info("Generating new Alert for {}",alertsDTO.getTitle());

        UsersEntity user = userRepository.findById(alertsDTO.getAssignedTo())
                .orElseThrow(() -> new ResourceNotFoundException("User not present with ID : " + alertsDTO.getAssignedTo()));

        DevicesEntity device = deviceRepository.findById(alertsDTO.getDeviceId())
                .orElseThrow(() -> new ResourceNotFoundException("Device not present with id : " + alertsDTO.getDeviceId()));

        AlertsEntity alertsEntity = new AlertsEntity();
        alertsEntity.setTitle(alertsDTO.getTitle());
        alertsEntity.setDescription(alertsDTO.getDescription());
        alertsEntity.setSeverity(alertsDTO.getSeverity());
        alertsEntity.setStatus(alertsDTO.getStatus());
        alertsEntity.setAssignedTo(user);

        alertsEntity.setDevice(device);

        if (alertsDTO.getTagIds() != null && !alertsDTO.getTagIds().isEmpty()) {
            log.info("Adding Tags to Alert");
            Set<TagsEntity> tags = alertsDTO.getTagIds().stream()
                    .map(tagId -> tagsRepository.findById(tagId)
                            .orElseThrow(() -> new ResourceNotFoundException("Tag not found with id: " + tagId)))
                    .collect(Collectors.toSet());
            alertsEntity.setTags(tags);
        }

        AlertsEntity savedEntity = alertsRepository.save(alertsEntity);
        log.info("Alert is created with id {}",savedEntity.getId());
        return convertToDto(savedEntity);
    }

    @Override
    public List<AlertsDTO> getAlertByStatus(AlertStatus status) {
        List<AlertsEntity> alertsByStatus = alertsRepository.findByStatus(status);
        return alertsByStatus.stream().map(this::convertToDto).toList();
    }

    @Override
    public List<AlertsDTO> getAlertBySeverity(Severity severity) {
        List<AlertsEntity> bySeverity = alertsRepository.findBySeverity(severity);
        return bySeverity.stream().map(this::convertToDto).toList();
    }

    @Override
    public AlertsDTO closeAlert(Long alertId) {
        log.info("Closing Alert with id {}",alertId);
        AlertsEntity alert = alertsRepository.findById(alertId)
                .orElseThrow(() -> new ResourceNotFoundException("Alert not found with id: " + alertId));

        if (alert.getStatus() == AlertStatus.CLOSED) {
            log.info("Alert Already CLOSED with id {}",alertId);
            throw new AlertStatusException("Alert Already CLOSED with id " + alertId);
        }

        alert.setStatus(AlertStatus.CLOSED);
        AlertsEntity saved = alertsRepository.save(alert);
        log.info("Alert Closed with id {}",alertId);

        return convertToDto(saved);
    }

    public AlertsDTO convertToDto(AlertsEntity entity) {
        String fncName = "convertToDto";
        log.info("{} Converting Entity to DTO", fncName);

        AlertsDTO dto = new AlertsDTO();

        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setSeverity(entity.getSeverity());
        dto.setStatus(entity.getStatus());
        dto.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        if (entity.getAssignedTo() != null) {
            dto.setAssignedTo(entity.getAssignedTo().getId());
        }

        if (entity.getDevice() != null) {
            dto.setDeviceId(entity.getDevice().getId());
        }

        dto.setTagIds(
                entity.getTags()
                        .stream()
                        .map(TagsEntity::getId)
                        .collect(Collectors.toSet())
        );

        return dto;
    }


}
