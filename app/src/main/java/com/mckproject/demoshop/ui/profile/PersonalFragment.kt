package com.mckproject.demoshop.ui.profile

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseAuth
import com.mckproject.demoshop.R
import com.mckproject.demoshop.databinding.FragmentPersonalBinding
import com.mckproject.demoshop.ui.activities.LoginActivity
import com.mckproject.demoshop.ui.activities.MainActivity
import com.mckproject.demoshop.ui.data.user.UserDataSource
import com.mckproject.demoshop.ui.di.ImageLoader
import com.mckproject.demoshop.ui.profile.adress.AddressFragmentArgs
import com.mckproject.demoshop.ui.shopping.product_detail.ProductDetailFragmentArgs
import java.io.IOException
import java.util.jar.Manifest
import javax.sql.DataSource

class PersonalFragment : Fragment() {

    private lateinit var personalViewModel: PersonalViewModel
    private var _binding: FragmentPersonalBinding? = null

    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                if(it.data!=null) {
                    val imageUri = it.data?.data!!
                    val name= personalViewModel.userid.value+"."+
                            getExtension(imageUri)
                    (activity as? MainActivity)?.showDialog()
                    UserDataSource().uploadImageToStorage(imageUri,name, object : UploadCallBack{
                        override fun onResult(downloadUrl: Uri) {
                            val userHashmap= HashMap<String,Any>()
                            userHashmap["image"]= downloadUrl.toString()
                            UserDataSource().updateUserInfo(userHashmap, object: UpdateInfoCallBack{
                                override fun onResult(isUpdated: Boolean) {
                                    if(isUpdated){
                                        (activity as? MainActivity)?.hideDialog()
                                        ImageLoader(binding.personalPhoto.context)
                                            .loadPicture(imageUri,binding.personalPhoto)
                                        (activity as? MainActivity)?.showSnackBar(resources.getString(R.string.profile_updated)
                                        ,false)
                                    }
                                }

                                override fun onError(error: Throwable) {
                                    (activity as? MainActivity)?.hideDialog()
                                    (activity as? MainActivity)?.showSnackBar(resources.getString(R.string.updating_faild)
                                            +": "+error.message
                                        ,true)
                                }
                            })
                        }

                        override fun onError(error: Throwable) {
                            (activity as? MainActivity)?.hideDialog()
                            (activity as? MainActivity)?.showSnackBar(resources.getString(R.string.updating_faild)
                                    +": "+error.message
                                ,true)
                        }
                    })
                }
            }
        }
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        personalViewModel =
            ViewModelProvider(this).get(PersonalViewModel::class.java)

        _binding = FragmentPersonalBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val textView: TextView = binding.textName

        personalViewModel.name.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        personalViewModel.email.observe(viewLifecycleOwner, Observer{
            binding.textEmail.text=it
        })

        personalViewModel.imageUri.observe(viewLifecycleOwner, Observer {
            ImageLoader(binding.personalPhoto.context)
                .loadPicture(it.toUri(),binding.personalPhoto)
        })

        binding.linearAdresses.setOnClickListener{
            findNavController()
                .navigate(PersonalFragmentDirections
                    .actionNavigationPersonalToAdressFragment(false))
        }

        binding.btnLogout.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(activity, LoginActivity::class.java))
            activity?.finish()
        }

        binding.personalPhoto.setOnClickListener{
            chooseAPhoto()
        }

        return root
    }

    private fun chooseAPhoto() {
        if(activity?.let { ContextCompat.checkSelfPermission(it, android.Manifest.permission.READ_EXTERNAL_STORAGE)}
        == PackageManager.PERMISSION_GRANTED){
            pickImage()
        }else{
            activity?.let { ActivityCompat.requestPermissions(it,
            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
            0) }
        }
    }

    private fun pickImage(){
        val gallery= Intent(Intent.ACTION_PICK,
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        getResult.launch(gallery)
        //(activity as MainActivity)?.let { it.startActivityFromFragment(this,gallery,1) }
    }


    private fun getExtension(uri: Uri): String?{

           return MimeTypeMap.getSingleton()
                .getExtensionFromMimeType(activity?.contentResolver?.getType(uri))

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}