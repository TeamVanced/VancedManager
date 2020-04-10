package com.vanced.manager.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.vanced.manager.ui.fragments.MicrogChangelogFragment
import com.vanced.manager.ui.fragments.VancedChangelogFragment

class SectionPageAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = VancedChangelogFragment()
            1 -> fragment = MicrogChangelogFragment()
        }
        return fragment!!
    }

}