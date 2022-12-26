package com.mckproject.demoshop.ui.profile

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.mckproject.demoshop.ui.data.user.UserDataSource
import kotlinx.coroutines.launch

class PersonalViewModel : ViewModel() {
    //private lateinit var usert :LiveData<UserInfo>

    init {
         viewModelScope.launch {
                 UserDataSource().getUserData().also { user->
                     name.value=user.firstName+", "+user.lastName
                     email.value=user.email
                     userid.value= user.id
                     imageUri.value= user.image
                 //println("Naaaa "+ usert.value?.firstName+5 )
                 }
         }
    }

    val name =MutableLiveData<String>()//get()= _text//"${usert?.firstName}, ${usert?.lastName}"//_text
    val email=MutableLiveData<String>() //: LiveData<String> = _email
    val imageUri=MutableLiveData<String>()
    val userid= MutableLiveData<String>()


    //private var user get() = usert.value

    /**private val _text = MutableLiveData<String>().apply {
        println("Nameee  "+usert.value?.firstName)
        value = "${usert.value?.firstName}, ${usert.value?.lastName}"
    //"${user.value?.get(1)?.firstName}, ${user.value?.get(0)?.lastName}"
    }
    private val _email = MutableLiveData<String>().apply {
        value = "${usert.value?.email}"
        //"${user.value?.get(1)?.firstName}, ${user.value?.get(0)?.lastName}"
    }*/


}