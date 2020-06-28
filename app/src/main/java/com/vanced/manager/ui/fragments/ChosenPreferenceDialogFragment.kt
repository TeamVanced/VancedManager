package com.vanced.manager.ui.fragments

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment

import com.vanced.manager.R

class ChosenPreferenceDialogFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (dialog != null && dialog?.window != null) {
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        return inflater.inflate(R.layout.fragment_chosen_preferences, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val closebtn = view.findViewById<Button>(R.id.chosen_prefs_close)
        val resetbtn = view.findViewById<Button>(R.id.chosen_prefs_reset)
        val themetxt = view.findViewById<TextView>(R.id.chosen_theme)
        val langtxt = view.findViewById<TextView>(R.id.chosen_lang)

        val prefs = activity?.getSharedPreferences("installPrefs", Context.MODE_PRIVATE)

        themetxt.text = activity?.getString(R.string.chosen_theme, prefs?.getString("theme", "dark"))
        langtxt.text = activity?.getString(R.string.chosen_lang, prefs?.getString("lang", "en"))

        closebtn.setOnClickListener { dismiss() }

        resetbtn.setOnClickListener {
            prefs?.edit()?.putString("theme", "dark")?.apply()
            prefs?.edit()?.putString("lang", "en")?.apply()
            prefs?.edit()?.putBoolean("valuesModified", false)?.apply()
            dismiss()
        }



    }



}
