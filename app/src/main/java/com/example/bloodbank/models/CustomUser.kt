package com.example.bloodbank.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class CustomUser (
    val name: String = "",
    val phone: String = "",
    val address: String = "",
    val bloodGroup: String = "",
    val state: String = "",
    val time: String = "",
    val date: String = ""):Parcelable