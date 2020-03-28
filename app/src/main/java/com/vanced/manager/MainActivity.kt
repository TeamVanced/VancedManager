package com.vanced.manager

import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentTransaction

class MainActivity : AppCompatActivity() {

    lateinit var homeFragment: HomeFragment
    lateinit var settingsFragment: SettingsFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val prefs = getSharedPreferences("prefs", MODE_PRIVATE)
        val firstStart = prefs.getBoolean("firstStart", true)
        if (firstStart) {
            showGayDialog()
        }

        val navView : BottomNavigationView = findViewById(R.id.bottom_nav)

        homeFragment = HomeFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame_layout, homeFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
        val toolbar = findViewById(R.id.home_toolbar) as Toolbar?
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        navView.setOnNavigationItemSelectedListener { item ->

            when (item.itemId) {

                R.id.navigation_home -> {

                    homeFragment = HomeFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.frame_layout, homeFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()
                    setSupportActionBar(toolbar)
                }
                R.id.navigation_settings -> {

                    settingsFragment = SettingsFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.frame_layout, settingsFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()
                }

            }

            true
        }
    }
    private fun showGayDialog() {
        AlertDialog.Builder(this)
            .setTitle("Gay Alert!")
            .setMessage("Warning!\nIf you didn't download this app from vanced.app or github,\nIt may be infected with malicious code. Make sure to have Official version or be gay")
            .setPositiveButton("close"
            ) { dialog, which -> dialog.dismiss() }
            .create().show()

        val prefs = getSharedPreferences("prefs", MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putBoolean("firstStart", false)
        editor.apply()
    }
}
