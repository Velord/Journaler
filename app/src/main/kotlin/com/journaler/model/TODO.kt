package com.journaler.model

import android.location.Location
import com.journaler.database.DbEntry

class TODO(
    title: String ,
    message: String,
    location: Location,
    var scheduledFor: Long
): DbEntry(title , message , location) {
    override var id: Long = 0L
}