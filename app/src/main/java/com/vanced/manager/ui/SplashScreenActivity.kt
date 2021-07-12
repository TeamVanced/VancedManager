package com.vanced.manager.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity

class SplashScreenActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startActivity(
            Intent(this, MainActivity::class.java)
        )
    }

}