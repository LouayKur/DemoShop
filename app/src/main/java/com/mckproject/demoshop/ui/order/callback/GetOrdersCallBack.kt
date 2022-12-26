package com.mckproject.demoshop.ui.order.callback
import com.mckproject.demoshop.ui.order.Order

interface GetOrdersCallBack {
    fun onResult(orders: ArrayList<Order>)
    fun onError(error: Throwable)
}