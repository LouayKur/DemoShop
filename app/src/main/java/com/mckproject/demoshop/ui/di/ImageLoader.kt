package com.mckproject.demoshop.ui.di

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.mckproject.demoshop.R
import java.io.IOException

class ImageLoader(private val context: Context) {
    fun loadPicture(uri: Uri, imageView: ImageView){
        try {
            Glide
                .with(context)
                .load(Uri.parse(uri.toString()))
                .centerCrop()
                .placeholder(R.drawable.image_placeholder_foreground)
                .into(imageView)
        }catch (e: IOException){
            e.printStackTrace()
        }
    }
}