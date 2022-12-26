package com.mckproject.demoshop.ui.data.product

import androidx.lifecycle.LiveData
import com.mckproject.demoshop.ui.order.callback.GetOrdersCallBack

interface ProductRepository {
     fun getProducts(): LiveData<List<Product>>
    fun getOrders(callback: GetOrdersCallBack)
}

class ProductRepositoryImpl(
    private val dataSource : RemoteDataSource
):ProductRepository{
    override fun getProducts(): LiveData<List<Product>> {
        return dataSource.getProducts()
    }

    override fun getOrders(callback: GetOrdersCallBack) {
        return dataSource.getOrders(callback)
    }

}