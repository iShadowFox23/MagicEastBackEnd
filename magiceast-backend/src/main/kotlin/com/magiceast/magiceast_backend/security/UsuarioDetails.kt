package com.magiceast.magiceast_backend.security

import com.magiceast.magiceast_backend.modelo.Usuario
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UsuarioDetails(
    private val usuario: Usuario
) : UserDetails {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> =
        mutableListOf()

    override fun getPassword() = usuario.contrasena

    override fun getUsername() = usuario.email

    override fun isAccountNonExpired() = true
    override fun isAccountNonLocked() = true
    override fun isCredentialsNonExpired() = true
    override fun isEnabled() = true
}
