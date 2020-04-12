package com.vanced.manager.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
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

    private var currentFragment: Fragment? = null
    private var activeFragment: Int = R.id.navigation_home

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        savedInstanceState?.let {
            activeFragment = it.getInt(CURRENT_FRAGMENT, R.id.navigation_home)
        }

        val toolbar: MaterialToolbar = findViewById(R.id.home_toolbar)
        setSupportActionBar(toolbar)

        val prefs = getSharedPreferences("prefs", MODE_PRIVATE)
        val firstStart = prefs.getBoolean("firstStart", true)
        if (firstStart) {
            showSecurityDialog()
        }

        when (activeFragment) {
            R.id.navigation_home -> currentFragment = homeFragment
            R.id.navigation_settings -> currentFragment = settingsFragment
        }

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.frame_layout, settingsFragment).hide(settingsFragment)
                .add(R.id.frame_layout, homeFragment).hide(homeFragment)
                .show(currentFragment!!)
                .commit()
        }

        val navView: BottomNavigationView = findViewById(R.id.bottom_nav)

        navView.setOnNavigationItemSelectedListener{
            setFragments(it.itemId)
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

    private fun setFragments(itemId: Int): Boolean {
        activeFragment = itemId
        when (itemId) {
            R.id.navigation_home -> {
                if (currentFragment is HomeFragment) {
                    return false
                }
                supportFragmentManager
                    .beginTransaction()
                    .hide(currentFragment!!)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .show(homeFragment)
                    .commit()

                currentFragment = homeFragment
            }
            R.id.navigation_settings -> {
                if (currentFragment is SettingsFragment) {
                    return false
                }
                supportFragmentManager
                    .beginTransaction()
                    .hide(currentFragment!!)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .show(settingsFragment)
                    .commit()

                currentFragment = settingsFragment
            }
        }
        return true
    }

    companion object{
        const val CURRENT_FRAGMENT = "current_fragment"
    }

}
