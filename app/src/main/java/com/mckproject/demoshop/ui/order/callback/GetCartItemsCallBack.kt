package com.mckproject.demoshop.ui.order.callback
import com.mckproject.demoshop.ui.cart.CartItem

interface GetCartItemsCallBack {
    fun onResult(userCartItems: ArrayList<CartItem>)
    fun onError(error: Throwable)
}