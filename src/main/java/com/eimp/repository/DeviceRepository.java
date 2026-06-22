package com.eimp.repository;

import com.eimp.entity.DevicesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRepository extends JpaRepository<DevicesEntity, Long> {
}
