package com.mckproject.demoshop.ui.cart

import android.os.Parcelable
import com.mckproject.demoshop.ui.data.product.Product
import kotlinx.parcelize.Parcelize

@Parcelize
data class CartItem (
    val product: Product,
    var qty: Int=1
):Parcelable