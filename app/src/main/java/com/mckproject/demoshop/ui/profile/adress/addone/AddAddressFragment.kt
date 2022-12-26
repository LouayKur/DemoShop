package com.mckproject.demoshop.ui.profile.adress.addone

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.mckproject.demoshop.R
import com.mckproject.demoshop.databinding.FragmentAddAddressBinding
import com.mckproject.demoshop.ui.activities.MainActivity
import com.mckproject.demoshop.ui.data.user.UserAddress
import com.mckproject.demoshop.ui.data.user.UserDataSource
import com.mckproject.demoshop.ui.profile.adress.AdressViewModel


class AddAddressFragment : Fragment(){

    private val addressViewModel: AdressViewModel by activityViewModels()
    private var _binding: FragmentAddAddressBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddAddressBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.saveAdress.setOnClickListener{
            if(checkInput()){
                (activity as? MainActivity)?.showDialog()
                val useraddress= UserAddress(
                    binding.textFrstName.text.toString(),
                    binding.textLastName.text.toString().trim{ it <=' ' },
                    binding.textStreet.text.toString(),
                    binding.textPlz.text.toString(),
                    binding.textCity.text.toString(),
                    binding.countrySpinner.selectedItem.toString()
                )
                UserDataSource().storeUserAddress(useraddress, object: AddressCallBack{
                    override fun onResult(isAdded: Boolean) {
                        if(isAdded){
                            (activity as? MainActivity)?.hideDialog()
                            var newList= addressViewModel.list.value
                            newList?.add(useraddress)
                            addressViewModel.list.postValue(newList)
                            Toast.makeText(context,resources.getString(R.string.address_added),Toast.LENGTH_LONG).show()
                            findNavController().navigateUp()
                        }
                    }
                    override fun onError(error: Throwable) {
                        (activity as? MainActivity)?.hideDialog()
                        Toast.makeText(context,error.message,Toast.LENGTH_LONG).show()
                    }

                })
            }
        }

        return root
    }

    private fun checkInput(): Boolean {
        return when{
            TextUtils.isEmpty(binding.textFrstName.text.toString()) -> {
                (activity as? MainActivity)?.showSnackBar(resources.getString(R.string.erroe_frst_name), true)
                false
            }
            TextUtils.isEmpty(binding.textLastName.text.toString().trim{ it <=' ' } ) -> {
                (activity as? MainActivity)?.showSnackBar(resources.getString(R.string.erroe_last_name), true)
                false
            }
            TextUtils.isEmpty(binding.textStreet.text.toString().trim{ it <=' ' } ) -> {
                (activity as? MainActivity)?.showSnackBar(resources.getString(R.string.erroe_entr_street), true)
                false
            }
            TextUtils.isEmpty(binding.textPlz.text.toString().trim{ it <=' ' } ) -> {
                (activity as? MainActivity)?.showSnackBar(resources.getString(R.string.erroe_plz), true)
                false
            }
            TextUtils.isEmpty(binding.textCity.text.toString().trim{ it <=' ' } ) -> {
                (activity as? MainActivity)?.showSnackBar(resources.getString(R.string.erroe_city), true)
                false
            }

            else ->{true}
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}