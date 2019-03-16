package com.journaler.api

data class UserLoginRequest(
    val userName: String,
    val password: String
) {
}