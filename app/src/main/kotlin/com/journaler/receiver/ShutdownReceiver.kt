package com.journaler.receiver

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class ShutdownReceiver: BroadcastReceiver() {
    private val tag  = "Shutdown receiver"

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i(tag , "Shutting down")
        // Perform your on cleanup stuff here.
    }
}