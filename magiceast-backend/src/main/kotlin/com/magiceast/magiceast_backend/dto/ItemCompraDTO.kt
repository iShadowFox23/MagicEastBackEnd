package com.magiceast.magiceast_backend.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(
    description = "Representa un ítem dentro de la compra (producto + cantidad)."
)
data class ItemCompraDTO(

    @field:Schema(
        description = "ID del producto que se está comprando.",
        example = "1"
    )
    val productoId: Long,

    @field:Schema(
        description = "Cantidad de unidades de ese producto.",
        example = "3",
        minimum = "1"
    )
    val cantidad: Int
)
