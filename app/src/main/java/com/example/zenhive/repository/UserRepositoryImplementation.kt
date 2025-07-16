package com.example.zenhive.repository

import com.example.zenhive.model.UserModel
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.tasks.await

class UserRepositoryImplementation : UserRepository {
    private val dbRef = FirebaseDatabase.getInstance().getReference("users")

    override suspend fun createUser(user: UserModel) {
        withContext(Dispatchers.IO) {
            try {
                // Ensure we're storing the complete user object including password
                dbRef.child(user.uid).setValue(user).await()
            } catch (e: Exception) {
                throw e
            }
        }
    }

    override suspend fun getUserByUid(uid: String): UserModel? {
        return withContext(Dispatchers.IO) {
            try {
                val snapshot = dbRef.child(uid).get().await()
                snapshot.getValue(UserModel::class.java)
            } catch (e: Exception) {
                null
            }
        }
    }

    override suspend fun updateUserPassword(uid: String, newPassword: String) {
        withContext(Dispatchers.IO) {
            try {
                // Get current user data
                val currentUser = getUserByUid(uid)
                if (currentUser != null) {
                    // Update the user with new password
                    val updatedUser = currentUser.copy(password = newPassword)
                    dbRef.child(uid).setValue(updatedUser).await()
                }
            } catch (e: Exception) {
                throw e
            }
        }
    }
}