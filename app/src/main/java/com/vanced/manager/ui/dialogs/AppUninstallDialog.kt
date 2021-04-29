package com.vanced.manager.ui.dialogs

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.vanced.manager.R
import com.vanced.manager.core.ui.base.BindingDialogFragment
import com.vanced.manager.databinding.DialogAppUninstallBinding

class AppUninstallDialog : BindingDialogFragment<DialogAppUninstallBinding>() {

    companion object {

        private var TAG_APP_NAME: String? = null

        fun newInstance(appName: String?) : AppUninstallDialog = AppUninstallDialog().apply {
            arguments = Bundle().apply {
                TAG_APP_NAME = appName
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
        with(binding) {
            appUninstallConfirm.setOnClickListener {
                //uninstall instruction ??
                //dataModel?.appPkg?.let { it1 -> viewModel.uninstallPackage(it1) } (taken from original ExpandanbleAppListAdapter.kt)
                //but uninstallPackage method is not static so I would be forced to spawn a new HomeViewModel instance
            }
            appUninstallCancel.setOnClickListener {
                dismiss()
            }
            appUninstallMessage.text = getString(R.string.uninstall_app_text, TAG_APP_NAME)
        }
    }
}