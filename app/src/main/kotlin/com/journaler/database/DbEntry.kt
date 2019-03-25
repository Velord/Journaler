package com.journaler.database

import android.location.Location

abstract class DbEntry(
    var title: String,
    var message: String,
    var location: Location
): DbModel()