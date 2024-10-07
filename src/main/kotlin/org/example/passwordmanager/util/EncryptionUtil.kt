package org.example.passwordmanager.util

import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

object EncryptionUtil {
    private const val ALGORITHM = "AES/GCM/NoPadding"
    private const val IV_LENGTH = 12

    private val keySpec: SecretKey = generateSecretKey()

    fun encrypt(plainText: String): String {
        val cipher = Cipher.getInstance(ALGORITHM)
        val iv = ByteArray(IV_LENGTH)
        java.security.SecureRandom().nextBytes(iv)
        val parameterSpec = GCMParameterSpec(128, iv)

        cipher.init(Cipher.ENCRYPT_MODE, keySpec, parameterSpec)
        val encryptedBytes = cipher.doFinal(plainText.toByteArray())

        return Base64.getEncoder().encodeToString(iv + encryptedBytes)
    }

    fun decrypt(encryptedText: String): String {
        val cipher = Cipher.getInstance(ALGORITHM)
        val decodedBytes = Base64.getDecoder().decode(encryptedText)
        val iv = decodedBytes.sliceArray(0 until IV_LENGTH)
        val encryptedBytes = decodedBytes.sliceArray(IV_LENGTH until decodedBytes.size)

        val parameterSpec = GCMParameterSpec(128, iv)

        cipher.init(Cipher.DECRYPT_MODE, keySpec, parameterSpec)
        val plainBytes = cipher.doFinal(encryptedBytes)

        return String(plainBytes)
    }

    private fun generateSecretKey(): SecretKey {
        val keyGenerator = KeyGenerator.getInstance("AES")
        keyGenerator.init(256)
        return keyGenerator.generateKey()
    }
}
