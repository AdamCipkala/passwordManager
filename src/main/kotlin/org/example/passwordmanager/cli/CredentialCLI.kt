package org.example.passwordmanager.cli

import org.example.passwordmanager.service.CredentialService
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import java.util.*

@Component
class CredentialCLI(private val credentialService: CredentialService) : CommandLineRunner {
    override fun run(vararg args: String?) {
        val scanner = Scanner(System.`in`)
        val console = System.console()
        println("Welcome to your Password Manager!")
        loop@ while (true) {
            println("Choose an option:")
            println("1. Add a new credential")
            println("2. List all credentials")
            println("3. View a password")
            println("4. Delete a credential")
            println("5. Exit")

            when (scanner.nextLine()) {
                "1" -> {
                    print("Enter the service name: ")
                    val service = scanner.nextLine()

                    print("Enter the username: ")
                    val username = scanner.nextLine()

                    val password: String = if (console != null) {
                        print("Enter the password: ")
                        String(console.readPassword())
                    } else {

                        println("Enter the password (warning: input will be visible):")
                        scanner.nextLine()
                    }

                    credentialService.saveCredential(service, username, password)
                    println("Credential saved successfully!")
                }
                "2" -> {
                    val credentials = credentialService.getAllCredentials()
                    if (credentials.isNotEmpty()) {
                        credentials.forEach {
                            println("ID: ${it.id}, Service: ${it.service}, Username: ${it.username}")
                        }
                    } else {
                        println("No credentials stored.")
                    }
                }
                "3" -> {
                    print("Enter the ID of the credential to view the password: ")
                    val id = scanner.nextLine().toLongOrNull()
                    id?.let {
                        val password = credentialService.getDecryptedPassword(it)
                        println("Password: $password")
                    } ?: println("Invalid ID.")
                }
                "4" -> {
                    print("Enter the ID of the credential to delete: ")
                    val id = scanner.nextLine().toLongOrNull()
                    id?.let {
                        credentialService.deleteCredential(it)
                        println("Credential deleted.")
                    } ?: println("Invalid ID.")
                }
                "5" -> break@loop
                else -> println("Invalid option. Please choose a valid action.")
            }
        }

        println("Exiting the Password Manager. Goodbye!")
    }
}