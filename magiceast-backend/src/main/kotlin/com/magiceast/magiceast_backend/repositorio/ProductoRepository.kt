package com.magiceast.magiceast_backend.repositorio

import com.magiceast.magiceast_backend.modelo.Producto
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductoRepository : JpaRepository<Producto, Long> {

    fun findByMarca(marca: String): List<Producto>

    // Busca productos por categorias que contengan el string exacto
    fun findByCategoriasContainingIgnoreCase(texto: String): List<Producto>
}