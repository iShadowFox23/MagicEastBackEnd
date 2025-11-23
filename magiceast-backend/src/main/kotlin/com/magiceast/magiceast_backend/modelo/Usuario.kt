package com.magiceast.magiceast_backend.modelo

import jakarta.persistence.*

@Entity
@Table(name = "USERS")
data class Usuario(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val nombre: String,

    @Column(nullable = false, unique = true)
    val email: String,

    @Column(nullable = false)
    val direccion: String,

    @Column(nullable = false)
    val contrasena: String
)
