package org.springapp.auth.configuration

import org.springframework.boot.context.properties.ConfigurationProperties


@ConfigurationProperties(prefix = "client-data")
data class ClientData(
    val clientId: String,
    val clientSecret: String
)