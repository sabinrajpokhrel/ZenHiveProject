package com.example.zenhive.repository

import com.example.zenhive.model.HiveModel

interface HiveRepository {
    suspend fun createHive(hive: HiveModel)
    suspend fun updateHiveStatus(hiveId: String, isLive: Boolean)
    suspend fun getLiveHives(): List<HiveModel>
    suspend fun getUserHives(uid: String): List<HiveModel>

}
