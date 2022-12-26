package com.mckproject.demoshop.ui.order

import android.os.Parcelable
import com.google.firebase.firestore.ServerTimestamp
import com.mckproject.demoshop.ui.cart.CartItem
import com.mckproject.demoshop.ui.data.user.UserAddress
import kotlinx.parcelize.Parcelize

import kotlin.collections.ArrayList

@Parcelize
data class Order (
    @ServerTimestamp
    val timestamp: java.util.Date?,
     val items: ArrayList<CartItem>,
     val shippingAddress: UserAddress,
     val state: String, //processing, on the way, delivered
):Parcelable
