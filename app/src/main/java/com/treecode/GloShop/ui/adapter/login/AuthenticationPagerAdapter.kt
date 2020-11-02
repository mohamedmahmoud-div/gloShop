package com.treecode.GloShop.ui.adapter.login

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class AuthenticationPagerAdapter (fm : FragmentManager): FragmentPagerAdapter(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private var fragmentList =  ArrayList<Fragment>();


    fun addFragmet(fragment: Fragment) {
        fragmentList.add(fragment);
    }

    override fun getItem(position: Int): Fragment {
        return fragmentList.get(position);
    }

    override fun getCount(): Int {
        return fragmentList.size
    }
}
