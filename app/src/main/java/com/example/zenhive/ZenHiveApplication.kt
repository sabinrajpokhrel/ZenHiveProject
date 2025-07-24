package com.example.zenhive
import android.app.Application
import im.zego.zegoexpress.ZegoExpressEngine
import im.zego.zegoexpress.constants.ZegoScenario
import im.zego.zegoexpress.callback.IZegoEventHandler

class ZenHiveApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        android.util.Log.d("ZenHiveApplication", "ZenHiveApplication onCreate called")
        val appID: Long = 815743631 // Your App ID
        val appSign: String = "63508d7f3df0ff0febf37aa449f1ce4de69686ede8386e5de944c2d84e0e6d04" // Your App Sign
        val isTestEnv = true // Use true for testing, false for production
        val scenario = ZegoScenario.LIVE
        val appContext = applicationContext

        ZegoExpressEngine.createEngine(
            appID,
            appSign,
            isTestEnv,
            scenario,
            this,
            object : IZegoEventHandler() {}
        )
        android.util.Log.d("ZenHiveApplication", "ZegoExpressEngine.createEngine called")
    }
}
