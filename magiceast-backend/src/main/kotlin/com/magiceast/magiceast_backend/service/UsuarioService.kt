package com.magiceast.magiceast_backend.service

import com.magiceast.magiceast_backend.dto.UsuarioRequest
import com.magiceast.magiceast_backend.modelo.Usuario
import com.magiceast.magiceast_backend.repositorio.UsuarioRepository
import org.springframework.stereotype.Service

@Service
class UsuarioService(
    private val usuarioRepository: UsuarioRepository
) {

    fun registrarUsuario(request: UsuarioRequest): Usuario {

        if (usuarioRepository.existsByEmail(request.email)) {
            throw IllegalArgumentException("El correo ya está registrado")
        }

        // SIN ENCRIPTAR
        val usuario = Usuario(
            nombre = request.nombre,
            email = request.email,
            direccion = request.direccion,
            contrasena = request.contrasena
        )

        return usuarioRepository.save(usuario)
    }

    fun listarUsuarios(): List<Usuario> = usuarioRepository.findAll()

    fun obtenerPorEmail(email: String): Usuario? =
        usuarioRepository.findByEmail(email)
}
