package com.vanced.manager.core.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.preference.PreferenceManager
import com.vanced.manager.core.base.BaseFragment

open class About : BaseFragment() {

    private var count = 0
    private var startMillSec: Long = 0

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.setOnTouchListener { _, event: MotionEvent ->

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
                    val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
                    val devSettings = prefs.getBoolean("devSettings", false)
                    if (!devSettings) {
                        Toast.makeText(requireContext(), "Dev options unlocked!", Toast.LENGTH_SHORT).show()
                        prefs.edit().putBoolean("devSettings", true).apply()
                    } else
                        Toast.makeText(requireContext(), "Dev options already unlocked", Toast.LENGTH_SHORT).show()

                }
                return@setOnTouchListener true
            }
            false
        }
    }

}