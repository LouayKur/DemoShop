package com.mckproject.demoshop.ui.shopping.product_detail

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
import com.mckproject.demoshop.R
import com.mckproject.demoshop.databinding.FragmentProductDetailBinding
import com.mckproject.demoshop.databinding.FragmentShoppingBinding
import com.mckproject.demoshop.ui.cart.CartItem
import com.mckproject.demoshop.ui.di.ImageLoader
import com.mckproject.demoshop.ui.shopping.ShareItemViewModel
import com.mckproject.demoshop.ui.shopping.ShoppingViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ProductDetailFragment : Fragment() {
    val args by navArgs<ProductDetailFragmentArgs>()
    private val detailViewModel by viewModel<ProductDetailViewModel>{
        parametersOf(args.productId)
    }
    private val viewmodel: ShareItemViewModel by activityViewModels()
    private var _binding: FragmentProductDetailBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentProductDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root

        detailViewModel.pd.observe(viewLifecycleOwner, Observer {
            with(binding){
                ImageLoader(imgProduct.context).loadPicture(it.imageUir.toUri(),imgProduct)
                productName.text=it.name
                productPrice.text="${it.price} €"
                productDescription.text=it.description
                if(!it.available)
                    addToCartBtn.isEnabled=false
            }

        })

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.addToCartBtn.setOnClickListener{
           // activity?.let {
                //val viewmodel = ViewModelProviders.of(it).get(ShareItemViewModel::class.java)
                // update the array in Viewmodel
            val product= detailViewModel.pd.value
            val list= viewmodel.list.value
            var qty: Boolean= false
            if (list != null) {
                if(!list.isEmpty()){
                    for(item in list){
                        if(item.product==product){
                            item.qty+= 1
                            qty=true
                            break
                        }
                    }
                    if(!qty)
                        list?.add(CartItem(product!!))
                }else
                    list?.add(CartItem(product!!))
            }
            viewmodel.list.postValue(list)
            //Log.w("TESSSTSSSS,1","${viewmodel.list.value?:0}")
            //Log.w("TESSST", "${product}")
            //}
        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}