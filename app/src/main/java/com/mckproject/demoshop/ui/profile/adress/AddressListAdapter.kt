package com.mckproject.demoshop.ui.profile.adress

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mckproject.demoshop.databinding.ItemAdressLayoutBinding
import com.mckproject.demoshop.ui.data.user.UserAddress

class AddressListAdapter(
    val deleteItem: (address: UserAddress) -> Unit,
    val pickAddress: Boolean,
    val chooseAddress: (address: UserAddress)-> Unit
): ListAdapter<UserAddress,AddressViewHolder>(Diff) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        val inflate = LayoutInflater.from(parent.context)
        return AddressViewHolder(ItemAdressLayoutBinding.inflate(inflate, parent, false))
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        val address= getItem(position)
        with(holder.binding){
            name.text= address.firstName+", "+ address.lastName
            street.text= address.streetAndNumber
            plz.text= address.plz
            country.text= address.country
            city.text= address.city
            deleteAddress.setOnClickListener{
                deleteItem(address)
            }
        }
        if(pickAddress){
            holder.binding.root.setOnClickListener{
                chooseAddress(address)
            }
        }

    }

}


class AddressViewHolder(
    val binding: ItemAdressLayoutBinding
) : RecyclerView.ViewHolder(binding.root){ }

object Diff: DiffUtil.ItemCallback<UserAddress>(){
    override fun areItemsTheSame(oldItem: UserAddress, newItem: UserAddress): Boolean {
        return (oldItem.firstName+oldItem.lastName).equals(newItem.firstName+newItem.lastName)
                && oldItem.streetAndNumber.equals(newItem.streetAndNumber)
                && oldItem.city.equals(newItem.city)
    }

    override fun areContentsTheSame(oldItem: UserAddress, newItem: UserAddress): Boolean {
        return  oldItem == newItem
    }

}