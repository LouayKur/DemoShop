package com.mckproject.demoshop.ui.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.WindowInsets
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.mckproject.demoshop.R
import com.mckproject.demoshop.databinding.ActivityLoginBinding


class LoginActivity : ExtendActivity(){
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
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

        binding.registerLbl.setOnClickListener{
            startActivity(Intent(this@LoginActivity,RegisterActivity::class.java))
        }

        //binding.loginBtn.background= ContextCompat.getDrawable(this,R.drawable.btn_background)

        binding.loginBtn.setOnClickListener{
            val email: String=binding.textMail.text.toString().trim{ it <=' ' }
            val pwd: String=binding.pwd.text.toString().trim{ it <=' ' }
            if(checkLoginInput()){
                showDialog()
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email,pwd)
                    .addOnCompleteListener{task ->
                        hideDialog()
                        if(task.isSuccessful){
                            showSnackBar(resources.getString(R.string.logged_in),false)
                            @Suppress("DEPRECATION")
                            Handler().postDelayed({
                                startActivity(Intent(this@LoginActivity,MainActivity::class.java))
                                finish()
                            },
                                1500
                            )
                        }else{
                            showSnackBar(task.exception!!.message.toString(),true)
                        }
                    }
            }
        }
    }

    private fun checkLoginInput(): Boolean{
        return when{
            TextUtils.isEmpty(binding.textMail.text.toString().trim{ it <=' ' } ) -> {
                showSnackBar(resources.getString(R.string.erroe_email), true)
                false
            }
            TextUtils.isEmpty(binding.pwd.text.toString().trim{ it <=' ' } ) -> {
                showSnackBar(resources.getString(R.string.erroe_pwd), true)
                false
            }
            else ->{true}
        }
    }
}