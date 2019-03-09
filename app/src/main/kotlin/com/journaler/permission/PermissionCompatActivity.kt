package com.journaler.permission

import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

abstract  class PermissionCompatActivity: AppCompatActivity() {
    private val tag = "Permission extension"
    private val latestPermissionRequest  = AtomicInteger()
    private val permissionRequests = ConcurrentHashMap<Int , List<String>>()
    private val permissionCallbacks =
        ConcurrentHashMap<List<String> , IPermissionRequestCallback>()

    private val defaultPermissionCallback = object : IPermissionRequestCallback{
        override fun onPermissionDenied(permissions: List<String>) {
            Log.w(tag , "Permission denied [$permissions] , " +
                    "request : [$latestPermissionRequest]")
        }

        override fun onPermissionGranted(permissions: List<String>) {
            Log.i(tag , "Permission granted [$permissions]," +
                    " request : [$latestPermissionRequest]\"")
        }
    }

    fun requestPermissions(
        vararg permissions: String ,
        callback: IPermissionRequestCallback = defaultPermissionCallback
    ){
        val id = latestPermissionRequest.incrementAndGet()
        val items = mutableListOf<String>()
        items.addAll(permissions)
        permissionRequests[id] = items
        permissionCallbacks[items] = callback
        ActivityCompat.requestPermissions(this , permissions , id)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        val items = permissionRequests[requestCode]
        items?.let {
            val callbacks = permissionCallbacks[items]
            callbacks?.let {
                var success = true
                for (x in 0..grantResults.lastIndex){
                    val result = grantResults[x]
                    if (result != PackageManager.PERMISSION_GRANTED){
                        success = false
                        break
                    }
                }
                if (success)
                    callbacks.onPermissionGranted(items)
                else
                    callbacks.onPermissionDenied(items)
            }
        }
    }
}