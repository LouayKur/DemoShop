package com.mckproject.demoshop.ui.profile

import android.net.Uri

interface UploadCallBack {

    fun onResult(downloadUrl: Uri)
    fun onError(error: Throwable)
}