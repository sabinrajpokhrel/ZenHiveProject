package com.example.zenhive.repository

import com.example.zenhive.model.UserModel
import com.google.firebase.database.DataSnapshot

interface UserRepository {
    suspend fun createUser(user: UserModel)
    suspend fun getUserByUid(uid: String): UserModel?
    suspend fun updateUserPassword(uid: String, newPassword: String)

    // New functions for updating and retrieving profile info
    suspend fun updateUserProfile(uid: String, updatedUser: UserModel)
    suspend fun updateUserBio(uid: String, bio: String)
    suspend fun updateUserInterests(uid: String, interests: List<String>)
    suspend fun firebaseAuthWithGoogle(idToken: String): UserModel?



    suspend fun getUserSnapshotByUid(uid: String): DataSnapshot?


    suspend fun login(email: String, password: String): UserModel?

}
