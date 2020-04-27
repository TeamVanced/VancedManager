package com.vanced.manager.ui

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.vanced.manager.R
import com.vanced.manager.core.Main

class MainActivity : Main() {

    private var isParent = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: MaterialToolbar = findViewById(R.id.home_toolbar)
        setSupportActionBar(toolbar)

        val navHost = findNavController(R.id.bottom_nav_host)
        val appBarConfiguration = AppBarConfiguration(navHost.graph)
        toolbar.setupWithNavController(navHost, appBarConfiguration)

        /*
        val navBar = findViewById<BottomNavigationView>(R.id.bottom_nav)
        navBar.setupWithNavController(navHost)

        navBar.setOnNavigationItemSelectedListener{ item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    navHost.navigate(R.id.action_homeFragment)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_settings -> {
                    navHost.navigate(R.id.action_settingsFragment)
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }

        navBar.setOnNavigationItemReselectedListener {
        }
        */

        navHost.addOnDestinationChangedListener{_, currfrag: NavDestination, _ ->
            /*
            val navBarHide: Animation = AnimationUtils.loadAnimation(applicationContext, R.anim.navbar_exit)
            val navBarShow: Animation = AnimationUtils.loadAnimation(applicationContext, R.anim.navbar_enter)
            when (currfrag.id) {

                R.id.home_fragment, R.id.settings_fragment -> {

                    if (navBar.visibility != View.VISIBLE) {
                        navBar.visibility = View.VISIBLE
                        navBar.startAnimation(navBarShow)
                    }

                }
                else -> {
                    if (navBar.visibility != View.GONE) {
                        navBar.startAnimation(navBarHide)
                        navBar.visibility = View.GONE
                    }
                }

            }
            */
            isParent = when (currfrag.id) {
                R.id.home_fragment -> true
                else -> false
            }

            setDisplayHomeAsUpEnabled(!isParent)

        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navHost = findNavController(R.id.bottom_nav_host)
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
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
            R.id.secret_settings -> {
                navHost.navigate(R.id.toSecretSettingsFragment)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
        return false
    }

    private fun setDisplayHomeAsUpEnabled(isNeeded: Boolean) {
        val toolbar: MaterialToolbar = findViewById(R.id.home_toolbar)
        when {
            isNeeded -> toolbar.setNavigationIcon(R.drawable.ic_keyboard_backspace_black_24dp)
            else -> toolbar.navigationIcon = null
        }
    }
}
