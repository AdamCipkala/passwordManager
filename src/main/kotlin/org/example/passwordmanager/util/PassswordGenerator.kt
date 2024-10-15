package org.example.passwordmanager.util

import java.security.SecureRandom

object PasswordGeneratorUtil {
    private const val LOWERCASE = "abcdefghijklmnopqrstuvwxyz"
    private const val UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    private const val DIGITS = "0123456789"
    private const val SPECIAL_CHARACTERS = "!@#\$%^&*()-_+=<>?/{}[]~"

    private val random = SecureRandom()

    fun generatePassword(length: Int = 16, includeSpecialChars: Boolean = true): String {
        val characterPool = StringBuilder().apply {
            append(LOWERCASE)
            append(UPPERCASE)
            append(DIGITS)
            if (includeSpecialChars) append(SPECIAL_CHARACTERS)
        }.toString()

        return (1..length)
            .map { characterPool[random.nextInt(characterPool.length)] }
            .joinToString("")
    }
}
