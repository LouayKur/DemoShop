package com.mckproject.demoshop.ui.order.orderDetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mckproject.demoshop.databinding.ItemCartLayoutBinding
import com.mckproject.demoshop.databinding.ItemOrderCartLayoutBinding
import com.mckproject.demoshop.ui.cart.CartItem
import com.mckproject.demoshop.ui.data.product.Product
import com.mckproject.demoshop.ui.di.ImageLoader


class OrderDetailListAdapter: ListAdapter<CartItem,OrderDetailViewHolder>(Diff) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderDetailViewHolder {
        val inflate = LayoutInflater.from(parent.context)
        return OrderDetailViewHolder(ItemOrderCartLayoutBinding.inflate(inflate, parent, false))
    }

    override fun onBindViewHolder(holder: OrderDetailViewHolder, position: Int) {
            val cartItem= getItem(position)
            with(holder.binding){
                productName.text=cartItem.product.name
                qty.text= cartItem.qty.toString()
                itemPrice.text=cartItem.product.price.toString()

            }
            ImageLoader(holder.binding.imageView.context).loadPicture(cartItem.product.imageUir.toUri(),holder.binding.imageView)
    }


}

class OrderDetailViewHolder(
    val binding: ItemOrderCartLayoutBinding
) : RecyclerView.ViewHolder(binding.root){ }

object Diff: DiffUtil.ItemCallback<CartItem>(){
    override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
        return oldItem.product.id == newItem.product.id
    }

    override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
        return  oldItem == newItem
    }

}