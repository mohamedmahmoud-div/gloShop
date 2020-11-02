package com.treecode.GloShop.ui.main.carts

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.treecode.GloShop.R
import com.treecode.GloShop.data.model.home.Product
import com.treecode.GloShop.ui.adapter.carts.MyCartAdapter
import com.treecode.GloShop.ui.main.BaseActivity
import com.treecode.GloShop.ui.main.home.ui.main.ActivityFragmentChangeListener
import com.treecode.GloShop.util.Constants
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CartsActivity : BaseActivity() ,ActivityFragmentChangeListener{
    private lateinit var cartAdapter : MyCartAdapter
    private var products = HashSet<Product>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {

            val fragment = CartsFragment.newInstance()
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.add(R.id.carts_container, fragment)
            fragmentTransaction.commit()
        }
      //  setupUI()
        //getCarts()
    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_carts)
//        val navView: BottomNavigationView = findViewById(R.id.nav_cart_view)
//        navView.selectedItemId = R.id.navigation_carts
//        navView.setOnNavigationItemSelectedListener { item ->
//            when (item.itemId) {
//                R.id.navigation_home ->{
//                    val intent = Intent(this, HomeActivity::class.java)
//                    startActivity(intent)
//                }
//                R.id.navigation_search -> {
//                    val intent = Intent(this, SearchActivity::class.java)
//                    startActivity(intent)
//                }
//                R.id.navigation_carts ->{
//
//                }
//                R.id.navigation_profile ->{
//                    val intent = Intent(this, ProfileActivty::class.java)
//                    startActivity(intent)
//
//                }
//            }
//            true
//        }
//
//        setupUI()
//        getCarts()
//    }
 /*   private fun setupUI(){
        val  verticalLayoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL,false)
        recyclerview_myCart.layoutManager = verticalLayoutManager
        cartAdapter = MyCartAdapter(products)
        recyclerview_myCart.adapter = cartAdapter
    button_myCart.setOnClickListener{


    }

    }*/
    private fun getCarts(){
        val gson = Gson()

        val mPrefs: SharedPreferences =
           getSharedPreferences("test", Context.MODE_PRIVATE)
        val prefsEditor: SharedPreferences.Editor = mPrefs.edit()
        val json = mPrefs.getString(Constants.sharedKey_Add_Cart, "")

        val type = object : TypeToken<Set<Product>>(){}.type
        var productsInCart: HashSet<Product>? = gson.fromJson(json, type)

        if (productsInCart != null){
            products = productsInCart
          //  cartAdapter.addItems(products)
            cartAdapter.notifyDataSetChanged()

        }
        val jsonToSave = gson.toJson(productsInCart)
        prefsEditor.putString(Constants.sharedKey_Add_Cart,jsonToSave)
        prefsEditor.commit()
    }

    override fun getLayoutId(): Int {
      return R.layout.activity_carts
    }

    override fun getBottomNavigationMenuItemId(): Int {
return R.id.navigation_carts
    }

    override fun replaceFragment(fragment: Fragment) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.carts_container, fragment)
       // fragmentTransaction.addToBackStack(fragment.toString())
        fragmentTransaction.commit()    }

    override fun popFragment(fragment: Fragment) {
        supportFragmentManager.popBackStack();
    }

    override fun addFragment(fragment: Fragment) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.carts_container, fragment)
         fragmentTransaction.addToBackStack(fragment.toString())
        fragmentTransaction.commit()

    }
}