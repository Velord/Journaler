package com.journaler.api

object BackendServiceHeaderMap {
    fun obtain(autorization: Boolean = false): Map<String, String>{
        val map = mutableMapOf(
            Pair("Accept", "*/*"),
            Pair("Content-type", "application/json; charset=UTF-8")
        )
        if (autorization)
            map["Autorization"] =
                "Bearer $[TokenManager.currentToken.token]"
        return map
    }
}