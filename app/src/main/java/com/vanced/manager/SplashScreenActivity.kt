package com.vanced.manager

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.vanced.manager.ui.MainActivity

class SplashScreenActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startActivity(
            Intent(this, MainActivity::class.java)
        )
    }

}