package com.vanced.manager.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.vanced.manager.ui.fragments.ManagerChangelogFragment
import com.vanced.manager.ui.fragments.MicrogChangelogFragment
import com.vanced.manager.ui.fragments.MusicChangelogFragment
import com.vanced.manager.ui.fragments.VancedChangelogFragment

class SectionPageAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> VancedChangelogFragment()
            1 -> MusicChangelogFragment()
            2 -> MicrogChangelogFragment()
            else -> ManagerChangelogFragment()
        }
    }

}
