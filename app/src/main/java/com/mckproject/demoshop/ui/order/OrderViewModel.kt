package com.mckproject.demoshop.ui.order

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mckproject.demoshop.ui.data.product.ProductRepository
import com.mckproject.demoshop.ui.order.callback.GetOrdersCallBack

class OrderViewModel(
    productRepository : ProductRepository
) : ViewModel() {


    var list =MutableLiveData<List<Order>>()

   init {
       productRepository.getOrders(object : GetOrdersCallBack {
           override fun onResult(orders: ArrayList<Order>) {
               list.postValue(orders)
           }

           override fun onError(error: Throwable) {
              // TODO("Not yet implemented")
           }
       })
   }
}