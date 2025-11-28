package com.magiceast.magiceast_backend.controller

import com.magiceast.magiceast_backend.security.JwtService
import com.magiceast.magiceast_backend.repositorio.UsuarioRepository
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.*

data class LoginRequest(val email: String, val contrasena: String)

@RestController
@RequestMapping("/auth")
class AuthController(
    private val usuarioRepository: UsuarioRepository,
    private val jwtService: JwtService
) {

    private val encoder = BCryptPasswordEncoder()

    @PostMapping("/login")
    fun login(@RequestBody req: LoginRequest): ResponseEntity<Any> {

        val usuario = usuarioRepository.findByEmail(req.email)
            ?: return ResponseEntity.status(400).body(mapOf("error" to "Email no registrado"))

        if (!encoder.matches(req.contrasena, usuario.contrasena)) {
            return ResponseEntity.status(400).body(mapOf("error" to "Contraseña incorrecta"))
        }

        val token = jwtService.generarToken(usuario.email)

        return ResponseEntity.ok(mapOf("token" to token))
    }
}
