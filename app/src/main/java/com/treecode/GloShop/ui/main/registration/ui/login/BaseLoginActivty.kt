package com.treecode.GloShop.ui.main.registration.ui.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.treecode.GloShop.R
import com.treecode.GloShop.ui.adapter.login.AuthenticationPagerAdapter
import com.treecode.GloShop.ui.main.registration.ui.login.ui.main.ui.login.LoginFragment
import com.treecode.GloShop.ui.main.registration.ui.login.ui.main.ui.login.SignupFragment
import kotlinx.android.synthetic.main.activity_base_login_activty.*


class BaseLoginActivty : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base_login_activty)

        val viewPager = pager
        val pagerAdapter =
            AuthenticationPagerAdapter(supportFragmentManager)
        pagerAdapter.addFragmet(LoginFragment())
        pagerAdapter.addFragmet(SignupFragment())
        viewPager.adapter = pagerAdapter
    }
}