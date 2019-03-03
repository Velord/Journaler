package com.journaler

import android.app.Application
import android.content.Context
import android.util.Log

class Journaler(): Application(){

    companion object {
        val TAG = "Journaler"
        var ctx: Context? = null
    }

    override fun onCreate() {
        super.onCreate()
        ctx = applicationContext
        Log.v(TAG , "[ON Create]")
    }

    override fun onLowMemory() {
        super.onLowMemory()
        Log.v(TAG ,"[ON Low Memory]")
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        Log.v(TAG,"[ON TRIM MEMORY]: $level")
    }
}