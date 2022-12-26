package com.mckproject.demoshop.ui.profile.adress

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import com.mckproject.demoshop.databinding.FragmentAdressBinding
import com.mckproject.demoshop.ui.activities.MainActivity
import com.mckproject.demoshop.ui.cart.CartViewModel
import com.mckproject.demoshop.ui.data.user.UserAddress
import com.mckproject.demoshop.ui.data.user.UserDataSource
import com.mckproject.demoshop.ui.profile.adress.addone.AddressCallBack
import com.mckproject.demoshop.ui.profile.adress.addone.GetAddressCallBack

class AddressFragment : Fragment() {

    private val addressViewModel: AdressViewModel by activityViewModels()
    private var _binding: FragmentAdressBinding? = null
    val args by navArgs<AddressFragmentArgs>()
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAdressBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.addressRecycler.apply {
            addItemDecoration(
                DividerItemDecoration(requireContext(),
                DividerItemDecoration.VERTICAL)
            )
            adapter = AddressListAdapter(
                deleteItem = ::deleteAddress,
                args.pickAddress,
                chooseAddress = ::chooseAddress
            ).also { adapter ->
                addressViewModel.list.observe(viewLifecycleOwner) {
                    adapter.submitList(it)
                }
            }
        }

        binding.addBtn.setOnClickListener{
            findNavController().navigate(AddressFragmentDirections
                .actionAdressFragmentToAddAddressFragment())
        }

        return root
    }

    private fun deleteAddress(address: UserAddress){
        (activity as? MainActivity)?.showDialog()
        UserDataSource().deleteUserAddress(address, object :AddressCallBack{
            override fun onResult(isAdded: Boolean) {
                if(isAdded){
                    (activity as? MainActivity)?.hideDialog()

                    UserDataSource().getUserAddresses(object: GetAddressCallBack {
                        override fun onResult(userAddressList: ArrayList<UserAddress>) {
                            addressViewModel.list.postValue(userAddressList)
                        }

                        override fun onError(error: Throwable) {
                            //TODO("Not yet implemented")
                        }
                    })
                }
            }
            override fun onError(error: Throwable) {
                (activity as? MainActivity)?.hideDialog()
                (activity as? MainActivity)?.showSnackBar(error.message.toString(),true)
            }

        })
    }

    private fun chooseAddress(address: UserAddress){
        val addressViewModel: CartViewModel by activityViewModels()
        addressViewModel.shippingAddress.value= address
        findNavController()
            .navigateUp()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
    /* override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AdressViewModel::class.java)
        // TODO: Use the ViewModel
    }*/

