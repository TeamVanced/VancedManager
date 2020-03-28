package com.vanced.manager.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class SectionPageAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val fragmentList : MutableList<Fragment> = ArrayList()
    private val titleList : MutableList<String> = ArrayList()

    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titleList[position]
    }
    fun addFragment(fragment: Fragment, title:String) {
        fragmentList.add(fragment)
        titleList.add(title)
    }
}