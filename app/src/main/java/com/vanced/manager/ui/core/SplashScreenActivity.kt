package com.vanced.manager.ui.core

import android.app.ActivityManager
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.vanced.manager.R
import com.vanced.manager.ui.MainActivity

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.SplashTheme)
        if (android.os.Build.VERSION.SDK_INT < 28) {
            setTaskBG()
        }

        super.onCreate(savedInstanceState)

        startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
        finish()
    }

    private fun setTaskBG() {
        val icon = BitmapFactory.decodeResource(resources, R.drawable.splash192)
        val label = getString(R.string.app_name)
        val color = ResourcesCompat.getColor(resources, R.color.Black, null)
        val taskDec: ActivityManager.TaskDescription = ActivityManager.TaskDescription(label, icon, color)
        setTaskDescription(taskDec)
    }
}
