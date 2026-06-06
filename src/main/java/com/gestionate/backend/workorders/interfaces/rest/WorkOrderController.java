package com.gestionate.backend.workorders.interfaces.rest;

import com.gestionate.backend.workorders.application.IWorkOrderService;
import com.gestionate.backend.workorders.interfaces.rest.dto.WorkOrderResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            @RequestParam Long cleaningStaffId) {
        return ResponseEntity.ok(
                workOrderService.findAvailableWorkOrders(cleaningStaffId));
    }
}
