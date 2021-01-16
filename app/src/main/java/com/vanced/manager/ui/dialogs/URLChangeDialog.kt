package com.vanced.manager.ui.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.edit
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import com.vanced.manager.core.ui.base.BindingDialogFragment
import com.vanced.manager.databinding.DialogCustomUrlBinding
import com.vanced.manager.utils.baseUrl
import com.vanced.manager.utils.loadJson
import kotlinx.coroutines.launch

class URLChangeDialog : BindingDialogFragment<DialogCustomUrlBinding>() {

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
                    getDefaultSharedPreferences(requireActivity()).getString("install_url", baseUrl)
                },
                TextView.BufferType.EDITABLE
            )
            urlSave.setOnClickListener {
                val finalUrl = if (urlInput.text?.startsWith("https://") == true || urlInput.text?.startsWith("http://") == true) {
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
            getDefaultSharedPreferences(requireActivity()).edit { putString("install_url", url) }
            loadJson(requireActivity())
            dismiss()
        }
    }
}
