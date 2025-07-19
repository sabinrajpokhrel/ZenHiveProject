package com.example.zenhive.model

data class UserModel(
    val uid: String = "",
    val displayName: String? = "",
    val email: String? = "",
    val birthdate: String = "",
    val photoUrl: String? = "",
    var password: String = "", // mutable for Firebase integration
    val instagram: String = "",
    val spotify: String = "",
    val bio: String = "",
    val interests: List<String> = emptyList()
)
