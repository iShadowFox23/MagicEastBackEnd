package com.magiceast.magiceast_backend.service

import com.magiceast.magiceast_backend.modelo.Producto
import com.magiceast.magiceast_backend.repositorio.ProductoRepository
import org.springframework.stereotype.Service

@Service
class ProductoService(
    private val productoRepository: ProductoRepository
) {

    fun listarTodos(): List<Producto> =
        productoRepository.findAll()

    fun buscarPorId(id: Long): Producto? =
        productoRepository.findById(id).orElse(null)

    fun crear(producto: Producto): Producto =
        productoRepository.save(producto)

    fun actualizar(id: Long, producto: Producto): Producto? {
        val existente = productoRepository.findById(id).orElse(null) ?: return null

        val actualizado = existente.copy(
            nombre = producto.nombre,
            marca = producto.marca,
            categorias = producto.categorias,
            precio = producto.precio,
            stock = producto.stock,
            descripcion = producto.descripcion
        )

        return productoRepository.save(actualizado)
    }

    fun eliminar(id: Long) {
        if (productoRepository.existsById(id)) {
            productoRepository.deleteById(id)
        }
    }
}