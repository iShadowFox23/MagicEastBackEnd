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
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses

@Tag(
    name = "Productos",
    description = "Endpoints para gestionar productos de Magic East."
)
@RestController
@RequestMapping("/api/productos")
class ProductoController(
    private val productoService: ProductoService,
    private val imagenService: ImagenService,

    @Value("\${app.upload-dir}")
    private val uploadDirPath: String
) {

    @Operation(
        summary = "Listar productos",
        description = "Obtiene la lista completa de productos disponibles. " +
                "La propiedad 'imagen' se devuelve solo con el nombre de archivo."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
        ]
    )
    @GetMapping
    fun listarProductos(): List<Producto> =
        productoService.listarTodos().map { producto ->
            producto.copy(imagen = producto.imagen.substringAfterLast("/"))
        }

    @Operation(
        summary = "Obtener producto por ID",
        description = "Obtiene un producto específico por su identificador."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Producto encontrado"),
            ApiResponse(responseCode = "404", description = "Producto no encontrado")
        ]
    )
    @GetMapping("/{id}")
    fun obtenerProducto(
        @Parameter(description = "ID del producto", example = "1")
        @PathVariable id: Long
    ): ResponseEntity<Producto> {
        val p = productoService.buscarPorId(id)
        return if (p != null) {
            ResponseEntity.ok(
                p.copy(imagen = p.imagen.substringAfterLast("/"))
            )
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @Operation(
        summary = "Crear producto (JSON)",
        description = "Crea un nuevo producto recibiendo un objeto JSON. " +
                "El campo 'imagen' puede ser un nombre de archivo o una ruta; " +
                "el backend se queda solo con el nombre de archivo."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Producto creado"),
            ApiResponse(responseCode = "400", description = "Datos inválidos")
        ]
    )
    @PostMapping
    fun crearProducto(
        @Valid @RequestBody producto: Producto
    ): ResponseEntity<Producto> {

        val imagenLimpia = producto.imagen.substringAfterLast("/")

        val nuevo = producto.copy(imagen = imagenLimpia)

        return ResponseEntity.ok(productoService.crear(nuevo))
    }

    @Operation(
        summary = "Actualizar producto",
        description = "Actualiza un producto existente identificado por su ID."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Producto actualizado"),
            ApiResponse(responseCode = "404", description = "Producto no encontrado"),
            ApiResponse(responseCode = "400", description = "Datos inválidos")
        ]
    )
    @PutMapping("/{id}")
    fun actualizarProducto(
        @Parameter(description = "ID del producto a actualizar", example = "1")
        @PathVariable id: Long,
        @Valid @RequestBody producto: Producto
    ): ResponseEntity<Producto> {

        val imagenLimpia = producto.imagen.substringAfterLast("/")

        val actualizado = productoService.actualizar(id, producto.copy(imagen = imagenLimpia))

        return actualizado?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()
    }

    @Operation(
        summary = "Eliminar producto",
        description = "Elimina un producto por su ID."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Producto eliminado (o no existía)"),
            ApiResponse(responseCode = "400", description = "ID inválido")
        ]
    )
    @DeleteMapping("/{id}")
    fun eliminarProducto(
        @Parameter(description = "ID del producto a eliminar", example = "1")
        @PathVariable id: Long
    ): ResponseEntity<Void> {
        productoService.eliminar(id)
        return ResponseEntity.noContent().build()
    }

    @Operation(
        summary = "Crear producto con imagen (multipart/form-data)",
        description = "Crea un producto recibiendo los campos del formulario y un archivo de imagen."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Producto creado con imagen"),
            ApiResponse(responseCode = "400", description = "Datos o archivo inválido")
        ]
    )
    @PostMapping("/crear-con-imagen", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun crearProductoConImagen(
        @Parameter(description = "Nombre del producto", example = "Mazo Preconstruido Fuego")
        @RequestParam nombre: String,

        @Parameter(description = "Marca del producto", example = "Magic East")
        @RequestParam marca: String?,

        @Parameter(description = "Categorías del producto separadas por coma", example = "Preconstruidos,Mazos")
        @RequestParam categorias: String,

        @Parameter(description = "Precio del producto en CLP", example = "25990")
        @RequestParam precio: Int,

        @Parameter(description = "Stock disponible", example = "10")
        @RequestParam stock: Int,

        @Parameter(description = "Descripción del producto", example = "Mazo temático con criaturas de fuego.")
        @RequestParam descripcion: String,

        @Parameter(
            description = "Archivo de imagen asociado al producto",
            required = true
        )
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

    @Operation(
        summary = "Obtener imagen de producto",
        description = "Devuelve el archivo de imagen asociado al producto por su nombre."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Imagen encontrada"),
            ApiResponse(responseCode = "404", description = "Imagen no encontrada")
        ]
    )
    @GetMapping("/imagenes/{nombre}")
    fun obtenerImagen(
        @Parameter(description = "Nombre del archivo de imagen", example = "precon1.png")
        @PathVariable nombre: String
    ): ResponseEntity<ByteArray> {

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
