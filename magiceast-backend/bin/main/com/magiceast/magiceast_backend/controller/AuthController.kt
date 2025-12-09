package com.magiceast.magiceast_backend.controller

import com.magiceast.magiceast_backend.repositorio.UsuarioRepository
import com.magiceast.magiceast_backend.security.JwtService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.*

@Schema(
    description = "Datos necesarios para iniciar sesión en la aplicación."
)
data class LoginRequest(
    @field:Schema(
        description = "Correo electrónico del usuario.",
        example = "usuario@magiceast.com"
    )
    val email: String,

    @field:Schema(
        description = "Contraseña del usuario en texto plano.",
        example = "MagicEast123"
    )
    val contrasena: String
)

@Tag(
    name = "Autenticación",
    description = "Endpoints para autenticación de usuarios y generación de tokens JWT."
)
@RestController
@RequestMapping("/auth")
class AuthController(
    private val usuarioRepository: UsuarioRepository,
    private val jwtService: JwtService
) {

    private val encoder = BCryptPasswordEncoder()

    @Operation(
        summary = "Iniciar sesión",
        description = "Valida las credenciales del usuario y, si son correctas, devuelve un token JWT que puede usarse en las siguientes peticiones."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Login exitoso. Se devuelve un token JWT en el cuerpo de la respuesta."
            ),
            ApiResponse(
                responseCode = "400",
                description = "Credenciales inválidas o email no registrado."
            )
        ]
    )
    @PostMapping("/login")
    fun login(
        @RequestBody req: LoginRequest
    ): ResponseEntity<Any> {

        val usuario = usuarioRepository.findByEmail(req.email)
            ?: return ResponseEntity.status(400).body(mapOf("error" to "Email no registrado"))

        if (!encoder.matches(req.contrasena, usuario.contrasena)) {
            return ResponseEntity.status(400).body(mapOf("error" to "Contraseña incorrecta"))
        }

        val token = jwtService.generarToken(usuario.email)

        val response = mapOf(
            "token" to token,
            "id" to usuario.id,
            "nombre" to usuario.nombre,
            "email" to usuario.email,
            "rol" to usuario.rol
        )

        return ResponseEntity.ok(response)
    }
}
