package com.magiceast.magiceast_backend.dto

import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Schema

@Schema(
    description = "Compra que contiene uno o más ítems de productos."
)
data class CompraDTO(

    @field:ArraySchema(
        schema = Schema(implementation = ItemCompraDTO::class),
        arraySchema = Schema(
            description = "Listado de productos y cantidades que forman parte de la compra."
        )
    )
    val items: List<ItemCompraDTO>
)
