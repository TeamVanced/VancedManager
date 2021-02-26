package com.vanced.manager.ui.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import com.vanced.manager.R
import com.vanced.manager.core.ui.base.BindingDialogFragment
import com.vanced.manager.databinding.DialogAppInfoBinding

class AppInfoDialog : BindingDialogFragment<DialogAppInfoBinding>() {

    companion object {

        private const val TAG_APP_NAME = "TAG_APP_NAME"
        private const val TAG_APP_ICON = "TAG_APP_ICON"
        private const val TAG_CHANGELOG = "TAG_CHANGELOG"

        fun newInstance(
            appName: String?,
            @DrawableRes appIcon: Int?,
            changelog: String?
        ): AppInfoDialog = AppInfoDialog().apply {
            arguments = Bundle().apply {
                putString(TAG_APP_NAME, appName)
                putString(TAG_CHANGELOG, changelog)
                if (appIcon != null) {
                    putInt(TAG_APP_ICON, appIcon)
                }
            }
        }
    }

    override fun binding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = DialogAppInfoBinding.inflate(inflater, container, false)

    override fun otherSetups() {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        bindData()
    }

    private fun bindData() {
        with(binding) {
            aboutAppName.text = getString(R.string.about_app, arguments?.getString(TAG_APP_NAME))
            aboutAppChangelog.text = arguments?.getString(TAG_CHANGELOG)
            arguments?.getInt(TAG_APP_ICON)?.let { aboutAppImage.setImageResource(it) }
        }
    }
}