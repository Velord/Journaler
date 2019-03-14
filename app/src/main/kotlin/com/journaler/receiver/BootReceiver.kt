package com.journaler.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class BootReceiver: BroadcastReceiver() {
    private val tag = "Boot receiver"

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i(tag , "Boot completed")
        // Perform your on boot stuff here.
    }
}