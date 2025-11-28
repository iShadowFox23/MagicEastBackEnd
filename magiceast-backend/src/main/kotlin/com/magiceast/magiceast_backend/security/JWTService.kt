package com.magiceast.magiceast_backend.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*

@Service
class JwtService(

    @Value("\${jwt.secret}")
    private val secret: String,

    @Value("\${jwt.expiration-ms}")
    private val expirationMs: Long
) {

    private fun getSigningKey() =
        Keys.hmacShaKeyFor(secret.toByteArray())

    fun generarToken(username: String): String {
        val ahora = Date()
        val expiracion = Date(ahora.time + expirationMs)

        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(ahora)
            .setExpiration(expiracion)
            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
            .compact()
    }

    fun obtenerUsername(token: String): String? =
        try {
            val claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .body

            claims.subject
        } catch (e: Exception) {
            null
        }

    fun esTokenValido(token: String): Boolean =
        try {
            val claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
            !claims.body.expiration.before(Date())
        } catch (e: Exception) {
            false
        }
}
