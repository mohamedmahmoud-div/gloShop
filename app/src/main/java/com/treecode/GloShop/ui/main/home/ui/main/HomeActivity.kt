package com.treecode.GloShop.ui.main.home.ui.main

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.treecode.GloShop.R
import com.treecode.GloShop.ui.main.BaseActivity
import com.treecode.GloShop.ui.main.home.HomeFragment


class HomeActivity : BaseActivity(),ActivityFragmentChangeListener{


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {

            val fragment = HomeFragment.newInstance()
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.add(R.id.home_container, fragment)
            fragmentTransaction.commit()
        }

    }

/*
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        if (savedInstanceState == null) {

            val fragment = HomeFragment.newInstance()
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.add(R.id.home_container, fragment)
            fragmentTransaction.commit()
        }
        val navView: BottomNavigationView = findViewById(R.id.nav_home_view)
        navView.selectedItemId = R.id.navigation_home
        navView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home ->{

                }
                R.id.navigation_search -> {
                    val intent = Intent(this, SearchActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(0,0)
                }
                R.id.navigation_carts ->{
                    val intent = Intent(this, CartsActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(0,0)

                }
                R.id.navigation_profile ->{
                    val intent = Intent(this, ProfileActivty::class.java)
                    startActivity(intent)

                }
            }
            true
        }
    }
*/

    override fun getLayoutId(): Int {
return R.layout.activity_home
    }

    override fun getBottomNavigationMenuItemId(): Int {
return R.id.navigation_home
    }

    fun  naviagateToSearch(){
        Toast.makeText(this,"Product Search", Toast.LENGTH_SHORT).show()

    }

    override fun replaceFragment(fragment: Fragment) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.home_container, fragment)
        fragmentTransaction.addToBackStack(fragment.toString())
        fragmentTransaction.commit()
    }

    override fun popFragment(fragment: Fragment) {
    }

    override fun addFragment(fragment: Fragment) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.home_container, fragment)
        fragmentTransaction.addToBackStack(fragment.toString())
        fragmentTransaction.commit()
    }
}