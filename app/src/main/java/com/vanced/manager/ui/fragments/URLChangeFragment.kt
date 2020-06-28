package com.vanced.manager.ui.fragments

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.preference.PreferenceManager
import com.dezlum.codelabs.getjson.GetJson
import com.google.android.material.button.MaterialButton
import com.vanced.manager.R
import com.vanced.manager.utils.InternetTools.getLatestVancedUrl

class URLChangeFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (dialog != null && dialog?.window != null) {
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        return inflater.inflate(R.layout.fragment_update_check, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val urlField = view.findViewById<EditText>(R.id.url_input)
        val prefs = PreferenceManager.getDefaultSharedPreferences(activity)
        urlField.hint = prefs.getString("install_url", activity?.let { getLatestVancedUrl(it) })
        view.findViewById<MaterialButton>(R.id.url_save).setOnClickListener {
            prefs.edit().putString("install_url", urlField.text.toString()).apply()
        }
        view.findViewById<MaterialButton>(R.id.url_reset).setOnClickListener {
            prefs.edit().putString("install_url", activity?.let { getLatestVancedUrl(it) }).apply()
        }
    }

}
