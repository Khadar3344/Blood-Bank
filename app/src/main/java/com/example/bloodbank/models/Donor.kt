package com.example.bloodbank.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Donor (
    val id: String = "",
    val fullName: String = "",
    val sex: String = "",
    val bloodGroup: String = "",
    val mobile: String = "",
    val address: String = "",
    val state: String = "",
    val lastDonate: String = "لم يتبرع بعد!",
    val totalDonate: Int = 0) : Parcelable