package com.magiceast.magiceast_backend.controller

import com.magiceast.magiceast_backend.dto.CreateOrderRequest
import com.magiceast.magiceast_backend.dto.OrderResponse
import com.magiceast.magiceast_backend.service.OrderService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Tag(
    name = "Órdenes",
    description = "Endpoints para gestionar órdenes de compra en Magic East."
)
@RestController
@RequestMapping("/api/ordenes")
class OrderController(
    private val orderService: OrderService
) {

    @Operation(
        summary = "Crear nueva orden",
        description = "Crea una nueva orden de compra para un usuario con los productos especificados. " +
                "Valida stock disponible y calcula el total automáticamente."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Orden creada exitosamente"),
            ApiResponse(responseCode = "400", description = "Datos inválidos o stock insuficiente"),
            ApiResponse(responseCode = "404", description = "Usuario o producto no encontrado")
        ]
    )
    @PostMapping
    fun crearOrden(
        @Valid @RequestBody request: CreateOrderRequest
    ): ResponseEntity<OrderResponse> {
        return try {
            val orden = orderService.crearOrden(request)
            ResponseEntity.ok(orden)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().build()
        } catch (e: IllegalStateException) {
            ResponseEntity.badRequest().build()
        }
    }

    @Operation(
        summary = "Obtener orden por ID",
        description = "Obtiene los detalles completos de una orden específica."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Orden encontrada"),
            ApiResponse(responseCode = "404", description = "Orden no encontrada")
        ]
    )
    @GetMapping("/{id}")
    fun obtenerOrden(
        @Parameter(description = "ID de la orden", example = "1")
        @PathVariable id: Long
    ): ResponseEntity<OrderResponse> {
        val orden = orderService.obtenerOrden(id)
        return orden?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()
    }

    @Operation(
        summary = "Listar todas las órdenes",
        description = "Obtiene la lista completa de todas las órdenes en el sistema."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
        ]
    )
    @GetMapping
    fun listarTodasLasOrdenes(): List<OrderResponse> {
        return orderService.listarTodasLasOrdenes()
    }

    @Operation(
        summary = "Listar órdenes por usuario",
        description = "Obtiene todas las órdenes realizadas por un usuario específico."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
        ]
    )
    @GetMapping("/usuario/{usuarioId}")
    fun listarOrdenesPorUsuario(
        @Parameter(description = "ID del usuario", example = "1")
        @PathVariable usuarioId: Long
    ): List<OrderResponse> {
        return orderService.listarOrdenesPorUsuario(usuarioId)
    }

    @Operation(
        summary = "Listar órdenes por estado",
        description = "Obtiene todas las órdenes que tienen un estado específico."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
        ]
    )
    @GetMapping("/estado/{estado}")
    fun listarOrdenesPorEstado(
        @Parameter(
            description = "Estado de la orden",
            example = "PENDIENTE"
        )
        @PathVariable estado: String
    ): List<OrderResponse> {
        return orderService.listarOrdenesPorEstado(estado)
    }

    @Operation(
        summary = "Actualizar estado de orden",
        description = "Actualiza el estado de una orden existente. " +
                "Estados válidos: PENDIENTE, CONFIRMADA, ENVIADA, ENTREGADA, CANCELADA"
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Estado actualizado"),
            ApiResponse(responseCode = "400", description = "Estado inválido"),
            ApiResponse(responseCode = "404", description = "Orden no encontrada")
        ]
    )
    @PatchMapping("/{id}/estado")
    fun actualizarEstado(
        @Parameter(description = "ID de la orden", example = "1")
        @PathVariable id: Long,
        @Parameter(description = "Nuevo estado", example = "CONFIRMADA")
        @RequestParam nuevoEstado: String
    ): ResponseEntity<OrderResponse> {
        return try {
            val orden = orderService.actualizarEstado(id, nuevoEstado)
            orden?.let { ResponseEntity.ok(it) }
                ?: ResponseEntity.notFound().build()
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().build()
        }
    }

    @Operation(
        summary = "Cancelar orden",
        description = "Marca una orden como CANCELADA."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Orden cancelada"),
            ApiResponse(responseCode = "404", description = "Orden no encontrada")
        ]
    )
    @PostMapping("/{id}/cancelar")
    fun cancelarOrden(
        @Parameter(description = "ID de la orden", example = "1")
        @PathVariable id: Long
    ): ResponseEntity<OrderResponse> {
        val orden = orderService.cancelarOrden(id)
        return orden?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()
    }
}
