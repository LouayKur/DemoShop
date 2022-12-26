package com.mckproject.demoshop.ui.shopping

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mckproject.demoshop.ui.data.product.Product
import com.mckproject.demoshop.ui.data.product.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ShoppingViewModel(
    val productRepository : ProductRepository
) : ViewModel() {


    var products :LiveData<List<Product>> = MutableLiveData() // productRepository.getProducts()

    var searchProduct = MutableLiveData<ArrayList<Product>>()
    init {
       // GlobalScope.launch(Dispatchers.IO) {
        viewModelScope.launch {
            products= productRepository.getProducts()
        }
    }
}