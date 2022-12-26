package com.mckproject.demoshop.ui.shopping

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mckproject.demoshop.R
import com.mckproject.demoshop.databinding.ItemShopLayoutBinding
import com.mckproject.demoshop.ui.data.product.Product
import com.mckproject.demoshop.ui.di.ImageLoader

class ProductListAdapter(
    private val onProductClicked: (id: Product) ->Unit,
    private val addToCart: (product: Product) -> Unit
): ListAdapter<Product,ProductViewHolder>(Diff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val inflate = LayoutInflater.from(parent.context)
        return ProductViewHolder(ItemShopLayoutBinding.inflate(inflate, parent, false))
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val item = getItem(position)
        with(holder.binding){
            root.setOnClickListener{
                onProductClicked(item)
            }
            addToCartBtn.setOnClickListener{
                addToCart(item)
            }

            productName.text=item.name
            productPrice.text= item.price.toString()
            if(item.available) {
                productStatus.text = "Available"
            }else {
                productStatus.setTextColor(ContextCompat.getColor(addToCartBtn.context, R.color.out_of_stock))
                productStatus.text = "out of stock"
                addToCartBtn.background= ContextCompat.getDrawable(addToCartBtn.context, R.color.btn_grey)
                addToCartBtn.isEnabled=false
            }
        }
        ImageLoader(holder.binding.productImage.context).loadPicture(item.imageUir.toUri(),holder.binding.productImage)
    }

}

class ProductViewHolder(
    val binding: ItemShopLayoutBinding
) : RecyclerView.ViewHolder(binding.root){ }

object Diff: DiffUtil.ItemCallback<Product>(){
    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        return  oldItem == newItem
    }

}