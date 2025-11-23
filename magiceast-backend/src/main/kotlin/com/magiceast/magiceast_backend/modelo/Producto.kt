package com.magiceast.magiceast_backend.modelo

import jakarta.persistence.*
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size

@Entity
@Table(name = "PRODUCTO")
data class Producto(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    val id: Long = 0,

    @field:NotBlank(message = "El nombre no puede estar vacío")
    @field:Size(max = 255, message = "El nombre no puede tener más de 255 caracteres")
    @Column(name = "NOMBRE", nullable = false, length = 255)
    val nombre: String,

    @field:NotBlank(message = "La marca no puede estar vacía")
    @field:Size(max = 255, message = "La marca no puede tener más de 255 caracteres")
    @Column(name = "MARCA", nullable = false, length = 255)
    val marca: String,

    // Ej: "Mazos,Intro Pack"
    @field:NotBlank(message = "Las categorías no pueden estar vacías")
    @field:Size(max = 500, message = "Las categorías no pueden tener más de 500 caracteres")
    @Column(name = "CATEGORIAS", nullable = false, length = 500)
    val categorias: String,

    @field:Positive(message = "El precio debe ser mayor a 0")
    @Column(name = "PRECIO", nullable = false)
    val precio: Int,

    @field:Min(value = 0, message = "El stock no puede ser negativo")
    @Column(name = "STOCK", nullable = false)
    val stock: Int,

    @field:NotBlank(message = "La descripción no puede estar vacía")
    @field:Size(max = 2000, message = "La descripción no puede tener más de 2000 caracteres")
    @Column(name = "DESCRIPCION", nullable = false, length = 2000)
    val descripcion: String,

    @field:NotBlank(message = "La ruta de la imagen no puede estar vacía")
    @field:Size(max = 500, message = "La ruta de la imagen no puede tener más de 500 caracteres")
    @Column(name = "IMAGEN", nullable = false, length = 500)
    val imagen: String
)