package com.treecode.GloShop.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.treecode.GloShop.R
import com.treecode.GloShop.data.model.home.Home
import com.treecode.GloShop.data.model.search.SearchResponse
import com.treecode.GloShop.ui.main.carts.CartsActivity
import com.treecode.GloShop.ui.main.home.ui.main.HomeActivity
import com.treecode.GloShop.ui.main.profile.base.ProfileActivty
import com.treecode.GloShop.ui.main.search.SearchActivity
import com.google.android.material.bottomnavigation.BottomNavigationView


abstract class BaseActivity  : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
companion object{
    var  home :Home? = null
    var homeSearchResponse:SearchResponse? = null
    var searchTapSearchResponse:SearchResponse? = null
}
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        navigationView!!.postDelayed({
            val itemId = item.itemId
            if (itemId == R.id.navigation_home) {
                startActivity(Intent(this, HomeActivity::class.java))
            } else if (itemId == R.id.navigation_search) {
                startActivity(Intent(this, SearchActivity::class.java))
            } else if (itemId == R.id.navigation_carts) {
                startActivity(Intent(this, CartsActivity::class.java))
            } else if (itemId == R.id.navigation_profile) {
                startActivity(Intent(this, ProfileActivty::class.java))
            }
            finish()
        }, 0)
        return true
    }
    protected var navigationView: BottomNavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
      //  supportActionBar?.setDisplayShowTitleEnabled(false);
      //sactionBar.

        navigationView = findViewById(R.id.navigation)
        navigationView!!.setOnNavigationItemSelectedListener(this)
    }

    override fun onStart() {
        super.onStart()
        updateNavigationBarState()
    }

    // Remove inter-activity transition to avoid screen tossing on tapping bottom navigation items
    override fun onPause() {
        super.onPause()
        overridePendingTransition(0, 0)
    }
    private fun updateNavigationBarState() {
        val actionId: Int = getBottomNavigationMenuItemId()
        selectBottomNavigationBarItem(actionId)
    }

    fun selectBottomNavigationBarItem(itemId: Int) {
        val item = navigationView!!.menu.findItem(itemId)
        item.isChecked = true
    }

     abstract fun getLayoutId(): Int // this is to return which layout(activity) needs to display when clicked on tabs.


     abstract fun getBottomNavigationMenuItemId(): Int //Which menu item selected and change the state of that menu item

}