package com.magiceast.magiceast_backend.controller

import com.magiceast.magiceast_backend.modelo.Producto
import com.magiceast.magiceast_backend.service.ProductoService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/productos")
class ProductoController(
    private val productoService: ProductoService
) {

    @GetMapping
    fun listarProductos(): List<Producto> =
        productoService.listarTodos()

    @GetMapping("/{id}")
    fun obtenerProducto(@PathVariable id: Long): ResponseEntity<Producto> {
        val producto = productoService.buscarPorId(id)
        return if (producto != null) {
            ResponseEntity.ok(producto)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @PostMapping
    fun crearProducto(@RequestBody producto: Producto): ResponseEntity<Producto> {
        val creado = productoService.crear(producto)
        return ResponseEntity.ok(creado)
    }

    @PutMapping("/{id}")
    fun actualizarProducto(
        @PathVariable id: Long,
        @RequestBody producto: Producto
    ): ResponseEntity<Producto> {
        val actualizado = productoService.actualizar(id, producto)
        return if (actualizado != null) {
            ResponseEntity.ok(actualizado)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @DeleteMapping("/{id}")
    fun eliminarProducto(@PathVariable id: Long): ResponseEntity<Void> {
        productoService.eliminar(id)
        return ResponseEntity.noContent().build()
    }
}