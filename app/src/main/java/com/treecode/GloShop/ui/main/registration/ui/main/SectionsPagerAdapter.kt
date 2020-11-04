package com.treecode.GloShop.ui.main.registration.ui.main

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.treecode.GloShop.R
import com.treecode.GloShop.ui.main.registration.ui.login.ui.main.ui.login.LoginFragment
import com.treecode.GloShop.ui.main.registration.ui.login.ui.main.ui.login.SignupFragment

private val TAB_TITLES = arrayOf(
    R.string.sign_in,
    R.string.sign_up
)

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm,FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = LoginFragment()
           // 1 -> fragment = SignupFragment()
        }
        return fragment!!
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        // Show 2 total pages.
        return 1
    }
}