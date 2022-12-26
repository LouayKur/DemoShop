package com.mckproject.demoshop.ui.profile.adress.addone

interface AddressCallBack {

    fun onResult(isAdded: Boolean)
    fun onError(error: Throwable)
}