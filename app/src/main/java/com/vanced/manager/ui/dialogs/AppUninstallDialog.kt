package com.vanced.manager.ui.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.vanced.manager.R
import com.vanced.manager.core.ui.base.BindingDialogFragment
import com.vanced.manager.databinding.DialogAppUninstallBinding
import com.vanced.manager.utils.PackageHelper

class AppUninstallDialog : BindingDialogFragment<DialogAppUninstallBinding>() {

    companion object {

        private const val TAG_APP_NAME = "APP_NAME"
        private const val TAG_APP_PACKAGE = "APP_PACKAGE"


        fun newInstance(
            appName: String?,
            appPackage: String?,
        ) = AppUninstallDialog().apply {
            arguments = Bundle().apply {
                putString(TAG_APP_NAME, appName)
                putString(TAG_APP_PACKAGE, appPackage)
            }
        }
    }

    override fun binding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = DialogAppUninstallBinding.inflate(inflater, container, false)

    override fun otherSetups() {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        bindData()
    }

    private fun bindData() {
        val appName = arguments?.getString(TAG_APP_NAME)
        val appPackage = arguments?.getString(TAG_APP_PACKAGE)
        with(binding) {
            appUninstallConfirm.setOnClickListener {
                if (appPackage != null) {
                    PackageHelper.uninstallApk(
                        pkg = appPackage,
                        context = requireActivity()
                    )
                }
                dismiss()
            }
            appUninstallCancel.setOnClickListener {
                dismiss()
            }
            appUninstallMessage.text = getString(R.string.uninstall_app_text, appName)
        }
    }
}