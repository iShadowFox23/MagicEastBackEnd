package com.magiceast.magiceast_backend.modelo

import jakarta.persistence.*

@Entity
@Table(name = "Usuarios")
data class Usuario(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    var nombre: String,

    @Column(nullable = false, unique = true)
    var email: String,

    @Column(nullable = false)
    var direccion: String,

    @Column(nullable = false)
    var contrasena: String
)

