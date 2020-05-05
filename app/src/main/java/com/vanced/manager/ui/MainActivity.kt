package com.vanced.manager.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController

import com.vanced.manager.R
import com.vanced.manager.core.Main

class MainActivity : Main() {

    private var isParent = true

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.home_toolbar)
        setSupportActionBar(toolbar)

        val navHost = findNavController(R.id.bottom_nav_host)
        val appBarConfiguration = AppBarConfiguration(navHost.graph)
        toolbar.setupWithNavController(navHost, appBarConfiguration)

        navHost.addOnDestinationChangedListener{_, currfrag: NavDestination, _ ->
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
            R.id.dev_settings -> {
                navHost.navigate(R.id.toDevSettingsFragment)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
        return false
    }

    private fun setDisplayHomeAsUpEnabled(isNeeded: Boolean) {
        val toolbar: Toolbar = findViewById(R.id.home_toolbar)
        when {
            isNeeded -> toolbar.setNavigationIcon(R.drawable.ic_keyboard_backspace_black_24dp)
            else -> toolbar.navigationIcon = null
        }
    }
}
