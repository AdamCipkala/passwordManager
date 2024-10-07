package org.example.passwordmanager.service

import org.example.passwordmanager.model.Credential
import org.example.passwordmanager.repository.CredentialRepository
import org.example.passwordmanager.util.EncryptionUtil
import org.springframework.stereotype.Service

@Service
class CredentialService(private val credentialRepository: CredentialRepository) {

    fun saveCredential(service: String, username: String, plainPassword: String) {
        val encryptedPassword = EncryptionUtil.encrypt(plainPassword)
        val credential = Credential(service = service, username = username, encryptedPassword = encryptedPassword)
        credentialRepository.save(credential)
    }

    fun getAllCredentials(): List<Credential> {
        return credentialRepository.findAll().toList()
    }

    fun getDecryptedPassword(credentialId: Long): String? {
        val credential = credentialRepository.findById(credentialId).orElse(null)
        return credential?.let { EncryptionUtil.decrypt(it.encryptedPassword) }
    }

    fun deleteCredential(credentialId: Long) {
        credentialRepository.deleteById(credentialId)
    }
}
