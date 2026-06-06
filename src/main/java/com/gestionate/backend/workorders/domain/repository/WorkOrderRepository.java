package com.gestionate.backend.workorders.domain.repository;

import com.gestionate.backend.workorders.domain.model.WorkOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WorkOrderRepository extends JpaRepository<WorkOrder, Long> {

    Optional<WorkOrder> findTopByOrderByIdDesc();

    boolean existsByReport_Id(Long reportId);

    Optional<WorkOrder> findByReport_Id(Long reportId);
}
