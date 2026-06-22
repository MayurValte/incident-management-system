package com.eimp.repository;

import com.eimp.entity.MetricsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface MetricsRepository extends JpaRepository<MetricsEntity,Long> {

    @Query("SELECT m FROM MetricsEntity m")
    Page<MetricsEntity> findAllMetrics(Pageable pageable);

}
