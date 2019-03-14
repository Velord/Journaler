package com.journaler

import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import com.journaler.service.MainService

class Journaler(): Application(){

    companion object {
        val TAG = "Journaler"
        var ctx: Context? = null
    }

    override fun onCreate() {
        super.onCreate()
        ctx = applicationContext
        Log.v(TAG , "[ON CREATE]")
        startService()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        Log.w(TAG ,"[ON Low Memory]")
        // If we get low on memory we will stop service if running.
        stopService()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        Log.d(TAG,"[ON TRIM MEMORY]: $level")
    }

    private fun startService(){
        val serviceIntent = Intent(this , MainService::class.java)
        startService(serviceIntent)
    }

    private fun stopService(){
        val serviceIntent = Intent(this , MainService::class.java)
        stopService(serviceIntent)
    }
}