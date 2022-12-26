package com.mckproject.demoshop.ui.cart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.mckproject.demoshop.R
import com.mckproject.demoshop.databinding.FragmentCartBinding
import com.mckproject.demoshop.ui.activities.MainActivity
import com.mckproject.demoshop.ui.data.user.UserAddress
import com.mckproject.demoshop.ui.data.user.UserDataSource
import com.mckproject.demoshop.ui.order.Order
import com.mckproject.demoshop.ui.order.callback.OrderCallBack
import com.mckproject.demoshop.ui.profile.adress.AdressViewModel
import com.mckproject.demoshop.ui.shopping.ShareItemViewModel
import kotlinx.coroutines.delay

class CartFragment : Fragment() {

    private val toShippingViewModel: CartViewModel by activityViewModels()
    private val cartViewModel: ShareItemViewModel by activityViewModels()
    private val addressViewModel: AdressViewModel by activityViewModels()

    private var _binding: FragmentCartBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        val root: View = binding.root

        cartViewModel.list.observe(viewLifecycleOwner, Observer { list ->
            var total: Double=0.0
            for(item in list){
                total+= item.product.price.toDouble() * item.qty
            }
            binding.totalPrice.text= total.toString()
        })

        binding.cartRecyclerView.apply {

            adapter = CartListAdapter(
                mountChanged = ::mountChanged,
                deleteFromCart = ::deleteFromCart
            ).also { adapter->
                cartViewModel.list.observe(viewLifecycleOwner){
                    adapter.submitList(it)
                }
            }
        }

        toShippingViewModel.shippingAddress.observe(viewLifecycleOwner, Observer {
            if(it!=null)
                setShippingAddress(it)
            else
                initAddress()
        })

        binding.chooseAddress.setOnClickListener{
            findNavController()
                .navigate(CartFragmentDirections
                    .actionCartFragmentToAdressFragment(true))
        }

        binding.placeOrderBtn.setOnClickListener{
            if(checkOrder()){
                (activity as? MainActivity)?.showDialog()
                var order= Order(null,cartViewModel.list.value!!,toShippingViewModel.shippingAddress.value!!,
                resources.getString(R.string.processing))    //,  FieldValue.serverTimestamp() as Timestamp


                UserDataSource().storeUserOrder(order, object: OrderCallBack {
                    override fun onResult(isAdded: Boolean) {
                        if(isAdded){
                            cartViewModel.list.postValue(arrayListOf())
                                findNavController().navigate(CartFragmentDirections
                                    .actionCartFragmentToNavigationOrder())
                            (activity as? MainActivity)?.hideDialog()
                            (activity as? MainActivity)?.showSnackBar(resources.getString(R.string.order_placed),false)

                        }
                    }

                    override fun onError(error: Throwable) {
                        (activity as? MainActivity)?.hideDialog()
                        (activity as? MainActivity)?.showSnackBar(error?.message.toString(),false)
                    }

                })
            }
        }

        return root
    }

    private fun checkOrder(): Boolean {
        return when{
            cartViewModel.list.value.isNullOrEmpty() -> {
                (activity as? MainActivity)
                    ?.showSnackBar(resources.getString(R.string.add_item_to_cart), true)
                false
            }
            toShippingViewModel.shippingAddress.value==null ->{
                (activity as? MainActivity)
                    ?.showSnackBar(resources.getString(R.string.choose_an_address), false)
                false
            }
            else ->{true}
        }
    }

    private fun initAddress() {
            if (!addressViewModel.list.value.isNullOrEmpty()) {
                val address = addressViewModel.list.value!!.get(0)
                setShippingAddress(address)
                toShippingViewModel.shippingAddress.value= address
            }

    }

    private fun setShippingAddress(address: UserAddress){
        binding.name.text = address.firstName + ", " + address.lastName
        binding.street.text = address.streetAndNumber
        binding.plz.text = address.plz
        binding.city.text = address.city
        binding.country.text = address.country
    }

    private fun mountChanged(item: CartItem, qty: Int){
        var mList=cartViewModel.list.value
        if (mList != null) {
            for(tmp in mList){
                if(tmp==item){
                    tmp.qty= qty
                    break
                }
            }
        }
        cartViewModel.list.postValue(mList)
    }

    private fun deleteFromCart(cartItem: CartItem){
        var mList=cartViewModel.list.value
        mList?.remove(cartItem)
        cartViewModel.list.postValue(mList)
    }
/*override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CartViewModel::class.java)
        // TODO: Use the ViewModel
    }*/

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}