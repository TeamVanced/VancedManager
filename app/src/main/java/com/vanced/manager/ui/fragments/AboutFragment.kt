package com.vanced.manager.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.preference.PreferenceManager
import com.vanced.manager.R
import com.vanced.manager.databinding.FragmentAboutBinding
import com.vanced.manager.ui.viewmodels.AboutViewModel

class AboutFragment : Fragment() {

    private lateinit var binding: FragmentAboutBinding
    private var count = 0
    private var startMillSec: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().title = getString(R.string.title_about)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_about, container, false)
        return binding.root
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel: AboutViewModel by viewModels()
        binding.viewModel = viewModel


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
