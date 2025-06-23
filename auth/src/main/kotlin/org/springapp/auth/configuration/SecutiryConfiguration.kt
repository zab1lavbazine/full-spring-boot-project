package org.springapp.auth.configuration

import org.springapp.auth.filters.JwtFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


@Configuration
@EnableWebSecurity
class SecurityConfiguration(
    private val keycloakPublicKeyProvider: KeycloakPublicKeyProvider
) {


    @Bean
    fun mainFilterChain(http: HttpSecurity): SecurityFilterChain {
        val publicKey = keycloakPublicKeyProvider.fetchPublicKey()
        val issuer = keycloakPublicKeyProvider.getIssuer()
        return http
            .csrf{ it.disable() }
            .authorizeHttpRequests { auth ->
                auth
                .requestMatchers("/api/auth/**").permitAll()
                .anyRequest().authenticated()
            }
            .addFilterBefore(
                JwtFilter( issuer, publicKey),
                UsernamePasswordAuthenticationFilter::class.java
            )
            .build()
    }
}