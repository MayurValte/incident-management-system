package com.eimp.service;

import com.eimp.dto.DevicesDTO;
import com.eimp.dto.MetricsDTO;
import com.eimp.entity.DevicesEntity;

import java.util.List;
import java.util.Optional;

public interface DeviceService {


    public List<DevicesDTO> getAllDevices();

    DevicesDTO createDevice(DevicesDTO devicesDTO);

    Optional<DevicesDTO> getDeviceById(Long id);

    DevicesDTO updateDeviceWithId(Long id,DevicesDTO devicesDTO);

    Boolean deleteDeviceById(Long id);

    MetricsDTO saveMetrics(Long id, MetricsDTO metricsDTO);
}
