package com.magiceast.magiceast_backend.repositorio

import com.magiceast.magiceast_backend.modelo.Usuario
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UsuarioRepository : JpaRepository<Usuario, Long> {
    fun existsByEmail(email: String): Boolean
    fun findByEmail(email: String): Usuario?
}
