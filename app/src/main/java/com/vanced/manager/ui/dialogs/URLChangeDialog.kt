package com.vanced.manager.ui.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.vanced.manager.core.ui.base.BindingDialogFragment
import com.vanced.manager.databinding.DialogCustomUrlBinding
import com.vanced.manager.utils.*
import kotlinx.coroutines.launch

class URLChangeDialog : BindingDialogFragment<DialogCustomUrlBinding>() {

    private val prefs by lazy { requireActivity().defPrefs }

    companion object {

        fun newInstance(): URLChangeDialog = URLChangeDialog().apply {
            arguments = Bundle()
        }
    }

    override fun binding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = DialogCustomUrlBinding.inflate(inflater, container, false)

    override fun otherSetups() {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        bindData()
    }

    private fun bindData() {
        with(binding) {
            urlInput.setText(
                if (arguments != null) {
                    arguments?.getString("url")
                } else {
                    prefs.installUrl
                },
                TextView.BufferType.EDITABLE
            )
            urlSave.setOnClickListener {
                val finalUrl =
                    if (urlInput.text?.startsWith("https://") == true || urlInput.text?.startsWith("http://") == true) {
                        urlInput.text?.removeSuffix("/").toString()
                    } else {
                        "https://${urlInput.text}".removeSuffix("/")
                    }
                saveUrl(finalUrl)
            }
            urlReset.setOnClickListener { saveUrl(baseUrl) }
        }
    }

    private fun saveUrl(url: String) {
        lifecycleScope.launch {
            prefs.installUrl = url
            baseInstallUrl = url
            loadJson(requireActivity())
            dismiss()
        }
    }
}
