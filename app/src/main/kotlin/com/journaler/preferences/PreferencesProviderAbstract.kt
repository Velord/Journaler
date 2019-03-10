package com.journaler.preferences

import android.content.Context
import android.content.SharedPreferences

abstract class PreferencesProviderAbstract {
    abstract fun obtain(configuration: PreferencesConfiguration ,
                        ctx: Context):SharedPreferences
}