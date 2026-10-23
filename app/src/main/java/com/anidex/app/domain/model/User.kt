package com.anidex.app.domain.model

data class User(
    val id: String,
    val name: String,
    val email: String,
    val avatarUrl: String? = null,
    val bio: String = "Anime Enthusiast & Tracker",
    val joinedDate: String = "October 2026"
)

