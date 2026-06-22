package com.eimp.controller;

import com.eimp.dto.DevicesDTO;
import com.eimp.dto.MetricsDTO;
import com.eimp.exception.ResourceNotFoundException;
import com.eimp.service.DeviceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/devices")
@PreAuthorize("hasRole('ADMIN')")
public class DeviceController {

    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @GetMapping
    public ResponseEntity<List<DevicesDTO>> getAllDevices(){
        List<DevicesDTO> allDevices = deviceService.getAllDevices();
        return ResponseEntity.ok(allDevices);
    }

    @PostMapping
    public ResponseEntity<DevicesDTO> createDevice(@Valid @RequestBody DevicesDTO devicesDTO){
        DevicesDTO device = deviceService.createDevice(devicesDTO);
        return ResponseEntity.ok(device);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<DevicesDTO> getDeviceById(@PathVariable Long id) throws Exception {
        Optional<DevicesDTO> deviceById = deviceService.getDeviceById(id);
       return deviceById
                .map(devicesDTO -> ResponseEntity.status(HttpStatus.CREATED).body(deviceById.get()))
                .orElseThrow(()-> new ResourceNotFoundException("Device Not found with Id: "+id));
    }


    @PutMapping(path = "/{id}")
    public ResponseEntity<DevicesDTO> updateDeviceById(@PathVariable Long id,@Valid @RequestBody DevicesDTO devicesDTO){
        DevicesDTO updatedDeviceWithId = deviceService.updateDeviceWithId(id, devicesDTO);
        return ResponseEntity.ok(updatedDeviceWithId);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Boolean> deleteDeviceById(@PathVariable Long id){
        Boolean deleteDeviceById = deviceService.deleteDeviceById(id);
        return ResponseEntity.ok(deleteDeviceById);
    }

    @PostMapping("/{id}/metrics")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<MetricsDTO> saveMetricsForDevice(@PathVariable Long id, @RequestBody MetricsDTO metricsDTO){

        MetricsDTO savedMetrics = deviceService.saveMetrics(id, metricsDTO);
        return new ResponseEntity<>(savedMetrics,HttpStatus.CREATED);
    }

}
