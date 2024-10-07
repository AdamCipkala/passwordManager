package org.example.passwordmanager.repository

import org.example.passwordmanager.model.Credential
import org.springframework.data.jpa.repository.JpaRepository


interface CredentialRepository : JpaRepository<Credential, Long>
