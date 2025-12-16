package com.magiceast.magiceast_backend.config

import com.magiceast.magiceast_backend.security.JwtAuthFilter
import com.magiceast.magiceast_backend.security.UsuarioDetailsService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
class SecurityConfig(
    private val jwtAuthFilter: JwtAuthFilter,
    private val usuarioDetailsService: UsuarioDetailsService
) {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {

        http
            .csrf { it.disable() }
            .cors { }

        http
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }

        http
            .authorizeHttpRequests {
                it.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                it.requestMatchers("/auth/**").permitAll()
                it.requestMatchers("/v3/api-docs/**").permitAll()
                it.requestMatchers("/swagger-ui/**").permitAll()
                it.requestMatchers(HttpMethod.POST, "/api/usuarios").permitAll()
                it.requestMatchers(HttpMethod.GET, "/api/productos/**").permitAll()
                it.requestMatchers("/api/checkout/**").authenticated()
                it.requestMatchers("/api/ordenes/**").authenticated()
                it.anyRequest().hasRole("ADMIN")
            }

        http
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }

    @Bean
    fun authenticationProvider(): DaoAuthenticationProvider =
        DaoAuthenticationProvider(usuarioDetailsService).apply {
            setPasswordEncoder(passwordEncoder())
        }

    @Bean
    fun authenticationManager(
        config: AuthenticationConfiguration
    ): AuthenticationManager = config.authenticationManager

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val config = CorsConfiguration().apply {
            allowedOrigins = listOf("*")
            allowedMethods = listOf("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
            allowedHeaders = listOf("*")
            allowCredentials = false
        }

        return UrlBasedCorsConfigurationSource().apply {
            registerCorsConfiguration("/**", config)
        }
    }
}
