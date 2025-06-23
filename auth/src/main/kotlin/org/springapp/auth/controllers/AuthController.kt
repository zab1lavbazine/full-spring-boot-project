package org.springapp.auth.controllers

import org.slf4j.LoggerFactory
import org.springapp.auth.configuration.ClientData
import org.springapp.auth.configuration.KeycloakInfo
import org.springapp.auth.data.LoginRequest
import org.springapp.auth.data.TokenResponse
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate


@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val clientData: ClientData,
    private val restTemplate: RestTemplate,
    private val keycloak: KeycloakInfo
) {

    private val logger = LoggerFactory.getLogger(AuthController::class.java)


    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<Any> {
        val params = LinkedMultiValueMap<String, String>().apply {
            add("client_id", clientData.clientId)
            add("client_secret", clientData.clientSecret)
            add("username", request.username)
            add("password", request.password)
            add("grant_type", "password")
        }

        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_FORM_URLENCODED
        }

        val httpEntity = HttpEntity(params, headers)
        logger.info("http entity: $httpEntity")

        return try {
            val response = restTemplate.postForEntity(
                "${keycloak.url}/protocol/openid-connect/token",
                httpEntity,
                TokenResponse::class.java
            )
            logger.info("Response: $response")
            ResponseEntity.ok(response.body)
        } catch (ex: HttpClientErrorException) {
            ResponseEntity.status(ex.statusCode).body(mapOf("error" to ex.statusText))
        } catch (ex: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(mapOf("error" to "Login failed", "details" to ex.message))
        }
    }

}