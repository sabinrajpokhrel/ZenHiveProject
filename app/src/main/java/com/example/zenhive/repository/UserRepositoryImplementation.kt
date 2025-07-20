package com.example.zenhive.repository

import com.example.zenhive.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class UserRepositoryImplementation(
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
) : UserRepository {

    // CORRECTLY SPECIFIED DATABASE URL
    private val dbRef = FirebaseDatabase.getInstance("https://zenhive-7c32d-default-rtdb.firebaseio.com/")
        .getReference("users")

    override suspend fun createUser(user: UserModel) {
        withContext(Dispatchers.IO) {
            dbRef.child(user.uid).setValue(user).await()
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
            val currentUser = getUserByUid(uid)
            if (currentUser != null) {
                val updatedUser = currentUser.copy(password = newPassword)
                dbRef.child(uid).setValue(updatedUser).await()
            }
        }
    }

    override suspend fun updateUserProfile(uid: String, updatedUser: UserModel) {
        withContext(Dispatchers.IO) {
            dbRef.child(uid).setValue(updatedUser).await()
        }
    }

    override suspend fun updateUserBio(uid: String, bio: String) {
        withContext(Dispatchers.IO) {
            dbRef.child(uid).child("bio").setValue(bio).await()
        }
    }

    override suspend fun updateUserInterests(uid: String, interests: List<String>) {
        val userRef = FirebaseDatabase.getInstance().getReference("users").child(uid)
        userRef.child("interests").setValue(interests)
    }


    override suspend fun firebaseAuthWithGoogle(idToken: String): UserModel? {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val authResult = firebaseAuth.signInWithCredential(credential).await()
            val user = authResult.user
            user?.let {
                UserModel(
                    uid = it.uid,
                    displayName = it.displayName,
                    email = it.email,
                    photoUrl = it.photoUrl?.toString(),
                    password = "",
                    birthdate = "",
                    instagram = "",
                    spotify = "",
                    bio = "",
                    interests = emptyList()
                )
            }
        } catch (e: Exception) {
            null
        }
    }
}
