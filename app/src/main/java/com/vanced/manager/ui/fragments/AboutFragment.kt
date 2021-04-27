package com.vanced.manager.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.edit
import androidx.fragment.app.viewModels
import androidx.preference.PreferenceManager
import com.vanced.manager.R
import com.vanced.manager.core.ui.base.BindingFragment
import com.vanced.manager.core.ui.ext.showDialog
import com.vanced.manager.databinding.FragmentAboutBinding
import com.vanced.manager.ui.dialogs.AppInfoDialog
import com.vanced.manager.ui.viewmodels.AboutViewModel
import com.vanced.manager.utils.manager

class AboutFragment : BindingFragment<FragmentAboutBinding>() {

    private val viewModel: AboutViewModel by viewModels()
    private var count = 0
    private var startMillSec: Long = 0

    override fun binding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentAboutBinding.inflate(inflater, container, false)

    override fun otherSetups() {
        dataBind()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun dataBind() {
        requireActivity().title = getString(R.string.title_about)
        binding.aboutVersionCard.setOnClickListener {
            showDialog(
                AppInfoDialog.newInstance(
                    appName = getString(R.string.app_name),
                    appIcon = R.mipmap.ic_launcher,
                    changelog = manager.value?.string("changelog")
                )
            )
        }
        binding.root.setOnTouchListener { _, event: MotionEvent ->
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
                        Toast.makeText(
                            requireContext(),
                            "Dev options unlocked!",
                            Toast.LENGTH_SHORT
                        ).show()
                        prefs.edit { putBoolean("devSettings", true) }
                    } else
                        Toast.makeText(
                            requireContext(),
                            "Dev options already unlocked",
                            Toast.LENGTH_SHORT
                        ).show()

                }
                return@setOnTouchListener true
            }
            false
        }
        binding.aboutGithubButton.setOnClickListener { viewModel.openUrl("https://github.com/YTVanced/VancedInstaller") }
        binding.aboutLicenseButton.setOnClickListener { viewModel.openUrl("https://raw.githubusercontent.com/YTVanced/VancedInstaller/dev/LICENSE") }
    }
}
