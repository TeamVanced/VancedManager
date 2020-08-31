package com.vanced.manager.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.vanced.manager.ui.fragments.MicrogChangelogFragment
import com.vanced.manager.ui.fragments.MusicChangelogFragment

class SectionPageMusicAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MusicChangelogFragment()
            1 -> MicrogChangelogFragment()
            else -> throw NotImplementedError()
        }
    }

}
