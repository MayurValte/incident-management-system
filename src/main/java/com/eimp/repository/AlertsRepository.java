package com.eimp.repository;

import com.eimp.entity.AlertsEntity;
import com.eimp.enums.AlertStatus;
import com.eimp.enums.Severity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertsRepository extends JpaRepository<AlertsEntity,Long> {

           List<AlertsEntity> findByStatus(AlertStatus status);
           List<AlertsEntity> findBySeverity(Severity severity);
}
