package com.mckproject.demoshop.ui.activities

import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowInsets
import android.view.WindowManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.mckproject.demoshop.R
import com.mckproject.demoshop.databinding.ActivityRegisterBinding
import com.mckproject.demoshop.ui.data.user.User
import com.mckproject.demoshop.ui.data.user.UserDataSource
import com.mckproject.demoshop.ui.data.user.UserInfo

class RegisterActivity : ExtendActivity() {
    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        @Suppress("DEPRECATION")
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        }else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        binding.loginLbl.setOnClickListener{
            onBackPressed()
        }

        binding.btnRegister.setOnClickListener{
            if(checkRegisterInput()){
                showDialog()
               val email: String= binding.textEmail.text.toString().trim{ it <=' ' }
               val pwd: String= binding.textPassowrd.text.toString().trim{ it <=' ' }
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,pwd)
                    .addOnCompleteListener{task ->
                        if(task.isSuccessful){
                            val firebaseuser: FirebaseUser= task.result!!.user!!
                            //here to save user data
                            //val adresses= ArrayList<UserAdress>()
                            //val adress= UserAdress()
                            //adresses.add(UserAdress("sasoki","hambola","ksi 40","04205 ","tizi"))
                            val user= UserInfo(
                                firebaseuser.uid,
                                binding.textFrstName.text.toString().trim{ it <=' ' },
                                binding.textLastName.text.toString().trim{ it <=' ' },
                                email,
                                ""
                            )
                            val userData= User(user, arrayListOf())
                            UserDataSource().storeUserData(this@RegisterActivity, userData)
                        }else{
                            hideDialog()
                            showSnackBar(task.exception!!.message.toString(),true)
                        }

                    }
            }
        }

    }

    private fun checkRegisterInput(): Boolean{
        return when{
            TextUtils.isEmpty(binding.textFrstName.text.toString().trim{ it <=' ' } ) -> {
                showSnackBar(resources.getString(R.string.erroe_frst_name), true)
                false
            }
            TextUtils.isEmpty(binding.textLastName.text.toString().trim{ it <=' ' } ) -> {
                showSnackBar(resources.getString(R.string.erroe_last_name), true)
                false
            }
            TextUtils.isEmpty(binding.textEmail.text.toString().trim{ it <=' ' } ) -> {
                showSnackBar(resources.getString(R.string.erroe_email), true)
                false
            }
            TextUtils.isEmpty(binding.textPassowrd.text.toString().trim{ it <=' ' } ) -> {
                showSnackBar(resources.getString(R.string.erroe_pwd), true)
                false
            }
            binding.textConfirmPassword.text.toString().trim{ it <=' ' } != binding.textPassowrd.text.toString() -> {
                showSnackBar(resources.getString(R.string.erroe_confirm_pwd), true)
                false
            }

            else ->{true}
        }
    }
}