package com.example.zenhive.repository

import com.example.zenhive.model.HiveModel
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class HiveRepositoryImplementation : HiveRepository {

    private val db = FirebaseDatabase.getInstance().reference
    private val hiveRef = db.child("hives")

    override suspend fun createHive(hive: HiveModel) {
        hiveRef.child(hive.hiveId).setValue(hive).await()
    }

    override suspend fun updateHiveStatus(hiveId: String, isLive: Boolean) {
        hiveRef.child(hiveId).child("isLive").setValue(isLive).await()
    }

    override suspend fun getLiveHives(): List<HiveModel> {
        val snapshot = hiveRef.get().await()
        val liveList = mutableListOf<HiveModel>()
        for (hiveSnap in snapshot.children) {
            val hive = hiveSnap.getValue(HiveModel::class.java)
            if (hive != null && hive.live) {
                liveList.add(hive)
            }
        }
        return liveList
    }

    override suspend fun getUserHives(uid: String): List<HiveModel> {
        val snapshot = hiveRef.get().await()
        val userList = mutableListOf<HiveModel>()
        for (hiveSnap in snapshot.children) {
            val hive = hiveSnap.getValue(HiveModel::class.java)
            if (hive != null && hive.hostUid == uid) {
                userList.add(hive)
            }
        }
        return userList.sortedByDescending { it.timestamp }
    }
}
