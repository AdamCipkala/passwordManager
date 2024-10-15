package org.example.passwordmanager.repository

import org.example.passwordmanager.model.Credential
import org.example.passwordmanager.model.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional


interface CredentialRepository : JpaRepository<Credential, Long> {
    fun findByIdAndUser(id: Long, user: User): Optional<Credential>
    fun findAllByUser(user: User): List<Credential>
    fun removeByIdAndUser(id: Long, user: User)
}

