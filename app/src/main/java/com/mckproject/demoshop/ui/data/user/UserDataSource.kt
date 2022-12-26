package com.mckproject.demoshop.ui.data.user


import android.content.ContentValues.TAG
import android.net.Uri
import android.os.Handler
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

import com.mckproject.demoshop.ui.activities.RegisterActivity
import com.mckproject.demoshop.ui.order.Order
import com.mckproject.demoshop.ui.order.callback.OrderCallBack
import com.mckproject.demoshop.ui.profile.UpdateInfoCallBack
import com.mckproject.demoshop.ui.profile.UploadCallBack
import com.mckproject.demoshop.ui.profile.adress.addone.AddressCallBack
import com.mckproject.demoshop.ui.profile.adress.addone.GetAddressCallBack
import java.lang.Character.getType
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class UserDataSource {

    private val fireStoreData= FirebaseFirestore.getInstance()
    private var success=true
   // private var userInfo= UserInfo

    fun storeUserData(activity: RegisterActivity, userData: User){

        storeUserInfo(userData.userInfo)
       /* if(!userData.addressList.isNullOrEmpty()){
            for(address in userData.addressList)
                storeUserAddress(address)
        }*/

        if(success){
            activity.hideDialog()
            activity.showSnackBar("You are registered successfully",false)
            @Suppress("DEPRECATION")
            Handler().postDelayed({
                FirebaseAuth.getInstance().signOut()
                activity.finish()
            },
                1500
            )
        }else{
            activity.hideDialog()
            FirebaseAuth.getInstance().currentUser?.delete()
            activity.showSnackBar("Saving Data failed",true)
            success=true
        }
    }

    fun updateUserInfo(userMap: HashMap<String,Any>, callback: UpdateInfoCallBack){
        if(getCurrentUser()!=null) {
            fireStoreData.collection("users").document(getCurrentUser())
                .update(userMap)
                .addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        callback.onResult(true)
                    }else
                        task.exception?.let { callback.onError(it) }
                }
        }
    }

    fun uploadImageToStorage(imageUri: Uri,name: String, callback: UploadCallBack){

        if(getCurrentUser()!=null){
            val sReff: StorageReference= FirebaseStorage.getInstance().reference
                .child("userPhotos")
            val sRef=  sReff.child(name)
            sRef.putFile(imageUri).
                    addOnCompleteListener{task ->
                        if(task.isSuccessful){
                            task.result.metadata!!.reference!!.downloadUrl
                                .addOnSuccessListener{uri->
                                    callback.onResult(uri)
                                }
                        }else{
                            task.exception?.let { callback.onError(it) }
                        }
                    }
        }
    }

    fun storeUserAddress( userData: UserAddress, callback:AddressCallBack) {

        if(getCurrentUser()!=null){
            fireStoreData.collection("users").document(getCurrentUser()).collection("addresses")
                .add(userData)
                .addOnCompleteListener{ task->
                    if(task.isSuccessful)
                        callback.onResult(true)
                    else
                        task.exception?.let { callback.onError(it) }
                }
        }
    }

    fun deleteUserAddress(userData: UserAddress, callback:AddressCallBack){
        if(getCurrentUser()!=null){
            fireStoreData.collection("users").document(getCurrentUser()).collection("addresses")
                .whereEqualTo("firstName",userData.firstName)
                .whereEqualTo("lastName",userData.lastName)
                .whereEqualTo("streetAndNumber",userData.streetAndNumber)
                .whereEqualTo("plz",userData.plz)
                .whereEqualTo("city",userData.city)
                .get()
                .addOnCompleteListener { task->
                    if(task.isSuccessful){
                        for(doc in task.result){
                            fireStoreData.collection("users")
                                .document(getCurrentUser())
                                .collection("addresses")
                                .document(doc.id)
                                .delete()
                                .addOnCompleteListener { deleteTask->
                                    if(!deleteTask.isSuccessful)
                                        deleteTask.exception?.let { callback.onError(it) }
                                }

                        }
                        callback.onResult(true)
                    }else
                        task.exception?.let { callback.onError(it) }
                }
        }
    }

    fun getUserAddresses(callback: GetAddressCallBack){
        if(getCurrentUser()!=null) {
            fireStoreData.collection("users").document(getCurrentUser())
                .collection("addresses")
                .get()
                .addOnCompleteListener { task->
                    if(task.isSuccessful){
                        val list = ArrayList<UserAddress>()
                        for(doc in task.result){
                            val userad= doc.toObject(UserAddress::class.java)
                            list.add(userad)
                        }
                        callback.onResult(list)
                    }else{
                        task.exception?.let { callback.onError(it) }
                    }
                }
        }
    }

    fun storeUserOrder(order: Order, callback: OrderCallBack){
        if(getCurrentUser()!=null) {
            val userid= getCurrentUser()!!
            //val orderident= OrderIdentifier(userid, FieldValue.serverTimestamp() as Date)
            val map: MutableMap<String, Any> = HashMap()
            map["date"] = FieldValue.serverTimestamp()
            map["userid"]=userid
            map["state"]= order.state

            val ref= fireStoreData.collection("orders").document()

            ref.set(map)
                .addOnCompleteListener { task ->
                    if(!task.isSuccessful)
                        task.exception?.let { callback.onError(it) }
                    else{
                        for(item in order.items) {
                            ref.collection("items")
                                .add(item)
                                .addOnCompleteListener { task->
                                    if(!task.isSuccessful)
                                        task.exception?.let { callback.onError(it) }
                                    else{
                                        if(item==order.items.get(order.items.size-1)){
                                            ref.collection("shippingAddress")
                                                .add(order.shippingAddress)
                                                .addOnCompleteListener { task->
                                                    if(!task.isSuccessful)
                                                        task.exception?.let { callback.onError(it) }
                                                    else
                                                        callback.onResult(true)
                                                }
                                        }
                                    }
                                }
                        }
                    }
                }
        }
    }

    private fun storeUserInfo(userData: UserInfo){

        fireStoreData.collection("users").document(userData.id)
            .set(userData, SetOptions.merge())
            .addOnFailureListener {
                success=false
            }
    }

    suspend fun getUserData()= suspendCoroutine<UserInfo>{ con ->
            val userUid: String?= getCurrentUser()
            //val adresses = MutableLiveData<List<UserAdress>>()
            //adresses.add(Adress("","","","",""))
            //var user: User= User("","","","","",adresses)
            //var userInfo= MutableLiveData<UserInfo>()
            if(userUid!=null){
                fireStoreData.collection("users").document(userUid)
                    .get()
                    .addOnSuccessListener{ document ->
                        if(document!= null) {
                            val user= document.toObject(UserInfo::class.java)
                            if(user!=null)
                            con.resume(user!!)
                            Log.d(TAG, "DocumentSnapshot data: ${user?.lastName}")
                        }
                    }
                    .addOnFailureListener { e->
                        con.resume(UserInfo())
                        Log.d(TAG, "DocumentSnapshot data: FAILLLLL")
                    }
            }
            //val user: User= User("","","","","",adresses)
            //while (userInfo.value?.id==null){}
            //Log.d(TAG, "DocumentSnapshot data: ${userInfo.value?.lastName} 55")
            //return userInfo
    }

    private fun getCurrentUser(): String{
        return FirebaseAuth.getInstance().currentUser?.uid.toString()
    }



}