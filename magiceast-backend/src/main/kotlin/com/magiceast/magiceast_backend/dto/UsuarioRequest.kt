package com.magiceast.magiceast_backend.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class UsuarioRequest(

    @field:NotBlank(message = "El nombre es obligatorio")
    val nombre: String,

    @field:NotBlank(message = "El correo es obligatorio")
    @field:Pattern(
        regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$",
        message = "El correo no es válido"
    )
    val email: String,

    @field:NotBlank(message = "La dirección es obligatoria")
    val direccion: String,

    @field:NotBlank(message = "La contraseña es obligatoria")
    @field:Pattern(
        regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-={}\\[\\]|:;\"'<>,.?/]).{8,}$",
        message = "La contraseña debe tener al menos 8 caracteres, una mayúscula, un número y un símbolo"
    )
    val contrasena: String
)
