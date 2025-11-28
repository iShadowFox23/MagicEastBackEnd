package com.magiceast.magiceast_backend.service

import com.magiceast.magiceast_backend.dto.UsuarioRequest
import com.magiceast.magiceast_backend.modelo.Usuario
import com.magiceast.magiceast_backend.repositorio.UsuarioRepository
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class UsuarioService(
    private val usuarioRepository: UsuarioRepository
) {

    private val encoder = BCryptPasswordEncoder()

    fun registrarUsuario(request: UsuarioRequest): Usuario {

        if (usuarioRepository.existsByEmail(request.email)) {
            throw IllegalArgumentException("El correo ya está registrado")
        }

        if (request.contrasena.isBlank()) {
            throw IllegalArgumentException("La contraseña es obligatoria")
        }

        val contrasenaEncriptada: String = encoder.encode(request.contrasena) ?: throw IllegalStateException("Error al encriptar contraseña")



        val usuario = Usuario(
            nombre = request.nombre,
            email = request.email,
            direccion = request.direccion,
            contrasena = contrasenaEncriptada
        )

        return usuarioRepository.save(usuario)
    }

    fun listarUsuarios(): List<Usuario> =
        usuarioRepository.findAll().filterNotNull()   // ← FIX IMPORTANTE

    fun obtenerPorEmail(email: String): Usuario? =
        usuarioRepository.findByEmail(email)

    fun eliminarUsuario(id: Long): Boolean {
        return if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id)
            true
        } else {
            false
        }
    }
}
