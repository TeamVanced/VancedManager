package com.vanced.manager.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.vanced.manager.ui.fragments.ManagerChangelogFragment
import com.vanced.manager.ui.fragments.VancedChangelogFragment

class SectionPageRootAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> VancedChangelogFragment()
            1 -> ManagerChangelogFragment()
            else -> throw NotImplementedError()
        }
    }

}