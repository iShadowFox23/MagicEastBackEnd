package com.magiceast.magiceast_backend.security

import com.magiceast.magiceast_backend.repositorio.UsuarioRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
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
            ?: throw UsernameNotFoundException("Usuario no encontrado")

        val authorities = listOf(SimpleGrantedAuthority("ROLE_" + usuario.rol))

        return User(
            usuario.email,
            usuario.contrasena,
            authorities
        )
    }
}
