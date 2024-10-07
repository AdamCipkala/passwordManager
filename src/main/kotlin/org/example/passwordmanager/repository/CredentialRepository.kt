package org.example.passwordmanager.repository

import Credential
import org.springframework.data.jpa.repository.JpaRepository

interface CredentialRepository : JpaRepository<Credential, Long>