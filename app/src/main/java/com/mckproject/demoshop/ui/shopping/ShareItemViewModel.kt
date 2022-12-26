package com.mckproject.demoshop.ui.shopping

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mckproject.demoshop.ui.cart.CartItem

class ShareItemViewModel: ViewModel() {
    val list: MutableLiveData<ArrayList<CartItem>> = MutableLiveData()

    init {
        list.value= arrayListOf()
    }
}