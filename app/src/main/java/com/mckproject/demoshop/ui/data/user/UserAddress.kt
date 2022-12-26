package com.mckproject.demoshop.ui.data.user

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserAddress (
    val firstName: String="",
    val lastName: String="",
    val streetAndNumber :String="",
    val plz: String="",
    val city: String="",
    val country: String="Germany"
): Parcelable