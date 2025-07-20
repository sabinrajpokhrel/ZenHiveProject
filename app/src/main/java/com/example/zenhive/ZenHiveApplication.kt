package com.example.zenhive

import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkManager

class ZenHiveApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Initialize WorkManager
        WorkManager.initialize(
            this,
            Configuration.Builder()
                .setMinimumLoggingLevel(android.util.Log.INFO)
                .build()
        )
    }
}
