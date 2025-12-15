package com.magiceast.magiceast_backend.modelo

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.persistence.*

@Schema(
    description = "Item individual dentro de una orden, representa un producto y su cantidad."
)
@Entity
@Table(name = "ORDER_ITEMS")
data class OrderItem(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    @field:Schema(
        description = "Identificador único del item.",
        example = "1",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ID", nullable = false)
    @field:Schema(
        description = "Orden a la que pertenece este item."
    )
    val order: Order,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PRODUCTO_ID", nullable = false)
    @field:Schema(
        description = "Producto incluido en este item."
    )
    val producto: Producto,

    @Column(name = "CANTIDAD", nullable = false)
    @field:Schema(
        description = "Cantidad de unidades del producto.",
        example = "2",
        minimum = "1"
    )
    val cantidad: Int,

    @Column(name = "PRECIO_UNITARIO", nullable = false)
    @field:Schema(
        description = "Precio unitario del producto al momento de la compra.",
        example = "25990"
    )
    val precioUnitario: Int,

    @Column(name = "SUBTOTAL", nullable = false)
    @field:Schema(
        description = "Subtotal del item (cantidad × precio unitario).",
        example = "51980",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    val subtotal: Int = cantidad * precioUnitario
)
