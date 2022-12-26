package com.mckproject.demoshop.ui.order.callback

interface OrderCallBack {

    fun onResult(isAdded: Boolean)
    fun onError(error: Throwable)
}