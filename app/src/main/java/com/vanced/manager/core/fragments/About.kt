package com.vanced.manager.core.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.preference.PreferenceManager
import com.vanced.manager.R
import com.vanced.manager.core.base.BaseFragment

open class About : BaseFragment() {

    private var count = 0
    private var startMillSec: Long = 0

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val aboutHeader: LinearLayout = view.findViewById(R.id.about_header_layout)
        aboutHeader.setOnTouchListener { _, event: MotionEvent ->

            val eventAction = event.action
            if (eventAction == MotionEvent.ACTION_UP) {
                val time = System.currentTimeMillis()
                if (startMillSec == 0L || time - startMillSec > 3000) {
                    startMillSec = time
                    count = 1
                } else {
                    count++
                }

                if (count == 5) {
                    Toast.makeText(requireContext(), "Dev options unlocked!", Toast.LENGTH_SHORT).show()
                    val prefs =
                        PreferenceManager.getDefaultSharedPreferences(
                            requireContext()
                        )
                    prefs.edit().putBoolean("devSettings", true).apply()
                }
                return@setOnTouchListener true
            }
            false
        }

        val githubSource = view.findViewById<Button>(R.id.about_github_button)
        val license = view.findViewById<Button>(R.id.about_license_button)

        githubSource.setOnClickListener {
            openUrl("https://github.com/YTvanced/VancedInstaller", R.color.GitHub)
        }

        license.setOnClickListener {
            openUrl("https://raw.githubusercontent.com/YTVanced/VancedInstaller/dev/LICENSE", R.color.GitHub)
        }
    }

}