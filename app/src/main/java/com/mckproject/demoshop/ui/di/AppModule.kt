package com.mckproject.demoshop.ui.di

import com.mckproject.demoshop.ui.cart.CartViewModel
import com.mckproject.demoshop.ui.data.product.FirebaseSource
import com.mckproject.demoshop.ui.data.product.ProductRepository
import com.mckproject.demoshop.ui.data.product.ProductRepositoryImpl
import com.mckproject.demoshop.ui.data.product.RemoteDataSource
import com.mckproject.demoshop.ui.order.OrderViewModel
import com.mckproject.demoshop.ui.shopping.ShoppingViewModel
import com.mckproject.demoshop.ui.shopping.product_detail.ProductDetailViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val AppModule= module {
    single<RemoteDataSource> {FirebaseSource() }
    single<ProductRepository> {ProductRepositoryImpl(get()) }
    viewModel{ShoppingViewModel(get())}
    viewModel{param->ProductDetailViewModel(param.get())}
    viewModel{OrderViewModel(get())}
   // viewModel{CartViewModel()}
}