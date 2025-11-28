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
                    .requestMatchers("/auth/**").permitAll()    // login y register
                    .requestMatchers("/h2-console/**").permitAll()
                    .requestMatchers("/api/**").permitAll()     // <-- luego lo ajustas
                    .anyRequest().authenticated()
            }
            .headers { headers ->
                headers.frameOptions { it.disable() }
            }
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)
            .httpBasic(Customizer.withDefaults())

        return http.build()
    }
}
