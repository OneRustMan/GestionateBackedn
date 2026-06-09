package com.gestionate.backend.workorders.interfaces.rest;

import com.gestionate.backend.workorders.interfaces.rest.dto.CompleteWorkOrderRequest;
import com.gestionate.backend.workorders.interfaces.rest.dto.CompleteWorkOrderResponse;
import jakarta.validation.Valid;
import com.gestionate.backend.workorders.interfaces.rest.dto.TakeWorkOrderResponse;
import com.gestionate.backend.workorders.interfaces.rest.dto.WorkOrderDetailResponse;
import com.gestionate.backend.workorders.application.IWorkOrderService;
import com.gestionate.backend.workorders.interfaces.rest.dto.WorkOrderResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.gestionate.backend.workorders.domain.model.WorkOrderPriority;
import java.util.List;

@RestController
@RequestMapping("/api/work-orders")
@RequiredArgsConstructor
@Tag(name = "Work Orders", description = "Gestión de órdenes de trabajo del área operativa")
public class WorkOrderController {

    private final IWorkOrderService workOrderService;

    @Operation(summary = "Ver órdenes disponibles para el área operativa")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Órdenes disponibles obtenidas correctamente"),
            @ApiResponse(responseCode = "404", description = "Personal operativo no encontrado")
    })
    @GetMapping("/available")
    public ResponseEntity<List<WorkOrderResponse>> findAvailableWorkOrders(
            @RequestParam Long cleaningStaffId,

            @RequestParam(required = false) WorkOrderPriority priority) {
        return ResponseEntity.ok(
                workOrderService.findAvailableWorkOrders(cleaningStaffId, priority));
    }

    @Operation(summary = "Consultar detalle de una orden de trabajo")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Detalle de la orden obtenido correctamente"),
            @ApiResponse(responseCode = "400", description = "La orden requiere ubicación para ser atendida"),
            @ApiResponse(responseCode = "404", description = "Orden no disponible o personal operativo no encontrado")
    })
    @GetMapping("/{workOrderId}/detail")
    public ResponseEntity<WorkOrderDetailResponse> findWorkOrderDetail(
            @PathVariable Long workOrderId,
            @RequestParam Long cleaningStaffId) {
        return ResponseEntity.ok(
                workOrderService.findWorkOrderDetail(cleaningStaffId, workOrderId));
    }

    @Operation(summary = "Tomar una orden de trabajo disponible")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Orden tomada correctamente"),
            @ApiResponse(responseCode = "400", description = "La orden ya fue asignada"),
            @ApiResponse(responseCode = "404", description = "Orden no disponible o personal operativo no encontrado")
    })
    @PatchMapping("/{workOrderId}/take")
    public ResponseEntity<TakeWorkOrderResponse> takeWorkOrder(
            @PathVariable Long workOrderId,
            @RequestParam Long cleaningStaffId) {
        return ResponseEntity.ok(
                workOrderService.takeWorkOrder(cleaningStaffId, workOrderId));
    }

    @Operation(summary = "Marcar una orden de trabajo como completada")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Orden completada correctamente"),
            @ApiResponse(responseCode = "400", description = "Observación inválida o la orden no puede completarse"),
            @ApiResponse(responseCode = "404", description = "Orden no disponible o personal operativo no encontrado")
    })
    @PatchMapping("/{workOrderId}/complete")
    public ResponseEntity<CompleteWorkOrderResponse> completeWorkOrder(
            @PathVariable Long workOrderId,
            @RequestParam Long cleaningStaffId,
            @Valid @RequestBody CompleteWorkOrderRequest request) {
        return ResponseEntity.ok(
                workOrderService.completeWorkOrder(cleaningStaffId, workOrderId, request));
    }
}
