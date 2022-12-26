package com.mckproject.demoshop.ui.cart

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mckproject.demoshop.ui.data.user.UserAddress

class CartViewModel : ViewModel() {

    val shippingAddress: MutableLiveData<UserAddress> = MutableLiveData()

    init{
        shippingAddress.value=null
    }
}