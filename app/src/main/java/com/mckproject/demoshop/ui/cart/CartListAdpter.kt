package com.mckproject.demoshop.ui.cart

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mckproject.demoshop.databinding.ItemCartLayoutBinding
import com.mckproject.demoshop.ui.data.product.Product
import com.mckproject.demoshop.ui.di.ImageLoader


class CartListAdapter(
    private val mountChanged: (cartItem: CartItem, qty: Int) ->Unit,
    private val deleteFromCart: (cartItem: CartItem) -> Unit
): ListAdapter<CartItem,CartItemViewHolder>(Diff) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemViewHolder {
        val inflate = LayoutInflater.from(parent.context)
        return CartItemViewHolder(ItemCartLayoutBinding.inflate(inflate, parent, false))
    }

    override fun onBindViewHolder(holder: CartItemViewHolder, position: Int) {
        val cartItem= getItem(position)
        with(holder.binding){
            productName.text=cartItem.product.name
            if(cartItem.qty<=10)
            qtySpinner.setSelection(cartItem.qty-1)
            itemPrice.text=cartItem.product.price.toString()
            qtySpinner.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val selectedQty = parent?.getItemAtPosition(position).toString().toInt()
                    mountChanged(cartItem, selectedQty)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

            deleteItem.setOnClickListener{
                deleteFromCart(cartItem)
                notifyDataSetChanged()
            }

        }
        ImageLoader(holder.binding.imageView.context).loadPicture(cartItem.product.imageUir.toUri(),holder.binding.imageView)
    }

}

class CartItemViewHolder(
    val binding: ItemCartLayoutBinding
) : RecyclerView.ViewHolder(binding.root){ }

object Diff: DiffUtil.ItemCallback<CartItem>(){
    override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
        return oldItem.product.id == newItem.product.id
    }

    override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
        return  oldItem == newItem
    }

}