package com.journaler.model

import android.location.Location
import android.os.Parcel
import android.os.Parcelable
import com.journaler.database.DbEntry

class Note(
    title: String,
    message: String,
    location: Location
): DbEntry(title , message , location), Parcelable {
    override var id: Long = 0L

    constructor(parcel: Parcel): this(
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(Location::class.java.classLoader)
    ){
        id = parcel.readLong()
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest!!.writeString(title)
        dest.writeString(message)
        dest.writeParcelable(location , 0)
        dest.writeLong(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Note>{
        override fun createFromParcel(source: Parcel?): Note {
            return Note(source!!)
        }

        override fun newArray(size: Int): Array<Note?> {
            return arrayOfNulls(size)
        }
    }
}