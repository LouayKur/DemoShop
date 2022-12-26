package com.mckproject.demoshop.ui.data.product

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Product(
    var id: Long,
    var name: String,
    var price: Number,
    var imageUir: String,
    var available: Boolean,
    var description: String
):Parcelable
