package org.springapp.auth.controllers

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/hello")
class HelloWorldController {

    @GetMapping
    fun hello(): ResponseEntity<String> {
        return  ResponseEntity.ok("Hello, Kotlin")
    }
}