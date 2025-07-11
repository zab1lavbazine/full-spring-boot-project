package org.springapp.auth.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate


@Configuration
class RestTemplateConguration {


    @Bean
    fun restTemplate(): RestTemplate {
        return RestTemplate()

    }
}