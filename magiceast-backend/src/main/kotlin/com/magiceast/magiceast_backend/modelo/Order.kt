package com.magiceast.magiceast_backend.modelo

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.persistence.*
import java.time.LocalDateTime

@Schema(
    description = "Orden de compra realizada por un usuario en Magic East."
)
@Entity
@Table(name = "ORDERS")
data class Order(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    @field:Schema(
        description = "Identificador único de la orden.",
        example = "1",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USUARIO_ID", nullable = false)
    @field:Schema(
        description = "Usuario que realizó la orden."
    )
    val usuario: Usuario,

    @OneToMany(mappedBy = "order", cascade = [CascadeType.ALL], orphanRemoval = true)
    @field:Schema(
        description = "Lista de productos incluidos en la orden."
    )
    val items: MutableList<OrderItem> = mutableListOf(),

    @Column(name = "FECHA_CREACION", nullable = false)
    @field:Schema(
        description = "Fecha y hora en que se creó la orden.",
        example = "2025-12-14T22:47:00",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    val fechaCreacion: LocalDateTime = LocalDateTime.now(),

    @Column(name = "TOTAL", nullable = false)
    @field:Schema(
        description = "Monto total de la orden en pesos chilenos.",
        example = "45990"
    )
    var total: Int = 0,

    @Column(name = "ESTADO", nullable = false, length = 50)
    @field:Schema(
        description = "Estado actual de la orden.",
        example = "PENDIENTE",
        allowableValues = ["PENDIENTE", "CONFIRMADA", "ENVIADA", "ENTREGADA", "CANCELADA"]
    )
    var estado: String = "PENDIENTE",

    @Column(name = "DIRECCION_ENVIO", nullable = false, length = 500)
    @field:Schema(
        description = "Dirección de envío de la orden.",
        example = "Av. Siempre Viva 742, Valparaíso"
    )
    var direccionEnvio: String
)
