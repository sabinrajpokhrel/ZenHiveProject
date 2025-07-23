package com.example.zenhive.repository

import com.example.zenhive.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

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

    override suspend fun login(email: String, password: String): UserModel? = suspendCoroutine { cont ->
        val database = FirebaseDatabase.getInstance().getReference("users")
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (userSnapshot in snapshot.children) {
                    val user = userSnapshot.getValue(UserModel::class.java)
                    if (user != null && user.email == email && user.password == password) {
                        cont.resume(user)
                        return
                    }
                }
                cont.resume(null) // Not found
            }


            override fun onCancelled(error: DatabaseError) {
                cont.resumeWithException(error.toException())
            }
        })
    }


    override suspend fun getUserSnapshotByUid(uid: String): DataSnapshot? {
        return try {
            FirebaseDatabase.getInstance()
                .getReference("users")
                .child(uid)
                .get()
                .await()
        } catch (e: Exception) {
            null
        }
    }



}
