package com.eimp.service.impl;

import com.eimp.dto.AlertsDTO;
import com.eimp.dto.DevicesDTO;
import com.eimp.dto.MetricsDTO;
import com.eimp.entity.AlertsEntity;
import com.eimp.entity.DevicesEntity;
import com.eimp.entity.MetricsEntity;
import com.eimp.enums.AlertStatus;
import com.eimp.enums.Severity;
import com.eimp.exception.ResourceNotFoundException;
import com.eimp.repository.AlertsRepository;
import com.eimp.repository.DeviceRepository;
import com.eimp.repository.MetricsRepository;
import com.eimp.service.DeviceService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DeviceImpl implements DeviceService {

    private final DeviceRepository deviceRepository;
    private final MetricsRepository metricsRepository;
    private final ModelMapper modelMapper;
    private final AlertsRepository alertsRepository;

    public DeviceImpl(DeviceRepository deviceRepository, MetricsRepository metricsRepository, ModelMapper modelMapper, AlertsRepository alertsRepository) {
        this.deviceRepository = deviceRepository;
        this.metricsRepository = metricsRepository;
        this.modelMapper = modelMapper;
        this.alertsRepository = alertsRepository;
    }

    @Override
    public List<DevicesDTO> getAllDevices() {
        List<DevicesEntity> allDevices=deviceRepository.findAll();
        return allDevices
                .stream()
                .map((devices)->  modelMapper.map(devices,DevicesDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public DevicesDTO createDevice(DevicesDTO devicesDTO) {
        DevicesEntity devicesEntity = modelMapper.map(devicesDTO, DevicesEntity.class);
        DevicesEntity savedDevice = deviceRepository.save(devicesEntity);
        return modelMapper.map(savedDevice, DevicesDTO.class);
    }

    @Override
    public Optional<DevicesDTO> getDeviceById(Long id) {
        return deviceRepository.findById(id).map(devicesEntity -> modelMapper.map(devicesEntity, DevicesDTO.class));

    }

    @Override
    public DevicesDTO updateDeviceWithId(Long id,DevicesDTO devicesDTO) {
        if(isDeviceExist(id)){
            DevicesEntity devicesEntity = modelMapper.map(devicesDTO, DevicesEntity.class);
            devicesEntity.setId(id);
            DevicesEntity savedDeviceEntity = deviceRepository.save(devicesEntity);
            return modelMapper.map(savedDeviceEntity, DevicesDTO.class);
        }else {
            throw new ResourceNotFoundException("Device not found with id  : "+id);
        }
    }

    @Override
    public Boolean deleteDeviceById(Long id) {
        if(isDeviceExist(id)){
            deviceRepository.deleteById(id);
            return true;
        }else {
            throw new ResourceNotFoundException("Device not found with id : "+id);
        }
    }

    @Override
    public MetricsDTO saveMetrics(Long id, MetricsDTO metricsDTO) {
        if(isDeviceExist(id)){
            metricsDTO.setDeviceId(id);
            MetricsEntity toBeSaved = modelMapper.map(metricsDTO, MetricsEntity.class);
            MetricsEntity metricsEntity = metricsRepository.save(toBeSaved);
            generateAlert(metricsDTO);
            return modelMapper.map(metricsEntity,MetricsDTO.class);
        }else{
            throw new ResourceNotFoundException("Device not found with id: " + id);
        }
    }

    public void generateAlert(MetricsDTO metricsDTO){
        AlertsDTO alertsDTO=new AlertsDTO();
        alertsDTO.setDeviceId(metricsDTO.getDeviceId());
        alertsDTO.setSeverity(Severity.CRITICAL);
        alertsDTO.setTagIds(new HashSet<>(Set.of(1L, 2L, 3L)));
        alertsDTO.setTitle("High CPU Usage");
        alertsDTO.setDescription("CPU usage exceeded threshold on device");
        alertsDTO.setStatus(AlertStatus.OPEN);
        AlertsEntity alertsEntity = modelMapper.map(alertsDTO, AlertsEntity.class);
        alertsRepository.save(alertsEntity);
    }


    public boolean isDeviceExist(Long id){
       return deviceRepository.existsById(id);
    }


}
