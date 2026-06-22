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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AlertsImplTest {

    @Mock
    private AlertsRepository alertsRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private DeviceRepository deviceRepository;

    @Mock
    private TagRepository tagsRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private AlertsImpl alertsService;

    @Test
    void shouldReturnAllAlerts() {

        AlertsEntity entity = new AlertsEntity();

        when(alertsRepository.findAll(any(Sort.class)))
                .thenReturn(List.of(entity));

        List<AlertsDTO> result =
                alertsService.getAllAlerts();

        assertEquals(1, result.size());

        verify(alertsRepository)
                .findAll(any(Sort.class));
    }

    @Test
    void shouldAssignAlertToUser() {

        Long alertId = 1L;
        Long userId = 10L;

        AlertsEntity alert = new AlertsEntity();

        UsersEntity user = new UsersEntity();
        user.setId(userId);

        when(alertsRepository.findById(alertId))
                .thenReturn(Optional.of(alert));

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(alertsRepository.save(alert))
                .thenReturn(alert);

        AlertsDTO result =
                alertsService.assignAlert(alertId, userId);

        assertEquals(userId,
                result.getAssignedTo());

        verify(alertsRepository)
                .save(alert);
    }

    @Test
    void shouldThrowExceptionWhenAlertNotFound() {

        when(alertsRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> alertsService.assignAlert(
                        1L,
                        10L)
        );
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {

        AlertsEntity alert = new AlertsEntity();

        when(alertsRepository.findById(1L))
                .thenReturn(Optional.of(alert));

        when(userRepository.findById(10L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> alertsService.assignAlert(
                        1L,
                        10L)
        );
    }

    @Test
    void shouldResolveAlert() {

        AlertsEntity alert = new AlertsEntity();

        when(alertsRepository.findById(1L))
                .thenReturn(Optional.of(alert));

        when(alertsRepository.save(alert))
                .thenReturn(alert);

        AlertsDTO result =
                alertsService.resolveAlert(1L);

        assertEquals(
                AlertStatus.RESOLVED,
                result.getStatus());
    }

    @Test
    void shouldGenerateAlertSuccessfully() {

        AlertsDTO dto = new AlertsDTO();

        dto.setAssignedTo(1L);
        dto.setDeviceId(2L);
        dto.setTagIds(Set.of(100L));

        UsersEntity user = new UsersEntity();
        user.setId(1L);

        DevicesEntity device = new DevicesEntity();
        device.setId(2L);

        TagsEntity tag = new TagsEntity();
        tag.setId(100L);

        AlertsEntity savedEntity =
                new AlertsEntity();

        savedEntity.setAssignedTo(user);
        savedEntity.setDevice(device);
        savedEntity.setTags(Set.of(tag));

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(deviceRepository.findById(2L))
                .thenReturn(Optional.of(device));

        when(tagsRepository.findById(100L))
                .thenReturn(Optional.of(tag));

        when(alertsRepository.save(any()))
                .thenReturn(savedEntity);

        AlertsDTO result =
                alertsService.generateAlert(dto);

        assertNotNull(result);

        verify(alertsRepository)
                .save(any(AlertsEntity.class));
    }

    @Test
    void shouldThrowExceptionWhenAssignedUserNotFound() {

        AlertsDTO dto = new AlertsDTO();
        dto.setAssignedTo(1L);

        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> alertsService.generateAlert(dto)
        );
    }

    @Test
    void shouldThrowExceptionWhenDeviceNotFound() {

        AlertsDTO dto = new AlertsDTO();

        dto.setAssignedTo(1L);
        dto.setDeviceId(2L);

        UsersEntity user = new UsersEntity();

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(deviceRepository.findById(2L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> alertsService.generateAlert(dto)
        );
    }

    @Test
    void shouldReturnAlertsByStatus() {

        AlertsEntity alert = new AlertsEntity();

        when(alertsRepository.findByStatus(
                AlertStatus.OPEN))
                .thenReturn(List.of(alert));

        List<AlertsDTO> result =
                alertsService.getAlertByStatus(
                        AlertStatus.OPEN);

        assertEquals(1, result.size());
    }

    @Test
    void shouldReturnAlertsBySeverity() {

        AlertsEntity alert = new AlertsEntity();

        when(alertsRepository.findBySeverity(
                Severity.CRITICAL))
                .thenReturn(List.of(alert));

        List<AlertsDTO> result =
                alertsService.getAlertBySeverity(
                        Severity.CRITICAL);

        assertEquals(1, result.size());
    }

    @Test
    void shouldConvertEntityToDto() {

        UsersEntity user = new UsersEntity();
        user.setId(10L);

        DevicesEntity device = new DevicesEntity();
        device.setId(20L);

        TagsEntity tag = new TagsEntity();
        tag.setId(30L);

        AlertsEntity entity =
                new AlertsEntity();

        entity.setId(1L);
        entity.setTitle("CPU Alert");
        entity.setAssignedTo(user);
        entity.setDevice(device);
        entity.setTags(Set.of(tag));

        AlertsDTO dto =
                alertsService.convertToDto(entity);

        assertEquals(1L, dto.getId());
        assertEquals(10L, dto.getAssignedTo());
        assertEquals(20L, dto.getDeviceId());
        assertTrue(dto.getTagIds().contains(30L));
    }
}