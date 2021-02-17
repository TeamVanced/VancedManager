package com.vanced.manager.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.vanced.manager.ui.fragments.GrantRootFragment
import com.vanced.manager.ui.fragments.SelectAppsFragment
import com.vanced.manager.ui.fragments.WelcomeFragment

class WelcomePageAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> WelcomeFragment()
            1 -> SelectAppsFragment()
            2 -> GrantRootFragment()
            else -> throw IllegalArgumentException("Unknown fragment")
        }
    }

}