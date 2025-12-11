package com.magiceast.magiceast_backend.modelo

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.persistence.*
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size

@Schema(
    description = "Representa un producto disponible en Magic East (mazos, accesorios, etc.)."
)
@Entity
@Table(name = "PRODUCTO")
data class Producto(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    @field:Schema(
        description = "Identificador único del producto.",
        example = "1",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    val id: Long = 0,

    @field:NotBlank(message = "El nombre no puede estar vacío")
    @field:Size(max = 255, message = "El nombre no puede tener más de 255 caracteres")
    @Column(name = "NOMBRE", nullable = false, length = 255)
    @field:Schema(
        description = "Nombre comercial del producto.",
        example = "Mazo Intro Azul"
    )
    val nombre: String,

    @field:Size(max = 255, message = "La marca no puede tener más de 255 caracteres")
    @Column(name = "MARCA", nullable = true, length = 255)
    @field:Schema(
        description = "Marca del producto (opcional).",
        example = "Magic East",
        nullable = true
    )
    val marca: String? = null,

    @field:Size(max = 255, message = "El set no puede tener más de 255 caracteres")
    @Column(name = "SET_NAME", nullable = true, length = 255)
    @field:Schema(
        description = "Nombre del set o expansión (opcional).",
        example = "Commander 2025",
        nullable = true
    )
    val set: String? = null,

    @field:NotBlank(message = "Las categorías no pueden estar vacías")
    @field:Size(max = 500, message = "Las categorías no pueden tener más de 500 caracteres")
    @Column(name = "CATEGORIAS", nullable = false, length = 500)
    @field:Schema(
        description = "Categorías separadas por coma a las que pertenece el producto.",
        example = "Preconstruidos,Mazos"
    )
    val categorias: String,

    @field:Positive(message = "El precio debe ser mayor a 0")
    @Column(name = "PRECIO", nullable = false)
    @field:Schema(
        description = "Precio del producto en pesos chilenos.",
        example = "25990",
        minimum = "1"
    )
    val precio: Int,

    @field:Min(value = 0, message = "El stock no puede ser negativo")
    @Column(name = "STOCK", nullable = false)
    @field:Schema(
        description = "Cantidad disponible en stock.",
        example = "15",
        minimum = "0"
    )
    val stock: Int,

    @field:NotBlank(message = "La descripción no puede estar vacía")
    @field:Size(max = 2000, message = "La descripción no puede tener más de 2000 caracteres")
    @Column(name = "DESCRIPCION", nullable = false, length = 2000)
    @field:Schema(
        description = "Descripción detallada del producto.",
        example = "Mazo preconstruido azul ideal para jugadores nuevos."
    )
    val descripcion: String,

    @field:NotBlank(message = "La ruta de la imagen no puede estar vacía")
    @field:Size(max = 500, message = "La ruta de la imagen no puede tener más de 500 caracteres")
    @Column(name = "IMAGEN", nullable = false, length = 500)
    @field:Schema(
        description = "Nombre o ruta relativa de la imagen asociada al producto.",
        example = "precon1.png"
    )
    val imagen: String
)
