package com.mckproject.demoshop.ui.activities

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.get
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.mckproject.demoshop.R
import com.mckproject.demoshop.databinding.ActivityMainBinding
import com.mckproject.demoshop.databinding.CartWithNumberBinding
import com.mckproject.demoshop.ui.shopping.ShareItemViewModel
import com.mckproject.demoshop.ui.shopping.ShoppingFragment
import com.mckproject.demoshop.ui.shopping.ShoppingFragmentDirections
import java.io.IOException

class MainActivity : ExtendActivity() {

    private val cartViewModel: ShareItemViewModel by viewModels()
    private var qty: Int= 0
    private lateinit var binding: ActivityMainBinding
    lateinit var navController : NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_order, R.id.navigation_shopping, R.id.navigation_personal
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        cartViewModel.list.observe(this, Observer {
            qty=0
            it?.let {
                for(item in it){
                    qty+=item.qty
                }
                invalidateOptionsMenu()
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        navController.navigateUp()
        return super.onSupportNavigateUp()
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {

        supportFragmentManager.fragments.forEach {

            when{
                 it.isVisible && navController.currentDestination?.label!!.equals("Shopping") ->{
                        menu?.findItem(R.id.search_icon)?.isVisible= true
                    }
                it.isVisible && navController.currentDestination?.label!="Shopping" ->{
                    val item= menu?.findItem(R.id.search_icon)
                    val searchView= item?.actionView as SearchView
                    searchView.isVisible=false
                    searchView.visibility= View.GONE
                    searchView.isIconified = true
                }
            }
            //menu?.findItem(R.id.search_icon)?.isVisible= false
        }

        return super.onPrepareOptionsMenu(menu)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.cart_menu, menu)

        //val searchItem= menu?.findItem(R.id.search_icon)
        //searchItem?.isVisible= true

        val menuItem= menu?.findItem(R.id.cartFragment)
        val actionView= menuItem?.actionView
        val textView= actionView?.findViewById<TextView>(R.id.added_items)
        textView?.setText(qty.toString())
        textView?.visibility=  if(qty== 0) View.GONE else View.VISIBLE
        actionView?.setOnClickListener{
            onOptionsItemSelected(menuItem)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //findNavController(R.id.nav_host_fragment_activity_main).navigate(R.id.cartFragment)
        when(item.itemId){
            R.id.cartFragment ->{
                navController.navigate(R.id.cartFragment)   // ShoppingFragmentDirections.actionNavigationShoppingToCartFragment())
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode==0){
            if(grantResults.isNotEmpty()){
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    showSnackBar(resources.getString(R.string.storge_permission_granted),false)
                }else{
                    Toast.makeText(this,resources.getString(R.string.storge_permission_denied),
                        Toast.LENGTH_LONG).show()
                }
            }else{
                showSnackBar(resources.getString(R.string.permission_denied),false)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== RESULT_OK){
            Log.w("TOOOOOS  ", requestCode.toString())
            if(requestCode==1){
                if(data!=null){
                    try {
                        val imageUri= data.data!!
                        Log.w("TOOOOOS", imageUri.toString())
                        showSnackBar(imageUri.toString(),false)
                    }catch (e: IOException){
                        showSnackBar("filed",true)
                    }
                }

            }
        }
    }*/

}