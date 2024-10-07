package org.example.passwordmanager

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PasswordManagerApplication

fun main(args: Array<String>) {
    runApplication<PasswordManagerApplication>(*args)
}
