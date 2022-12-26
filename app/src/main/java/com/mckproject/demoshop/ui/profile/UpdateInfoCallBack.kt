package com.mckproject.demoshop.ui.profile

interface UpdateInfoCallBack {

    fun onResult(isUpdated: Boolean)
    fun onError(error: Throwable)
}