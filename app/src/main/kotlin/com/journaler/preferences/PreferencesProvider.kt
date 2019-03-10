package com.journaler.preferences

import android.content.Context
import android.content.SharedPreferences

class PreferencesProvider: PreferencesProviderAbstract() {
    override fun obtain(configuration: PreferencesConfiguration,
                        ctx: Context): SharedPreferences
            = ctx.getSharedPreferences(configuration.key , configuration.mode)
}