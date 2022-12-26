package com.mckproject.demoshop.ui.profile.adress

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mckproject.demoshop.ui.data.user.UserAddress
import com.mckproject.demoshop.ui.data.user.UserDataSource
import com.mckproject.demoshop.ui.profile.adress.addone.GetAddressCallBack

class AdressViewModel : ViewModel() {
    val list: MutableLiveData<ArrayList<UserAddress>> = MutableLiveData()

    init {
        UserDataSource().getUserAddresses(object: GetAddressCallBack{
            override fun onResult(userAddressList: ArrayList<UserAddress>) {
                list.value=userAddressList
            }

            override fun onError(error: Throwable) {
                //TODO("Not yet implemented")
            }

        })
    }
}