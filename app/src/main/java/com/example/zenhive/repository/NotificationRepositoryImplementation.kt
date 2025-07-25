package com.example.zenhive.repository

import com.example.zenhive.model.NotificationModel
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class NotificationRepositoryImplementation {
    private val dbRef = FirebaseDatabase.getInstance().getReference("notifications")

    suspend fun sendNotification(notification: NotificationModel) {
        val notificationId = dbRef.child(notification.toUid).push().key ?: return
        val notificationWithId = notification.copy(id = notificationId)
        dbRef.child(notification.toUid).child(notificationId).setValue(notificationWithId).await()
    }

    suspend fun getNotificationsForUser(uid: String): List<NotificationModel> {
        val snapshot = dbRef.child(uid).get().await()
        return snapshot.children.mapNotNull { it.getValue(NotificationModel::class.java) }
    }

    suspend fun markNotificationAsRead(uid: String, notificationId: String) {
        dbRef.child(uid).child(notificationId).child("isRead").setValue(true).await()
    }

    suspend fun deleteNotification(uid: String, notificationId: String) {
        dbRef.child(uid).child(notificationId).removeValue().await()
    }
}

