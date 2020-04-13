package com.vanced.manager.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.appbar.MaterialToolbar
import com.vanced.manager.ui.fragments.HomeFragment
import com.vanced.manager.R
import com.vanced.manager.ui.fragments.SettingsFragment
import com.vanced.manager.ui.core.ThemeActivity

class MainActivity : ThemeActivity() {

    private val homeFragment: HomeFragment = HomeFragment()
    private val settingsFragment: SettingsFragment = SettingsFragment()

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

        supportFragmentManager
            .beginTransaction()
            .add(R.id.frame_layout, settingsFragment).hide(settingsFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .add(R.id.frame_layout, homeFragment)
            .commit()
        supportActionBar?.title = getString(R.string.title_home)

        val navView: BottomNavigationView = findViewById(R.id.bottom_nav)

        navView.setOnNavigationItemSelectedListener{ item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    supportFragmentManager
                        .beginTransaction()
                        .hide(settingsFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .add(homeFragment)
                        .commit()
                    supportActionBar?.title = getString(R.string.title_home)

                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_settings -> {
                    supportFragmentManager
                        .beginTransaction()
                        .hide(homeFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .add(settingsFragment)
                        .commit()
                    supportActionBar?.title = getString(R.string.title_settings)

                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }

        navView.setOnNavigationItemReselectedListener {
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return super .onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.about -> {
            val intent = Intent(this, AboutActivity::class.java)
            startActivity(intent)
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
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
