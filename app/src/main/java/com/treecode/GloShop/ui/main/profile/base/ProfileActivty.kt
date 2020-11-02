package com.treecode.GloShop.ui.main.profile.base

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.treecode.GloShop.R
import com.treecode.GloShop.ui.main.BaseActivity
import com.treecode.GloShop.ui.main.home.ui.main.ActivityFragmentChangeListener
import com.treecode.GloShop.ui.main.registration.BaseRegisterActivity

class ProfileActivty : BaseActivity(), ActivityFragmentChangeListener ,LogoutListener{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      //val mainProfileFragment = MainProfileFragment()
        addFragment()
    }
private fun addFragment(){
    val fragment = MainProfileFragment.newInstance("","")
    val fragmentManager = supportFragmentManager
    val fragmentTransaction = fragmentManager.beginTransaction()
    fragmentTransaction.add(R.id.profile_container, fragment)
    fragmentTransaction.commit()
}
    override fun getLayoutId(): Int {
return R.layout.activity_profile_activty
    }

    override fun getBottomNavigationMenuItemId(): Int {
return R.id.navigation_profile
    }

    override fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.profile_container, fragment)
        fragmentTransaction.addToBackStack(fragment.toString())
        fragmentTransaction.commit()
    }

    override fun popFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.remove(fragment)
        fragmentTransaction.commit()
    }

    override fun addFragment(fragment: Fragment) {
    }

    override fun onClickLogout() {
        startActivity(Intent(this, BaseRegisterActivity::class.java))
        finish()
    }
}
interface LogoutListener{
    fun onClickLogout()
}