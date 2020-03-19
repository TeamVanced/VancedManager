package com.vanced.manager

import android.os.Bundle
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

        val navView : BottomNavigationView = findViewById(R.id.bottom_nav)

        homeFragment = HomeFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame_layout, homeFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
        val toolbar = findViewById(R.id.home_toolbar) as Toolbar?
        setSupportActionBar(toolbar)

        navView.setOnNavigationItemSelectedListener { item ->

            when (item.itemId) {

                R.id.home -> {

                    homeFragment = HomeFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.frame_layout, homeFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()
                    setSupportActionBar(toolbar)
                }

            }

            true
        }
    }
}
