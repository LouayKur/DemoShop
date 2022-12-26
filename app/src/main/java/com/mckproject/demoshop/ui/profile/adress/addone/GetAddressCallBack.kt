package com.mckproject.demoshop.ui.profile.adress.addone
import com.mckproject.demoshop.ui.data.user.UserAddress

interface GetAddressCallBack {
    fun onResult(userAddressList: ArrayList<UserAddress>)
    fun onError(error: Throwable)
}