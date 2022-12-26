package com.mckproject.demoshop.ui.order.orderDetail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import com.mckproject.demoshop.R
import com.mckproject.demoshop.databinding.FragmentOrderDetailBinding
import com.mckproject.demoshop.databinding.FragmentProductDetailBinding
import com.mckproject.demoshop.databinding.FragmentShoppingBinding
import com.mckproject.demoshop.ui.cart.CartItem
import com.mckproject.demoshop.ui.di.ImageLoader
import com.mckproject.demoshop.ui.shopping.ShareItemViewModel
import com.mckproject.demoshop.ui.shopping.ShoppingViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat

class OrderDetailFragment : Fragment() {
    val args by navArgs<OrderDetailFragmentArgs>()

    private var _binding: FragmentOrderDetailBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val myOrder= args.orderDetail
        _binding = FragmentOrderDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.orderDetailRecyclerView.apply {
            addItemDecoration(
                DividerItemDecoration(requireContext(),
                DividerItemDecoration.VERTICAL)
            )
            adapter= OrderDetailListAdapter().also { adapter ->
                adapter.submitList(myOrder.items)
            }
        }

        binding.orderDate.text= myOrder.state
        val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy hh:mm")
        binding.orderDate.text= simpleDateFormat.format(myOrder.timestamp)
        var total: Double= 0.0
        for(item in myOrder.items)
            total +=item.qty* item.product.price.toDouble()
        binding.totalPrice.text= total.toString()

        with(myOrder.shippingAddress){
            binding.name.text= firstName+", "+lastName
            binding.street.text= streetAndNumber
            binding.plz.text= plz
            binding.city.text= city
            binding.country.text= country
        }

        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}