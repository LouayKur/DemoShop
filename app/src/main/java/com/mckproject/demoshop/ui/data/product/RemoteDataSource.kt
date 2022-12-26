package com.mckproject.demoshop.ui.data.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

import com.mckproject.demoshop.ui.cart.CartItem
import com.mckproject.demoshop.ui.data.user.UserAddress
import com.mckproject.demoshop.ui.order.callback.GetCartItemsCallBack
import com.mckproject.demoshop.ui.order.callback.GetOrdersCallBack
import com.mckproject.demoshop.ui.order.Order
import com.mckproject.demoshop.ui.profile.adress.addone.GetAddressCallBack

import java.util.*
import kotlin.collections.ArrayList

interface RemoteDataSource {
    fun getProducts(): LiveData<List<Product>>
    fun getOrders(callback: GetOrdersCallBack)
}

class FirebaseSource():RemoteDataSource {

    private val fireStoreData = FirebaseFirestore.getInstance()
    private val ref = fireStoreData.collection("products")
    private val orderRef= fireStoreData.collection("orders")

    override  fun getProducts(): LiveData<List<Product>> {
        val products = MutableLiveData<List<Product>>()
        ref.addSnapshotListener { doc, err ->
            products.value= if (err != null) {
                //Log.d(ContentValues.TAG, "DocumentSnapshot data: ${doc.toString()} 45")
                return@addSnapshotListener
                emptyList()
            } else {
                //Log.d(ContentValues.TAG, "DocumentSnapshot data: ${doc.toString()} 66")
                doc?.mapNotNull { snapshot ->
                        Product(
                            id = snapshot.getLong("id") ?: 0,
                            name = snapshot.getString("name").orEmpty(),
                            price = snapshot.getDouble("price") ?: 0.0,
                            imageUir = snapshot.getString("imageUir").orEmpty(),
                            available = snapshot.getBoolean("available") ?: false,
                            description = snapshot.getString("description").orEmpty()
                        )
                }
            }
        }
        return products
    }

    override fun getOrders(callback: GetOrdersCallBack) {
        var userid: String?=""
        if(getCurrentUser()!= null)
            userid= getCurrentUser()
        else
            callback.onError(Throwable("User not found"))

        orderRef.whereEqualTo("userid",userid)
            .get()
            .addOnCompleteListener {task->
                if(task.isSuccessful){
                    val orders = ArrayList<Order>()
                    for(doc in task!!.result){

                        getCartItems(doc.reference.collection("items")
                            ,object : GetCartItemsCallBack {
                                override fun onResult(userCartItems: ArrayList<CartItem>) {

                                    getShippingAddress(doc.reference.collection("shippingAddress"),
                                        object: GetAddressCallBack{
                                            override fun onResult(userAddressList: ArrayList<UserAddress>) {
                                                val shipAddress= userAddressList?.get(0)
                                                var date= doc.getTimestamp("date")?.toDate()

                                                orders.add(
                                                    Order(
                                                        date?.let {it },
                                                        items= userCartItems,
                                                        shippingAddress= shipAddress,
                                                        state = doc.getString("state").orEmpty()
                                                    )
                                                )
                                                //Log.d(ContentValues.TAG, "DocumentSnapshot data: ${orders.toString()} 81")
                                                if(doc== task!!.result.last())
                                                    callback.onResult(orders)
                                            }
                                            override fun onError(error: Throwable) {}
                                        })
                                }
                                override fun onError(error: Throwable) {}
                            })
                    }

                }else
                    task?.exception?.let { callback.onError(it) }
            }
    }

    /*data?.let {
        for ((key, value) in data) {
            val v =value as Map<*, *>
            list.add(CartItem(
                Product(
                    id = v["id"].toString().toLong(),
                    name = v["name"].toString(),
                    price = v["price"].toString().toDouble(),
                    imageUir = v["imageUir"].toString(),
                    available = v["available"].toString().toBoolean(),
                    description = v["description"].toString()
                ),
                doc.get("qty").toString().toInt()
            ))
        }
    }*/

    private fun getCartItems(ref: CollectionReference,callback: GetCartItemsCallBack){
        ref.get()
            .addOnCompleteListener{ task->
                if(task.isSuccessful){
                    val list = ArrayList<CartItem>()
                    if(!task.result.isEmpty) {
                        for (doc in task.result) {
                           // Log.d(ContentValues.TAG, "DocumentSnapshot data: ${doc.data.get("product")} 99")
                            val data = doc.data
                            data?.let {
                                for ((key, value) in data) {
                                    try {
                                        val v =value as Map<String, *>
                                        //Log.d(ContentValues.TAG, "DocumentSnapshot data: ${v["name"]} 99")
                                        list.add(CartItem(
                                            Product(
                                                id = v["id"].toString().toLong(),
                                                name = v["name"].toString(),
                                                price = v["price"].toString().toDouble(),
                                                imageUir = v["imageUir"].toString(),
                                                available = v["available"].toString().toBoolean(),
                                                description = v["description"].toString()
                                            ),
                                            doc.get("qty").toString().toInt()
                                        ))
                                    }catch (e: Exception){

                                    }

                                }
                            }
                        }
                        callback.onResult(list)
                    }else{
                        task.exception?.let { callback.onError(it) }
                    }
                }else{
                    task.exception?.let { callback.onError(it) }
                }
            }
    }

    fun getShippingAddress(ref: CollectionReference,callback: GetAddressCallBack){
                ref.get()
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

    private fun getCurrentUser(): String{
        return FirebaseAuth.getInstance().currentUser?.uid.toString()
    }
    /** override fun getProducts() = suspendCoroutine<List<Product>> { con ->
        ref.addSnapshotListener { doc, err ->
            if (err != null) {
                Log.d(ContentValues.TAG, "DocumentSnapshot data: ${doc.toString()} 45")
                return@addSnapshotListener
                con.resume(emptyList<Product>())
            } else {
                Log.d(ContentValues.TAG, "DocumentSnapshot data: ${doc.toString()} 66")
                if (doc != null) {
                    try {
                        con.resume(
                            doc!!.mapNotNull { snapshot ->
                                Product(
                                    id = snapshot.getLong("id") ?: 0,
                                    name = snapshot.getString("name").orEmpty(),
                                    price = snapshot.getDouble("price") ?: 0.0,
                                    imageUir = snapshot.getString("imageUir").orEmpty(),
                                    available = snapshot.getBoolean("available") ?: false
                                )
                            }
                        )
                    } catch (e: Throwable) {
                        con.resumeWithException(e)
                    }
                }
            }
        }
             ref.get()
            .addOnSuccessListener{ document->
            if(!document.isEmpty){
            Log.d(ContentValues.TAG, "DocumentSnapshot data: ${document} 55")
            con.resume(
            document.mapNotNull { snapshot->
            Product(
            id=snapshot.getLong("id")?: 0,
            name = snapshot.getString("name").orEmpty(),
            price = snapshot.getDouble("price")?: 0.0,
            imageUir = snapshot.getString("imageUir").orEmpty(),
            available = snapshot.getBoolean("available")?:false
            )
            }
            )
            }
            }
            .addOnFailureListener{
            con.resume(emptyList())
            }

        }*/

}
