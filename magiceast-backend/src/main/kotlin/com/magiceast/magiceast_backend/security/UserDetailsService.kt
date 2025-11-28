package com.magiceast.magiceast_backend.security

import com.magiceast.magiceast_backend.repositorio.UsuarioRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UsuarioDetailsService(
    private val usuarioRepository: UsuarioRepository
) : UserDetailsService {

    override fun loadUserByUsername(email: String): UserDetails {
        val usuario = usuarioRepository.findByEmail(email)
            ?: throw UsernameNotFoundException("Usuario no encontrado: $email")

        return UsuarioDetails(usuario)
    }
}
