package com.magiceast.magiceast_backend.config

import com.magiceast.magiceast_backend.security.JwtAuthFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
class SecurityConfig(
    private val jwtAuthFilter: JwtAuthFilter
) {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {

        http
            .csrf { it.disable() }

            .authorizeHttpRequests { auth ->
                auth
                    // Endpoints públicos
                    .requestMatchers("/auth/**").permitAll()            // login
                    .requestMatchers("/api/usuarios/**").permitAll()     // registrar usuario
                    .requestMatchers("/h2-console/**").permitAll()
                    .requestMatchers("/api/productos").permitAll()
                    .requestMatchers("/api/productos/").permitAll()
                    .requestMatchers("/api/productos/*").permitAll()
                    .requestMatchers("/api/productos/**").permitAll()

                    // Todo lo demás requiere autenticación
                    .anyRequest().authenticated()
            }

            // Permite el H2 console
            .headers { headers ->
                headers.frameOptions { it.disable() }
            }

            // JWT filter
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)

            // Opción básica para pruebas
            .httpBasic(Customizer.withDefaults())

        return http.build()
    }
}
