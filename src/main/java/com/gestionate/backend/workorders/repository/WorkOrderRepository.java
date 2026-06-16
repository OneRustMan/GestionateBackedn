package com.gestionate.backend.workorders.repository;

import com.gestionate.backend.workorders.model.WorkOrder;
import com.gestionate.backend.workorders.model.WorkOrderPriority;
import com.gestionate.backend.workorders.model.WorkOrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WorkOrderRepository extends JpaRepository<WorkOrder, Long> {

    Optional<WorkOrder> findTopByOrderByIdDesc();

    boolean existsByReport_Id(Long reportId);

    Optional<WorkOrder> findByReport_Id(Long reportId);

    List<WorkOrder> findByCleaningStaffIdAndStatusInOrderByCreatedAtDesc(
            Long cleaningStaffId,
            List<WorkOrderStatus> statuses);

    List<WorkOrder> findByCleaningStaffIdAndStatusInAndPriorityOrderByCreatedAtDesc(
            Long cleaningStaffId,
            List<WorkOrderStatus> statuses,
            WorkOrderPriority priority);
}
