package com.vanced.manager.ui.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.edit
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import com.google.android.material.button.MaterialButton
import com.vanced.manager.R
import com.vanced.manager.utils.Extensions.fetchData
import com.vanced.manager.utils.InternetTools.baseUrl
import kotlinx.coroutines.launch

class URLChangeDialog : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (dialog != null && dialog?.window != null) {
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        return inflater.inflate(R.layout.dialog_custom_url, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val urlField = view.findViewById<EditText>(R.id.url_input)
        val fieldTxt = if (arguments != null) arguments?.getString("url") else getDefaultSharedPreferences(requireActivity()).getString("install_url", baseUrl)
        urlField.setText(fieldTxt, TextView.BufferType.EDITABLE)
        view.findViewById<MaterialButton>(R.id.url_save).setOnClickListener {
            val finalUrl =
                if (urlField.text.startsWith("https://") || urlField.text.startsWith("http://"))
                    urlField.text.removeSuffix("/").toString()
                else
                    "https://${urlField.text}".removeSuffix("/")

            saveUrl(finalUrl)
        }
        view.findViewById<MaterialButton>(R.id.url_reset).setOnClickListener { saveUrl(baseUrl) }
    }

    private fun saveUrl(url: String) {
        lifecycleScope.launch {
            getDefaultSharedPreferences(requireActivity()).edit { putString("install_url", url) }
            requireActivity().fetchData()
            dismiss()
        }
    }
}
