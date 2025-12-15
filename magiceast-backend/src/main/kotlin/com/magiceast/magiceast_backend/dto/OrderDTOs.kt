package com.magiceast.magiceast_backend.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "DTO para crear una nueva orden")
data class CreateOrderRequest(
    
    @field:Schema(
        description = "ID del usuario que realiza la orden",
        example = "1"
    )
    val usuarioId: Long,
    
    @field:Schema(
        description = "Dirección de envío de la orden",
        example = "Av. Siempre Viva 742, Valparaíso"
    )
    val direccionEnvio: String,
    
    @field:Schema(
        description = "Lista de items a incluir en la orden"
    )
    val items: List<OrderItemRequest>
)

@Schema(description = "DTO para un item dentro de la orden")
data class OrderItemRequest(
    
    @field:Schema(
        description = "ID del producto",
        example = "5"
    )
    val productoId: Long,
    
    @field:Schema(
        description = "Cantidad de unidades",
        example = "2",
        minimum = "1"
    )
    val cantidad: Int
)

@Schema(description = "DTO de respuesta con los datos de una orden")
data class OrderResponse(
    
    @field:Schema(description = "ID de la orden", example = "1")
    val id: Long,
    
    @field:Schema(description = "ID del usuario", example = "1")
    val usuarioId: Long,
    
    @field:Schema(description = "Nombre del usuario", example = "Agustín Morales")
    val usuarioNombre: String,
    
    @field:Schema(description = "Items de la orden")
    val items: List<OrderItemResponse>,
    
    @field:Schema(description = "Fecha de creación", example = "2025-12-14T22:47:00")
    val fechaCreacion: String,
    
    @field:Schema(description = "Total de la orden", example = "45990")
    val total: Int,
    
    @field:Schema(description = "Estado de la orden", example = "PENDIENTE")
    val estado: String,
    
    @field:Schema(description = "Dirección de envío", example = "Av. Siempre Viva 742, Valparaíso")
    val direccionEnvio: String
)

@Schema(description = "DTO de respuesta para un item de la orden")
data class OrderItemResponse(
    
    @field:Schema(description = "ID del item", example = "1")
    val id: Long,
    
    @field:Schema(description = "ID del producto", example = "5")
    val productoId: Long,
    
    @field:Schema(description = "Nombre del producto", example = "Mazo Intro Azul")
    val productoNombre: String,
    
    @field:Schema(description = "Cantidad", example = "2")
    val cantidad: Int,
    
    @field:Schema(description = "Precio unitario", example = "25990")
    val precioUnitario: Int,
    
    @field:Schema(description = "Subtotal", example = "51980")
    val subtotal: Int
)
