package com.gary.news.model

import android.os.Parcel
import android.os.Parcelable

data class Users(
    val id: String = "",
    var name: String = "",
    val email: String = "",
    val image: String = "",
    val aboutMe: String = "Hmmmm. What could I say...",
    val data: String = "",
    val fcmToken: String = "",
    var selected: Boolean = false
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readString()!!,
        source.readString()!!,
        source.readString()!!,
        source.readString()!!,
        source.readString()!!,
        source.readString()!!
    )
    override fun describeContents() = 0
    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(id)
        writeString(name)
        writeString(email)
        writeString(image)
        writeString(data)
        writeString(fcmToken)
    }
    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Users> = object : Parcelable.Creator<Users> {
            override fun createFromParcel(source: Parcel): Users = Users(source)
            override fun newArray(size: Int): Array<Users?> = arrayOfNulls(size)
        }
    }
}