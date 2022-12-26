package com.mckproject.demoshop.ui.order

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mckproject.demoshop.databinding.ItemOrderLayoutBinding
import com.mckproject.demoshop.ui.di.ImageLoader
import java.text.SimpleDateFormat


class OrderListAdapter(
    private val onOrderClicked: (order: Order) ->Unit
): ListAdapter<Order,OrderViewHolder>(Diff) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val inflate = LayoutInflater.from(parent.context)
        return OrderViewHolder(ItemOrderLayoutBinding.inflate(inflate, parent, false))
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val item = getItem(position)
        val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy hh:mm")
        with(holder.binding){
            orderDate.text= simpleDateFormat.format(item.timestamp)
            orderState.text= item.state
            root.setOnClickListener{
                onOrderClicked(item)
            }
            var total: Double= 0.0
            for(item in item.items)
                total +=item.qty* item.product.price.toDouble()
            itemPrice.text= total.toString()
        }
        ImageLoader(holder.binding.imageView.context)
            .loadPicture(item.items?.get(0).product.imageUir.toUri()
                ,holder.binding.imageView)
    }

}

class OrderViewHolder(
    val binding: ItemOrderLayoutBinding
) : RecyclerView.ViewHolder(binding.root){ }

object Diff: DiffUtil.ItemCallback<Order>(){
    override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
        return oldItem.timestamp == newItem.timestamp
    }

    override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
        return  oldItem == newItem
    }

}