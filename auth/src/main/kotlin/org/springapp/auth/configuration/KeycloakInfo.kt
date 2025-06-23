package org.springapp.auth.configuration

import org.springframework.boot.context.properties.ConfigurationProperties


@ConfigurationProperties(prefix = "keycloak")
data class KeycloakInfo (
    val url: String
)