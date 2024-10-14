package org.example.passwordmanager.util

import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

object EncryptionUtil {
    private const val ALGORITHM = "AES/GCM/NoPadding"
    private const val IV_LENGTH = 12

    fun encrypt(plainText: String, password: String, salt: ByteArray): String {
        val cipher = Cipher.getInstance(ALGORITHM)
        val iv = ByteArray(IV_LENGTH)
        java.security.SecureRandom().nextBytes(iv)
        val parameterSpec = GCMParameterSpec(128, iv)
        val keySpec = deriveKey(password, salt)

        cipher.init(Cipher.ENCRYPT_MODE, keySpec, parameterSpec)
        val encryptedBytes = cipher.doFinal(plainText.toByteArray())

        return Base64.getEncoder().encodeToString(iv + encryptedBytes)
    }

    fun decrypt(encryptedText: String, password: String, salt: ByteArray): String {
        val cipher = Cipher.getInstance(ALGORITHM)
        val decodedBytes = Base64.getDecoder().decode(encryptedText)
        val iv = decodedBytes.sliceArray(0 until IV_LENGTH)
        val encryptedBytes = decodedBytes.sliceArray(IV_LENGTH until decodedBytes.size)
        val parameterSpec = GCMParameterSpec(128, iv)
        val keySpec = deriveKey(password, salt)

        cipher.init(Cipher.DECRYPT_MODE, keySpec, parameterSpec)
        val plainBytes = cipher.doFinal(encryptedBytes)

        return String(plainBytes)
    }

    private fun deriveKey(password: String, salt: ByteArray): SecretKey {
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val spec = PBEKeySpec(password.toCharArray(), salt, 65536, 256)
        val tmp = factory.generateSecret(spec)
        return SecretKeySpec(tmp.encoded, "AES")
    }

    fun hashPassword(password: String, salt: ByteArray): String {
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val spec = PBEKeySpec(password.toCharArray(), salt, 65536, 256)
        val tmp = factory.generateSecret(spec)
        return Base64.getEncoder().encodeToString(tmp.encoded)
    }

}
