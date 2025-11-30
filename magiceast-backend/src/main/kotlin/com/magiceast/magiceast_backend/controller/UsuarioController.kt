package com.magiceast.magiceast_backend.controlador

import com.magiceast.magiceast_backend.dto.UsuarioRequest
import com.magiceast.magiceast_backend.modelo.Usuario
import com.magiceast.magiceast_backend.service.UsuarioService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Tag(
    name = "Usuarios",
    description = "Gestión de usuarios registrados en Magic East (registro, consulta y eliminación)."
)
@RestController
@RequestMapping("/api/usuarios")
class UsuarioController(
    private val usuarioService: UsuarioService
) {

    @PostMapping
    fun registrar(@Valid @RequestBody request: UsuarioRequest): ResponseEntity<Any> {
        return try {
            val nuevo = usuarioService.registrarUsuario(request)
            ResponseEntity.ok(nuevo)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body(mapOf("error" to e.message))
        }
    }

    @GetMapping
    fun listar(): List<Usuario> = usuarioService.listarUsuarios()

    @GetMapping("/{email}")
    fun buscarPorEmail(
        @Parameter(description = "Correo electrónico del usuario a buscar.", example = "agustin@magiceast.com")
        @PathVariable email: String
    ): ResponseEntity<Usuario> {
        val usuario = usuarioService.obtenerPorEmail(email)
        return if (usuario != null) {
            ResponseEntity.ok(usuario)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @DeleteMapping("/{id}")
    fun eliminar(
        @Parameter(description = "ID del usuario a eliminar.", example = "5")
        @PathVariable id: Long
    ): ResponseEntity<Any> {
        return if (usuarioService.eliminarUsuario(id)) {
            ResponseEntity.ok(mapOf("mensaje" to "Usuario eliminado"))
        } else {
            ResponseEntity.status(404).body(mapOf("error" to "Usuario no encontrado"))
        }
    }

    @PatchMapping("/{id}/rol")
    fun actualizarRol(
        @PathVariable id: Long,
        @RequestParam rol: String
    ): ResponseEntity<Any> {
        val usuario = usuarioService.obtenerUsuario(id) ?: return ResponseEntity.status(404).body(mapOf("error" to "Usuario no encontrado"))
        usuario.rol = rol
        val actualizado = usuarioService.guardarUsuario(usuario)
        return ResponseEntity.ok(actualizado)
    }
}
