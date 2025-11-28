package com.magiceast.magiceast_backend.controller

import com.magiceast.magiceast_backend.modelo.Producto
import com.magiceast.magiceast_backend.service.ImagenService
import com.magiceast.magiceast_backend.service.ProductoService
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.nio.file.Files

@RestController
@RequestMapping("/api/productos")
class ProductoController(
    private val productoService: ProductoService,
    private val imagenService: ImagenService,

    @Value("\${app.upload-dir}")
    private val uploadDirPath: String
) {


    @GetMapping
    fun listarProductos(): List<Producto> =
        productoService.listarTodos().map { producto ->
            producto.copy(imagen = producto.imagen.substringAfterLast("/"))
        }

    @GetMapping("/{id}")
    fun obtenerProducto(@PathVariable id: Long): ResponseEntity<Producto> {
        val p = productoService.buscarPorId(id)
        return if (p != null) {
            ResponseEntity.ok(
                p.copy(imagen = p.imagen.substringAfterLast("/"))
            )
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @PostMapping
    fun crearProducto(@Valid @RequestBody producto: Producto): ResponseEntity<Producto> {

        val imagenLimpia = producto.imagen.substringAfterLast("/")

        val nuevo = producto.copy(imagen = imagenLimpia)

        return ResponseEntity.ok(productoService.crear(nuevo))
    }


    @PutMapping("/{id}")
    fun actualizarProducto(
        @PathVariable id: Long,
        @Valid @RequestBody producto: Producto
    ): ResponseEntity<Producto> {

        val imagenLimpia = producto.imagen.substringAfterLast("/")

        val actualizado = productoService.actualizar(id, producto.copy(imagen = imagenLimpia))

        return actualizado?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()
    }


    @DeleteMapping("/{id}")
    fun eliminarProducto(@PathVariable id: Long): ResponseEntity<Void> {
        productoService.eliminar(id)
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/crear-con-imagen", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun crearProductoConImagen(
        @RequestParam nombre: String,
        @RequestParam marca: String?,
        @RequestParam categorias: String,
        @RequestParam precio: Int,
        @RequestParam stock: Int,
        @RequestParam descripcion: String,
        @RequestParam imagen: MultipartFile
    ): ResponseEntity<Producto> {

        val nombreImagen = imagenService.guardarImagen(imagen)

        val producto = Producto(
            nombre = nombre,
            marca = marca,
            categorias = categorias,
            precio = precio,
            stock = stock,
            descripcion = descripcion,
            imagen = nombreImagen
        )

        return ResponseEntity.ok(productoService.crear(producto))
    }

    @GetMapping("/imagenes/{nombre}")
    fun obtenerImagen(@PathVariable nombre: String): ResponseEntity<ByteArray> {

        val archivo = File("$uploadDirPath/$nombre")


        if (!archivo.exists()) {
            return ResponseEntity.notFound().build()
        }

        val bytes = archivo.readBytes()
        val contentType = Files.probeContentType(archivo.toPath()) ?: "image/jpeg"

        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(contentType))
            .body(bytes)
    }
}
