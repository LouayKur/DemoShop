package com.mckproject.demoshop.ui.shopping.product_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mckproject.demoshop.ui.data.product.Product

class ProductDetailViewModel(
    private val product: Product
) : ViewModel() {
    val pd: MutableLiveData<Product> =MutableLiveData(product)
}