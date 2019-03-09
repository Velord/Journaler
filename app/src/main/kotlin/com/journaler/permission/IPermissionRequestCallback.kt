package com.journaler.permission

interface IPermissionRequestCallback {
    fun onPermissionGranted(permissions: List<String>)
    fun onPermissionDenied(permissions: List<String>)
}