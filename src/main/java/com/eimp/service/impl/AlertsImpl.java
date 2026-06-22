package com.eimp.service.impl;

import com.eimp.dto.AlertsDTO;
import com.eimp.entity.AlertsEntity;
import com.eimp.entity.DevicesEntity;
import com.eimp.entity.TagsEntity;
import com.eimp.entity.UsersEntity;
import com.eimp.enums.AlertStatus;
import com.eimp.enums.Severity;
import com.eimp.exception.ResourceNotFoundException;
import com.eimp.repository.AlertsRepository;
import com.eimp.repository.DeviceRepository;
import com.eimp.repository.TagRepository;
import com.eimp.repository.UserRepository;
import com.eimp.service.AlertsService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AlertsImpl implements AlertsService {

    private final AlertsRepository alertsRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final DeviceRepository deviceRepository;
    private final TagRepository tagsRepository;

    public AlertsImpl(AlertsRepository alertsRepository, ModelMapper modelMapper, UserRepository userRepository, DeviceRepository deviceRepository,TagRepository tagRepository) {
        this.alertsRepository = alertsRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.deviceRepository = deviceRepository;
        this.tagsRepository = tagRepository;
    }

    @Override
    public List<AlertsDTO> getAllAlerts() {
        List<AlertsEntity> allAlerts =
                alertsRepository.findAll(
                        Sort.by(Sort.Direction.DESC, "severity"));

        return allAlerts.stream()
                .map(this::convertToDto)
                .toList();
    }

    @Override
    public AlertsDTO assignAlert(Long alertId, Long userId) {
        AlertsEntity alertsEntity = alertsRepository.findById(alertId).orElseThrow(() -> new ResourceNotFoundException("Alert not found with id: " + alertId));
        UsersEntity user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User with id " + userId + "Not present"));

        alertsEntity.setAssignedTo(user);
        AlertsEntity savedAlertEntity = alertsRepository.save(alertsEntity);

        return convertToDto(savedAlertEntity);
    }

    @Override
    public AlertsDTO resolveAlert(Long alertId) {
        AlertsEntity alert = alertsRepository.findById(alertId)
                .orElseThrow(() -> new ResourceNotFoundException("Alert not found with id: " + alertId));

        alert.setStatus(AlertStatus.RESOLVED);
        AlertsEntity saved = alertsRepository.save(alert);

        return convertToDto(saved);
    }

    @Override
    public AlertsDTO generateAlert(AlertsDTO alertsDTO) {
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
            Set<TagsEntity> tags = alertsDTO.getTagIds().stream()
                    .map(tagId -> tagsRepository.findById(tagId)
                            .orElseThrow(() -> new ResourceNotFoundException("Tag not found with id: " + tagId)))
                    .collect(Collectors.toSet());
            alertsEntity.setTags(tags);
        }

        AlertsEntity savedEntity = alertsRepository.save(alertsEntity);
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
        AlertsEntity alert = alertsRepository.findById(alertId)
                .orElseThrow(() -> new ResourceNotFoundException("Alert not found with id: " + alertId));

        alert.setStatus(AlertStatus.CLOSED);
        AlertsEntity saved = alertsRepository.save(alert);

        return convertToDto(saved);
    }

    public AlertsDTO convertToDto(AlertsEntity entity) {
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
