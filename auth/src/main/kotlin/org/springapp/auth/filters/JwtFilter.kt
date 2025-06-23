package org.springapp.auth.filters

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import java.security.interfaces.RSAPublicKey

class JwtFilter(
    private val issuer: String,
    private val publicKey: RSAPublicKey,
) : OncePerRequestFilter() {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val header = request.getHeader("Authorization")
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response)
            return
        }

        val token =  header.removePrefix("Bearer ")

        try {
            val verifier = JWT.require(Algorithm.RSA256(publicKey, null))
                .withIssuer(issuer)
                .build()
            val jwt = verifier.verify(token)
            logger.info("JWT verified: ${jwt.subject}")
            val roles =  jwt.getClaim("roles").asList(String::class.java)
            val authorities = roles?.map { SimpleGrantedAuthority(it) }?.toSet() ?: emptySet()
            val auth = UsernamePasswordAuthenticationToken(token, null, authorities)
            SecurityContextHolder.getContext().authentication = auth

            filterChain.doFilter(request, response)
        } catch (e : Exception) {
            logger.warn("Invalid authorization header, ignoring request")
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")
            return
        }
    }

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        return request.requestURI.startsWith("/api/auth")
    }
}