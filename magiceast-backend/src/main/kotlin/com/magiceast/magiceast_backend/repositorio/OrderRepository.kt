package com.magiceast.magiceast_backend.repositorio

import com.magiceast.magiceast_backend.modelo.Order
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderRepository : JpaRepository<Order, Long> {
    
    // Encontrar todas las órdenes de un usuario específico
    fun findByUsuarioId(usuarioId: Long): List<Order>
    
    // Encontrar órdenes por estado
    fun findByEstado(estado: String): List<Order>
    
    // Encontrar órdenes de un usuario por estado
    fun findByUsuarioIdAndEstado(usuarioId: Long, estado: String): List<Order>
}
