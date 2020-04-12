package com.vanced.manager.ui

import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import com.google.android.material.appbar.MaterialToolbar
import com.vanced.manager.R
import com.vanced.manager.ui.core.ThemeActivity
import com.vanced.manager.ui.fragments.AboutFragment
import com.vanced.manager.ui.fragments.VancedThemeSelectionFragment

class AboutActivity : ThemeActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        val toolbar : MaterialToolbar = findViewById(R.id.about_toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_backspace_black_24dp)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        supportActionBar!!.title = "About"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(android.R.anim.fade_in, R.animator.fragment_exit)
            .replace(R.id.frame_layout_about, AboutFragment())
            .commit()
    }
}
