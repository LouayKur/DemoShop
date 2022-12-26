package com.mckproject.demoshop.ui.activities

import android.annotation.SuppressLint
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.mckproject.demoshop.R
import com.mckproject.demoshop.databinding.ActivityRegisterBinding
import com.mckproject.demoshop.databinding.ProgressDialogBinding

open class ExtendActivity : AppCompatActivity() {

    private lateinit var binding: ProgressDialogBinding
    private lateinit var dialog: Dialog

    /**override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_extend)
    }*/

    @SuppressLint("ResourceAsColor")
    fun showSnackBar(message: String, errorMsg: Boolean){
        val snackBar= Snackbar.make(findViewById(android.R.id.content),message,Snackbar.LENGTH_LONG)
        if(errorMsg)
            snackBar.view.setBackgroundColor(ContextCompat.getColor(this@ExtendActivity,R.color.error_true))
        else
            snackBar.view.setBackgroundColor(ContextCompat.getColor(this@ExtendActivity,R.color.error_false))

        snackBar.show()
    }

    fun showDialog(){
        dialog= Dialog(this)
        binding = ProgressDialogBinding.inflate(layoutInflater)
        dialog.setContentView(binding!!.root)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }

    fun hideDialog(){
        try {
            dialog.dismiss()
        }catch (e: Exception){

        }

    }

}