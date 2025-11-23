package com.magiceast.magiceast_backend.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
class SecurityConfig {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            // Desactivar CSRF para que no moleste en desarrollo
            .csrf { it.disable() }
            // Qué rutas están permitidas sin login
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers("/h2-console/**").permitAll()
                    .requestMatchers("/api/**").permitAll()
                    .anyRequest().permitAll()
            }
            // Necesario para que H2 console funcione dentro de un <frame>
            .headers { headers ->
                headers.frameOptions { frame ->
                    frame.disable()
                }
            }
            // Desactivar el formLogin para que no salga el login por defecto
            .formLogin { it.disable() }
            .httpBasic(Customizer.withDefaults())

        return http.build()
    }
}