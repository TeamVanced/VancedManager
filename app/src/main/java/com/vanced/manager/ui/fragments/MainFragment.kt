package com.vanced.manager.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.tabs.TabLayout
import com.vanced.manager.R
import com.vanced.manager.databinding.FragmentMainBinding
import com.vanced.manager.ui.MainActivity
import com.vanced.manager.utils.AppUtils.installing
import kotlin.system.exitProcess

class MainFragment : Fragment() {
    
    private lateinit var binding: FragmentMainBinding
    private lateinit var navHost: NavController
    private lateinit var act: MainActivity
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navHostFragment = childFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        navHost = navHostFragment.navController
        act = requireActivity() as MainActivity

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if(!navHost.popBackStack()) {
                    exitProcess(0)
                }
            }
        }
        act.onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        val tabLayout = act.binding.mainTablayout

        installing.observe(viewLifecycleOwner, { value ->
            if (value)
                hideTab(tabLayout)
            else
                showTab(tabLayout)
        })
        
        navHost.addOnDestinationChangedListener { _, currFrag: NavDestination, _ ->
            setDisplayHomeAsUpEnabled(currFrag.id != R.id.home_fragment)
            if (currFrag.id != R.id.home_fragment)
                hideTab(tabLayout)
            else
                showTab(tabLayout)

        }

    }

    private fun hideTab(tabLayout: TabLayout) {
        val tabHide = AnimationUtils.loadAnimation(act, R.anim.tablayout_exit)
        if (tabLayout.visibility != View.GONE) {
            tabLayout.startAnimation(tabHide)
            tabLayout.visibility = View.GONE
        }
        act.binding.mainViewpager.isUserInputEnabled = false
    }

    private fun showTab(tabLayout: TabLayout) {
        val tabShow = AnimationUtils.loadAnimation(act, R.anim.tablayout_enter)
        if (tabLayout.visibility != View.VISIBLE) {
            tabLayout.visibility = View.VISIBLE
            tabLayout.startAnimation(tabShow)
        }
        act.binding.mainViewpager.isUserInputEnabled = true
    }
    
    private fun setDisplayHomeAsUpEnabled(isNeeded: Boolean) {
        act.binding.homeToolbar.navigationIcon = if (isNeeded) ContextCompat.getDrawable(act, R.drawable.ic_keyboard_backspace_black_24dp) else null
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (installing.value!!)
            return false

        when (item.itemId) {
            android.R.id.home -> {
                requireActivity().onBackPressedDispatcher.onBackPressed()
                return true
            }
            R.id.toolbar_about -> {
                navHost.navigate(HomeFragmentDirections.toAboutFragment())
                return true
            }
            R.id.toolbar_settings -> {
                navHost.navigate(HomeFragmentDirections.toSettingsFragment())
                return true
            }
            R.id.dev_settings -> {
                navHost.navigate(SettingsFragmentDirections.toDevSettingsFragment())
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }

        return false
    }
    
}
