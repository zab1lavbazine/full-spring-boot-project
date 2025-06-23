package org.springapp.auth.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component
import java.math.BigInteger
import java.net.URL
import java.security.KeyFactory
import java.security.interfaces.RSAPublicKey
import java.security.spec.RSAPublicKeySpec
import java.util.Base64


@Component
class KeycloakPublicKeyProvider(
    private val keycloakInfo: KeycloakInfo,
) {

    fun fetchPublicKey(): RSAPublicKey {
        val json = URL("${keycloakInfo.url}/protocol/openid-connect/certs").readText()
        val keys = ObjectMapper().readTree(json).get("keys")
        val key = keys[0] // Simplification: just pick first key
        val n = key.get("n").asText()
        val e = key.get("e").asText()

        val modulus = BigInteger(1, Base64.getUrlDecoder().decode(n))
        val exponent = BigInteger(1, Base64.getUrlDecoder().decode(e))
        val spec = RSAPublicKeySpec(modulus, exponent)
        return KeyFactory.getInstance("RSA").generatePublic(spec) as RSAPublicKey
    }

    fun getIssuer() : String = keycloakInfo.url
}