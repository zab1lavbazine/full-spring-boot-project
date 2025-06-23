package org.springapp.auth

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@EnableConfigurationProperties
@ConfigurationPropertiesScan("org.springapp.auth")
class AuthApplication

fun main(args: Array<String>) {
    runApplication<AuthApplication>(*args)
}
