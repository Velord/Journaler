package com.journaler.model

import android.os.Parcel
import android.os.Parcelable

class Dummy2(
    private var count: Int
) : Parcelable {

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Dummy2>
                = object : Parcelable.Creator<Dummy2> {
            override fun createFromParcel(source: Parcel): Dummy2
                    = Dummy2(source)


            override fun newArray(size: Int): Array<Dummy2?>
                    = arrayOfNulls(size)
        }
    }

    private var result: Float = (count * 100).toFloat()

    constructor(`in`: Parcel):this(`in`.readInt())

    override fun writeToParcel(parcel: Parcel, i: Int) {
        parcel.writeInt(count)
    }

    override fun describeContents(): Int = 0

}
