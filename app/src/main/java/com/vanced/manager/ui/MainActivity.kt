package com.vanced.manager.ui

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AlertDialog
import androidx.core.view.forEach
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.vanced.manager.R
import com.vanced.manager.ui.core.ThemeActivity

class MainActivity : ThemeActivity() {

    private var isParent = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: MaterialToolbar = findViewById(R.id.home_toolbar)
        setSupportActionBar(toolbar)

        val prefs = getSharedPreferences("prefs", MODE_PRIVATE)
        val firstStart = prefs.getBoolean("firstStart", true)
        if (firstStart) {
            showSecurityDialog()
        }

        val navHost = findNavController(R.id.bottom_nav_host)
        val navBar = findViewById<BottomNavigationView>(R.id.bottom_nav)
        val appBarConfiguration = AppBarConfiguration(navHost.graph)
        toolbar.setupWithNavController(navHost, appBarConfiguration)
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

        navHost.addOnDestinationChangedListener{_, currfrag: NavDestination, _ ->
            val navBarHide: Animation = AnimationUtils.loadAnimation(applicationContext, R.anim.navbar_exit)
            val navBarShow: Animation = AnimationUtils.loadAnimation(applicationContext, R.anim.navbar_enter)
            when (currfrag.id) {

                R.id.home_fragment, R.id.settings_fragment -> {
                    navBar.visibility = View.VISIBLE
                    navBar.startAnimation(navBarShow)

                }
                else -> {
                    navBar.startAnimation(navBarHide)
                    navBar.visibility = View.INVISIBLE
                }

            }
            isParent = when (currfrag.id) {
                R.id.home_fragment, R.id.settings_fragment -> true
                else -> false
            }

            setDisplayHomeAsUpEnabled(!isParent)

            navBar.menu.forEach {
                if (it.itemId == currfrag.id) {
                    it.isChecked = true
                }
            }

        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                findNavController(R.id.bottom_nav_host).navigate(R.id.action_homeFragment)
            }
            else -> super.onOptionsItemSelected(item)

        }
        return true
    }
    
    private fun setDisplayHomeAsUpEnabled(isNeeded: Boolean) {
        val toolbar: MaterialToolbar = findViewById(R.id.home_toolbar)
        when {
            isNeeded -> toolbar.setNavigationIcon(R.drawable.ic_keyboard_backspace_black_24dp)
            else -> toolbar.navigationIcon = null
        }
    }

    private fun showSecurityDialog() {
        AlertDialog.Builder(this)
            .setTitle("Welcome!")
            .setMessage("Before we implement a proper security system to check whether app was modified or not, please be sure that you downloaded manager from vanced.app/github")
            .setPositiveButton("close"
            ) { dialog, _ -> dialog.dismiss() }
            .create().show()

        val prefs = getSharedPreferences("prefs", MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putBoolean("firstStart", false)
        editor.apply()
    }

}
