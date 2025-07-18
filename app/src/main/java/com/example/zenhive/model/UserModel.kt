package com.example.zenhive.model

data class UserModel(
    val uid: String = "",
    val displayName: String? = null,
    val email: String? = null,
    val birthdate: String? = null,
    val photoUrl: String? = null,
    var password: String? = null // mutable for Firebase integration
)
