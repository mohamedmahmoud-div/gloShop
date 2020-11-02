package com.treecode.GloShop.ui.main.home.ui.main

import androidx.fragment.app.Fragment

interface ActivityFragmentChangeListener {
    fun replaceFragment(fragment: Fragment)
    fun popFragment(fragment: Fragment)
    fun addFragment(fragment: Fragment)
}