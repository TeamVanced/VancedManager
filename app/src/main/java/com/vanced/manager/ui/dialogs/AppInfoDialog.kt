package com.vanced.manager.ui.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.vanced.manager.R
import com.vanced.manager.databinding.DialogAppInfoBinding

class AppInfoDialog(
        private val appName: String?,
        private val appIcon: Drawable?,
        private val changelog: String?
) : DialogFragment() {

    private lateinit var binding: DialogAppInfoBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (dialog != null && dialog?.window != null) {
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_app_info, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.aboutAppName.text = requireActivity().getString(R.string.about_app, appName)
        binding.aboutAppImage.setImageDrawable(appIcon)
        binding.aboutAppChangelog.text = changelog
    }

}