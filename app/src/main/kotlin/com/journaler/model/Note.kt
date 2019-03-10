package com.journaler.model

import android.location.Location
import com.journaler.database.DbEntry

class Note(
    title: String,
    message: String,
    location: Location
): DbEntry(title , message , location) {
    override var id: Long = 0L
}