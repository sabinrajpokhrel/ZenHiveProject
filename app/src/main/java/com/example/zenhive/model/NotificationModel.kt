package com.example.zenhive.model

data class NotificationModel(
    val id: String = "", // unique notification id
    val toUid: String = "", // recipient user id
    val fromUid: String = "", // sender user id
    val fromDisplayName: String = "", // sender display name
    val hiveId: String = "", // related hive id
    val hiveTitle: String = "", // related hive title
    val type: String = "invite", // e.g. "invite"
    val message: String = "", // notification message
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false
)
