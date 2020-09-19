package com.vanced.manager.ui.fragments

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.google.android.material.button.MaterialButton
import com.vanced.manager.R
import com.vanced.manager.utils.InternetTools.baseUrl

class URLChangeFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (dialog != null && dialog?.window != null) {
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        return inflater.inflate(R.layout.fragment_custom_url, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val urlField = view.findViewById<EditText>(R.id.url_input)
        val prefs = PreferenceManager.getDefaultSharedPreferences(activity)
        urlField.setText(prefs.getString("install_url", baseUrl), TextView.BufferType.EDITABLE)
        view.findViewById<MaterialButton>(R.id.url_save).setOnClickListener {
            val finalUrl =
                if (urlField.text.startsWith("https://") || urlField.text.startsWith("http://"))
                    urlField.text.removeSuffix("/").toString()
                else
                    "https://${urlField.text}".removeSuffix("/")


            prefs.edit().putString("install_url", finalUrl).apply()
            dismiss()
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        view.findViewById<MaterialButton>(R.id.url_reset).setOnClickListener {
            prefs.edit().putString("install_url", baseUrl).apply()
            dismiss()
        }
    }

}
