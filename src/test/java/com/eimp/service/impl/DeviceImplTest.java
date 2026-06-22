package com.eimp.service.impl;

import com.eimp.dto.AlertsDTO;
import com.eimp.dto.DevicesDTO;
import com.eimp.dto.MetricsDTO;
import com.eimp.entity.AlertsEntity;
import com.eimp.entity.DevicesEntity;
import com.eimp.entity.MetricsEntity;
import com.eimp.exception.ResourceNotFoundException;
import com.eimp.repository.AlertsRepository;
import com.eimp.repository.DeviceRepository;
import com.eimp.repository.MetricsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeviceImplTest {

    @Mock
    private DeviceRepository deviceRepository;

    @Mock
    private MetricsRepository metricsRepository;

    @Mock
    private AlertsRepository alertsRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private DeviceImpl deviceService;

    @Test
    void shouldReturnAllDevices() {

        DevicesEntity entity = new DevicesEntity();
        DevicesDTO dto = new DevicesDTO();

        when(deviceRepository.findAll())
                .thenReturn(List.of(entity));

        when(modelMapper.map(entity, DevicesDTO.class))
                .thenReturn(dto);

        List<DevicesDTO> result =
                deviceService.getAllDevices();

        assertEquals(1, result.size());

        verify(deviceRepository).findAll();
    }

    @Test
    void shouldCreateDeviceSuccessfully() {

        DevicesDTO dto = new DevicesDTO();

        DevicesEntity entity = new DevicesEntity();

        when(modelMapper.map(dto, DevicesEntity.class))
                .thenReturn(entity);

        when(deviceRepository.save(entity))
                .thenReturn(entity);

        when(modelMapper.map(entity, DevicesDTO.class))
                .thenReturn(dto);

        DevicesDTO result =
                deviceService.createDevice(dto);

        assertNotNull(result);

        verify(deviceRepository).save(entity);
    }

    @Test
    void shouldReturnDeviceById() {

        Long id = 1L;

        DevicesEntity entity = new DevicesEntity();
        DevicesDTO dto = new DevicesDTO();

        when(deviceRepository.findById(id))
                .thenReturn(Optional.of(entity));

        when(modelMapper.map(entity, DevicesDTO.class))
                .thenReturn(dto);

        Optional<DevicesDTO> result =
                deviceService.getDeviceById(id);

        assertTrue(result.isPresent());
    }

    @Test
    void shouldReturnEmptyOptionalWhenDeviceNotFound() {

        Long id = 1L;

        when(deviceRepository.findById(id))
                .thenReturn(Optional.empty());

        Optional<DevicesDTO> result =
                deviceService.getDeviceById(id);

        assertTrue(result.isEmpty());
    }
    @Test
    void shouldUpdateDeviceSuccessfully() {

        Long id = 1L;

        DevicesDTO dto = new DevicesDTO();

        DevicesEntity entity = new DevicesEntity();

        when(deviceRepository.existsById(id))
                .thenReturn(true);

        when(modelMapper.map(dto, DevicesEntity.class))
                .thenReturn(entity);

        when(deviceRepository.save(entity))
                .thenReturn(entity);

        when(modelMapper.map(entity, DevicesDTO.class))
                .thenReturn(dto);

        DevicesDTO result =
                deviceService.updateDeviceWithId(id, dto);

        assertNotNull(result);

        verify(deviceRepository).save(entity);
    }
    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingDevice() {

        Long id = 1L;

        when(deviceRepository.existsById(id))
                .thenReturn(false);

        assertThrows(
                ResourceNotFoundException.class,
                () -> deviceService.updateDeviceWithId(
                        id,
                        new DevicesDTO())
        );
    }
    @Test
    void shouldDeleteDeviceSuccessfully() {

        Long id = 1L;

        when(deviceRepository.existsById(id))
                .thenReturn(true);

        Boolean result =
                deviceService.deleteDeviceById(id);

        assertTrue(result);

        verify(deviceRepository)
                .deleteById(id);
    }
    @Test
    void shouldThrowExceptionWhenDeletingNonExistingDevice() {

        Long id = 1L;

        when(deviceRepository.existsById(id))
                .thenReturn(false);

        assertThrows(
                ResourceNotFoundException.class,
                () -> deviceService.deleteDeviceById(id)
        );
    }

    @Test
    void shouldSaveMetricsSuccessfully() {

        Long deviceId = 1L;

        MetricsDTO dto = new MetricsDTO();

        MetricsEntity entity = new MetricsEntity();

        when(deviceRepository.existsById(deviceId))
                .thenReturn(true);

        when(modelMapper.map(dto, MetricsEntity.class))
                .thenReturn(entity);

        when(metricsRepository.save(entity))
                .thenReturn(entity);

        when(modelMapper.map(entity, MetricsDTO.class))
                .thenReturn(dto);

        DeviceImpl spyService =
                Mockito.spy(
                        new DeviceImpl(
                                deviceRepository,
                                metricsRepository,
                                modelMapper,
                                alertsRepository));

        doNothing()
                .when(spyService)
                .generateAlert(any());

        MetricsDTO result =
                spyService.saveMetrics(deviceId, dto);

        assertNotNull(result);

        verify(metricsRepository)
                .save(entity);

        verify(spyService)
                .generateAlert(dto);
    }
    @Test
    void shouldThrowExceptionWhenSavingMetricsForInvalidDevice() {

        Long deviceId = 1L;

        when(deviceRepository.existsById(deviceId))
                .thenReturn(false);

        assertThrows(
                ResourceNotFoundException.class,
                () -> deviceService.saveMetrics(
                        deviceId,
                        new MetricsDTO())
        );
    }
    @Test
    void shouldGenerateAlertSuccessfully() {

        MetricsDTO metricsDTO =
                new MetricsDTO();

        metricsDTO.setDeviceId(1L);

        AlertsEntity alertsEntity =
                new AlertsEntity();

        when(modelMapper.map(
                any(AlertsDTO.class),
                eq(AlertsEntity.class)))
                .thenReturn(alertsEntity);

        deviceService.generateAlert(metricsDTO);

        verify(alertsRepository)
                .save(alertsEntity);
    }
    @Test
    void shouldReturnTrueWhenDeviceExists() {

        when(deviceRepository.existsById(1L))
                .thenReturn(true);

        assertTrue(
                deviceService.isDeviceExist(1L));
    }

    @Test
    void shouldReturnFalseWhenDeviceDoesNotExist() {

        when(deviceRepository.existsById(1L))
                .thenReturn(false);

        assertFalse(
                deviceService.isDeviceExist(1L));
    }
}