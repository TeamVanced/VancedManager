package com.vanced.manager.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.vanced.manager.R
import com.vanced.manager.ui.core.ThemeActivity

class MainActivity : ThemeActivity() {

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
        navBar.setupWithNavController(navHost)

        navBar.setOnNavigationItemSelectedListener{ item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    navHost.navigate(R.id.action_settingsFragment_to_homeFragment)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_settings -> {
                    navHost.navigate(R.id.action_homeFragment_to_settingsFragment)
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }

        navBar.setOnNavigationItemReselectedListener {
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
