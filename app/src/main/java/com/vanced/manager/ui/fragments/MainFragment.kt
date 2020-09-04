package com.vanced.manager.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.tabs.TabLayout
import com.vanced.manager.databinding.FragmentMainBinding
import com.vanced.manager.R

class MainFragment : Fragment() {
    
    private lateinit var binding: FragmentMainBinding
    private lateinit var navHost: NavController
    
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
    
        val appBarConfiguration = AppBarConfiguration(navHost.graph)
        requireActivity().findViewById<MaterialToolbar>(R.id.home_toolbar).setupWithNavController(navHost, appBarConfiguration)
        val tabLayout = requireActivity().findViewById<TabLayout>(R.id.main_tablayout)
        
        navHost.addOnDestinationChangedListener { _, currFrag: NavDestination, _ ->
            setDisplayHomeAsUpEnabled(currFrag.id != R.id.home_fragment)
            with (requireActivity()) {
                val tabHide = AnimationUtils.loadAnimation(this, R.anim.tablayout_exit)
                val tabShow = AnimationUtils.loadAnimation(this, R.anim.tablayout_enter)
                if (currFrag.id != R.id.home_fragment) {
                    if (tabLayout.visibility != View.GONE) {
                        tabLayout.startAnimation(tabHide)
                        tabLayout.visibility = View.GONE
                    }
                } else {
                   if (tabLayout.visibility != View.VISIBLE) {
                        tabLayout.visibility = View.VISIBLE
                        tabLayout.startAnimation(tabShow)
                    }
                }
            }
        }
    }
    
    private fun setDisplayHomeAsUpEnabled(isNeeded: Boolean) {
        with(requireActivity()) {
            findViewById<MaterialToolbar>(R.id.home_toolbar).navigationIcon = if (isNeeded) getDrawable(R.drawable.ic_keyboard_backspace_black_24dp) else null
        }
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                navHost.popBackStack()
                return true
            }
            R.id.toolbar_about -> {
                navHost.navigate(R.id.toAboutFragment)
                return true
            }
            R.id.toolbar_settings -> {
                navHost.navigate(R.id.action_settingsFragment)
                return true
            }
            R.id.dev_settings -> {
                navHost.navigate(R.id.toDevSettingsFragment)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }

        return false
    }
    
}
