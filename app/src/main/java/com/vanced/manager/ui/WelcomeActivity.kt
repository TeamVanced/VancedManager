package com.vanced.manager.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.vanced.manager.R

class WelcomeActivity : AppCompatActivity() {

    private val navHost by lazy { findNavController(R.id.welcome_navhost) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
    }

    override fun onBackPressed() {
        if (!navHost.popBackStack())
            finish()
    }
}