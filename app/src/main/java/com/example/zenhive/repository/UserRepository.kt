package com.example.zenhive.repository

import com.example.zenhive.model.UserModel

interface UserRepository {
    suspend fun createUser(user: UserModel)
    suspend fun getUserByUid(uid: String): UserModel?
    suspend fun updateUserPassword(uid: String, newPassword: String)
}