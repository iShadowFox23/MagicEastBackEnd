package com.magiceast.magiceast_backend.modelo

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.persistence.*

@Schema(
    description = "Usuario registrado en la plataforma Magic East."
)
@Entity
@Table(name = "Usuarios")
data class Usuario(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @field:Schema(
        description = "Identificador único del usuario.",
        example = "1",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    val id: Long = 0,

    @Column(nullable = false)
    @field:Schema(
        description = "Nombre completo del usuario.",
        example = "Agustín Morales"
    )
    var nombre: String,

    @Column(nullable = false, unique = true)
    @field:Schema(
        description = "Correo electrónico del usuario. Debe ser único.",
        example = "agustin@magiceast.com"
    )
    var email: String,

    @Column(nullable = false)
    @field:Schema(
        description = "Dirección del usuario.",
        example = "Av. Siempre Viva 742, Valparaíso"
    )
    var direccion: String,

    @Column(nullable = false)
    @field:Schema(
        description = "Contraseña almacenada en formato encriptado (bcrypt).",
        example = "\$2a\$10\$hashDeEjemplo1234567890",
        accessMode = Schema.AccessMode.READ_WRITE
    )
    var contrasena: String
)
