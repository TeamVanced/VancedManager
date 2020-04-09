package com.vanced.manager.ui

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.appbar.MaterialToolbar
import com.vanced.manager.R
import com.vanced.manager.ui.core.ThemeActivity
import com.vanced.manager.ui.fragments.VancedThemeSelectionFragment

class VancedInstallActivity : ThemeActivity() {

    lateinit var vancedThemeSelectionFragment: VancedThemeSelectionFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vanced_install)

        val toolbar : MaterialToolbar = findViewById(R.id.vanced_install_toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_backspace_black_24dp)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        supportActionBar!!.title = "Install"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        vancedThemeSelectionFragment = VancedThemeSelectionFragment()
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(android.R.anim.fade_in, R.animator.fragment_exit)
            .replace(R.id.vanced_install_frame, vancedThemeSelectionFragment)
            .commit()
    }
}
