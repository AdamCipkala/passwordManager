package org.example.passwordmanager.model

import jakarta.persistence.*

@Entity
data class Credential(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val service: String,
    val username: String,

    val encryptedPassword: String,

    val salt: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    var user: User? = null
)