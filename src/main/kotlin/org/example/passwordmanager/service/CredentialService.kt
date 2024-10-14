package org.example.passwordmanager.service

import org.example.passwordmanager.model.Credential
import org.example.passwordmanager.repository.CredentialRepository
import org.example.passwordmanager.util.EncryptionUtil
import org.springframework.stereotype.Service
import java.util.*

@Service
class CredentialService(private val credentialRepository: CredentialRepository) {

    fun saveCredential(service: String, username: String, plainPassword: String, masterPassword: String) {
        val salt = generateSalt()

        val encryptedPassword = EncryptionUtil.encrypt(plainPassword, masterPassword, salt)

        val credential = Credential(
            service = service,
            username = username,
            encryptedPassword = encryptedPassword,
            salt = Base64.getEncoder().encodeToString(salt)
        )
        credentialRepository.save(credential)
    }

    fun getAllCredentials(): List<Credential> {
        return credentialRepository.findAll().toList()
    }

    fun getDecryptedPassword(id: Long, masterPassword: String): String? {
        val credential = credentialRepository.findById(id)
        if (credential.isPresent) {
            val encryptedPassword = credential.get().encryptedPassword
            val salt = Base64.getDecoder().decode(credential.get().salt)

            return try {
                EncryptionUtil.decrypt(encryptedPassword, masterPassword, salt)
            } catch (e: Exception) {
                null
            }
        }
        return null
    }
    fun deleteCredential(credentialId: Long) {
        credentialRepository.deleteById(credentialId)
    }

    private fun generateSalt(): ByteArray {
        val salt = ByteArray(16)
        Random().nextBytes(salt)
        return salt
    }
}
