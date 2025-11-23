package com.magiceast.magiceast_backend.modelo

import jakarta.persistence.*

@Entity
@Table(name = "PRODUCTO")
data class Producto(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    val id: Long = 0,

    @Column(name = "NOMBRE", nullable = false)
    val nombre: String,

    @Column(name = "MARCA", nullable = false)
    val marca: String,

    //En un futuro, quizas, cambiarlo a lista?
    @Column(name = "CATEGORIAS", nullable = false)
    val categorias: String,

    @Column(name = "PRECIO", nullable = false)
    val precio: Int,

    @Column(name = "STOCK", nullable = false)
    val stock: Int,

    @Column(name = "DESCRIPCION", nullable = false, length = 2000)
    val descripcion: String
)