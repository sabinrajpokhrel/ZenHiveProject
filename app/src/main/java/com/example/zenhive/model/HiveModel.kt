package com.example.zenhive.model

data class HiveModel(
    val  hiveId: String = "",
    val title: String = "",
    val hostUid: String = "",
    val hostName: String = "",
    val timestamp: Long = 0L,
    val live: Boolean = false,
    val participants: List<String> = emptyList()
)
