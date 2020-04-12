package com.vanced.manager.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.vanced.manager.ui.fragments.MicrogChangelogFragment
import com.vanced.manager.ui.fragments.VancedChangelogFragment

class SectionPageAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private val fragmentItems = 2
    override fun getItemCount(): Int {
        return fragmentItems
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = VancedChangelogFragment()
            1 -> fragment = MicrogChangelogFragment()
        }
        return fragment!!
    }

}