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
import androidx.navigation.NavDestination
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.MaterialToolbar
import com.vanced.manager.databinding.FragmentMainBinding
import com.vanced.manager.R

class MainFragment : Fragment() {
    
    private lateinit var binding: FragmentMainBinding
    private val navHost by lazy { activity?.findNavController(R.id.nav_host) }
    
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
    
        val appBarConfiguration = AppBarConfiguration(navHost?.graph!!)
        activity?.findViewById<MaterialToolbar>(R.id.home_toolbar)?.setupWithNavController(navHost!!, appBarConfiguration)
        
        navHost?.addOnDestinationChangedListener { _, currFrag: NavDestination, _ ->
            setDisplayHomeAsUpEnabled(currFrag.id != R.id.home_fragment)
            with (activity) {
                val tabHide = this.let { AnimationUtils.loadAnimation(it, R.anim.tablayout_exit) }
                val tabShow = this.let { AnimationUtils.loadAnimation(it, R.anim.tablayout_enter) }
                if (currFrag.id != R.id.home_fragment) {
                    this?.findViewById<LinearLayout>(R.id.variant_tab_container)?.startAnimation(tabHide)
                    this?.findViewById<LinearLayout>(R.id.variant_tab_container)?.visibility = View.GONE
                } else {
                    this?.findViewById<LinearLayout>(R.id.variant_tab_container)?.visibility = View.VISIBLE
                    this?.findViewById<LinearLayout>(R.id.variant_tab_container)?.startAnimation(tabShow)
                }
            }
        }
    }
    
    private fun setDisplayHomeAsUpEnabled(isNeeded: Boolean) {
        activity?.findViewById<MaterialToolbar>(R.id.home_toolbar)?.navigationIcon = if (isNeeded) activity?.getDrawable(R.drawable.ic_keyboard_backspace_black_24dp) else null
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.toolbar_about -> {
                navHost?.navigate(R.id.toAboutFragment)
                return true
            }
            R.id.toolbar_settings -> {
                navHost?.navigate(R.id.action_settingsFragment)
                return true
            }
            R.id.dev_settings -> {
                navHost?.navigate(R.id.toDevSettingsFragment)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }

        return false
    }
    
}
