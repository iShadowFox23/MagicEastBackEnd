package com.magiceast.magiceast_backend.controlador

import com.magiceast.magiceast_backend.dto.UsuarioRequest
import com.magiceast.magiceast_backend.modelo.Usuario
import com.magiceast.magiceast_backend.service.UsuarioService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/usuarios")
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
    fun buscarPorEmail(@PathVariable email: String): ResponseEntity<Usuario> {
        val usuario = usuarioService.obtenerPorEmail(email)
        return if (usuario != null) ResponseEntity.ok(usuario)
        else ResponseEntity.notFound().build()
    }
}
