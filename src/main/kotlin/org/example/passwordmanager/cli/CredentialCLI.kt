package org.example.passwordmanager.cli

import org.example.passwordmanager.model.User
import org.example.passwordmanager.service.CredentialService
import org.example.passwordmanager.service.UserService
import org.example.passwordmanager.util.PasswordGeneratorUtil
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import java.util.*

@Component
class CredentialCLI(
    private val credentialService: CredentialService,
    private val userService: UserService
) : CommandLineRunner {
    override fun run(vararg args: String?) {
        val scanner = Scanner(System.`in`)

        var user: User?
        var masterPassword: String

        println("Welcome to your Password Manager!")
        println("Do you want to (1) Log in or (2) Create an account? (Enter 1 or 2)")

        when (scanner.nextLine()) {
            "1" -> {
                print("Enter your username: ")
                val username = scanner.nextLine()

                user = userService.getUserByUsername(username)
                if (user == null) {
                    println("No account found for $username. Exiting.")
                    return
                }

                println("Enter your master password:")
                masterPassword = scanner.nextLine()

                if (!userService.verifyPassword(username, masterPassword)) {
                    println("Incorrect master password. Exiting.")
                    return
                }

                println("Login successful!")
            }

            "2" -> {
                print("Enter a username: ")
                val username = scanner.nextLine()

                user = userService.getUserByUsername(username)
                if (user != null) {
                    println("An account already exists for $username. Exiting.")
                    return
                }

                println("Enter a new master password:")
                val newMasterPassword = scanner.nextLine()

                if (newMasterPassword.isBlank()) {
                    println("Master password cannot be empty. Exiting.")
                    return
                }

                user = userService.createUser(username, newMasterPassword)
                masterPassword = newMasterPassword
                println("Account created successfully!")
            }

            else -> {
                println("Invalid option. Exiting.")
                return
            }
        }

        println("Welcome to your Password Manager!")
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

                    println("Do you want to (1) generate a strong password or (2) input a password manually? (Enter 1 or 2)")
                    val password: String = when (scanner.nextLine()) {
                        "1" -> {
                            val generatedPassword = PasswordGeneratorUtil.generatePassword()
                            println("Generated strong password: $generatedPassword")
                            generatedPassword
                        }

                        "2" -> {
                            println("Enter the password: ")
                            scanner.nextLine()
                        }

                        else -> {
                            println("Invalid option. Defaulting to generating a strong password.")
                            val generatedPassword = PasswordGeneratorUtil.generatePassword()
                            println("Generated strong password: $generatedPassword")
                            generatedPassword
                        }
                    }

                    credentialService.saveCredential(service, username, password, masterPassword, user)
                    println("Credential saved successfully!")
                }

                "2" -> {
                    val credentials = credentialService.getAllUserCredentials(user)
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
                        val password = credentialService.getDecryptedPassword(it, masterPassword, user)
                        if (password != null) {
                            println("Password: $password")
                        } else {
                            println("Incorrect master password.")
                        }
                    } ?: println("Invalid ID.")
                }

                "4" -> {
                    print("Enter the ID of the credential to delete: ")
                    val id = scanner.nextLine().toLongOrNull()
                    id?.let {
                        credentialService.deleteCredential(it, user)
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
