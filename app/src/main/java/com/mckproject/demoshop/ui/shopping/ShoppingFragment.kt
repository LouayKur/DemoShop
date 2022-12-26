package com.mckproject.demoshop.ui.shopping

import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import android.util.Log.w
import android.view.*
import android.widget.ImageButton
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.mckproject.demoshop.R
import com.mckproject.demoshop.databinding.FragmentShoppingBinding
import com.mckproject.demoshop.ui.cart.CartItem
import com.mckproject.demoshop.ui.data.product.Product
import org.koin.androidx.scope.fragmentScope
import org.koin.androidx.viewmodel.ext.android.viewModel

class ShoppingFragment : Fragment() {

    //private lateinit var shoppingViewModel: ShoppingViewModel
    private val shoppingViewModel by viewModel<ShoppingViewModel>()

    private var _binding: FragmentShoppingBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private  var item: MenuItem?= null
    //private val _item get()= item!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentShoppingBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.recyclerView.apply {
            addItemDecoration(DividerItemDecoration(requireContext(),
            DividerItemDecoration.HORIZONTAL))
            addItemDecoration(DividerItemDecoration(requireContext(),
                DividerItemDecoration.VERTICAL))
            adapter = ProductListAdapter(
                onProductClicked = ::onProductClicked,
                addToCart = ::addToCart
            ).also { adapter->
                shoppingViewModel.products.observe(viewLifecycleOwner){
                    shoppingViewModel.searchProduct.postValue(shoppingViewModel.products.value as ArrayList<Product>?)
                    shoppingViewModel.searchProduct.observe(viewLifecycleOwner,{
                        adapter.submitList(it)
                    })

                }
            }
        }

        return root
    }


    private fun onProductClicked(id: Product){
        findNavController()
            .navigate(ShoppingFragmentDirections
                .actionNavigationShoppingToNavigationProductDetail(id))
    }

    private fun addToCart(product: Product){
        val viewmodel: ShareItemViewModel by activityViewModels()
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
                    list?.add(CartItem(product))
            }else
                list?.add(CartItem(product))
        }
            viewmodel.list.postValue(list)
            //Log.w("TESSSTSSSS,","${viewmodel.list.value?:0}")
            //w("TESSST","${product}")

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        item= menu.findItem(R.id.search_icon)
        item?.isVisible= true
        val searchView= item?.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                shoppingViewModel.searchProduct.postValue(arrayListOf())

                if(!newText.isNullOrEmpty()){
                    var searchlist= ArrayList<Product>()
                    shoppingViewModel.products.value?.forEach {
                        if(it.name.contains(newText,true)){
                            searchlist.add(it)
                        }
                    }
                    shoppingViewModel.searchProduct.postValue(searchlist)
                }else{
                    shoppingViewModel.searchProduct.postValue(
                        shoppingViewModel.products.value as ArrayList<Product>?
                    )
                }
                return false
            }
        })
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}