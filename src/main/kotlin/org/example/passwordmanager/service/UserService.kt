package org.example.passwordmanager.service

import org.example.passwordmanager.model.User
import org.example.passwordmanager.repository.UserRepository
import org.example.passwordmanager.util.EncryptionUtil
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService(private val userRepository: UserRepository) {

    fun getUserByUsername(username: String): User? {
        return userRepository.findByUsername(username)
    }

    fun createUser(username: String, masterPassword: String) {
        val salt = generateSalt()
        val hashedPassword = EncryptionUtil.hashPassword(masterPassword, salt)

        val user = User(
            username = username,
            hashedPassword = hashedPassword,
            salt = Base64.getEncoder().encodeToString(salt)
        )
        userRepository.save(user)
    }

    fun verifyPassword(username: String, masterPassword: String): Boolean {
        val user = getUserByUsername(username) ?: return false
        val salt = Base64.getDecoder().decode(user.salt)
        val hashedPassword = EncryptionUtil.hashPassword(masterPassword, salt)
        return hashedPassword == user.hashedPassword
    }

    private fun generateSalt(): ByteArray {
        val salt = ByteArray(16)
        Random().nextBytes(salt)
        return salt
    }
}
