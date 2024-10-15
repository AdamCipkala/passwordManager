package org.example.passwordmanager.service

import org.example.passwordmanager.model.Credential
import org.example.passwordmanager.model.User
import org.example.passwordmanager.repository.CredentialRepository
import org.example.passwordmanager.util.EncryptionUtil
import org.springframework.stereotype.Service
import java.util.*

@Service
class CredentialService(
    private val credentialRepository: CredentialRepository,
) {

    fun saveCredential(service: String, username: String, plainPassword: String, masterPassword: String, user: User) {
        val salt = generateSalt()

        val encryptedPassword = EncryptionUtil.encrypt(plainPassword, masterPassword, salt)

        val credential = Credential(
            service = service,
            username = username,
            encryptedPassword = encryptedPassword,
            salt = Base64.getEncoder().encodeToString(salt),
            user = user
        )

        credentialRepository.save(credential)
    }

    fun getAllUserCredentials(user: User): List<Credential> {
        return credentialRepository.findAllByUser(user)
    }

    fun getDecryptedPassword(id: Long, masterPassword: String, user: User): String? {
        val credential = credentialRepository.findByIdAndUser(id, user)
        if (credential.isPresent) {
            val encryptedPassword = credential.get().encryptedPassword
            val salt = Base64.getDecoder().decode(credential.get().salt)
            return EncryptionUtil.decrypt(encryptedPassword, masterPassword, salt)
        }
        return null
    }

    fun deleteCredential(credentialId: Long, user: User) {
        credentialRepository.removeByIdAndUser(credentialId, user)
    }

    private fun generateSalt(): ByteArray {
        val salt = ByteArray(16)
        Random().nextBytes(salt)
        return salt
    }
}
