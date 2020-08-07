package com.vanced.manager.ui.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.preference.PreferenceManager
import com.google.android.material.button.MaterialButton
import com.google.android.material.checkbox.MaterialCheckBox
import com.vanced.manager.R
import com.vanced.manager.core.downloader.VancedDownloadService
import com.vanced.manager.utils.InternetTools.baseUrl
import com.vanced.manager.utils.InternetTools.getArrayFromJson
import kotlinx.coroutines.*
import java.util.*

class VancedLanguageSelectionFragment : Fragment() {

    private lateinit var langs: MutableList<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.title = getString(R.string.install)
        return inflater.inflate(R.layout.fragment_vanced_language_selection, container, false)
    }

    @ExperimentalStdlibApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        CoroutineScope(Dispatchers.IO).launch {
            langs = getArrayFromJson("${PreferenceManager.getDefaultSharedPreferences(activity).getString("install_url", baseUrl)}/vanced.json", "langs")
        }
        loadBoxes(view.findViewById(R.id.lang_button_ll))
        view.findViewById<MaterialButton>(R.id.vanced_install_finish).setOnClickListener {
            val chosenLangs = mutableListOf("en")
            if (!langs.contains("null"))
                langs.forEach { lang ->
                    if (view.findViewWithTag<MaterialCheckBox>(lang).isChecked) {
                        chosenLangs.add(lang)
                    }
                }
            activity?.getSharedPreferences("installPrefs", Context.MODE_PRIVATE)?.edit()?.apply {
                putString("lang", chosenLangs.joinToString())?.apply()
                putBoolean("valuesModified", true).apply()
            }
            activity?.startService(Intent(activity, VancedDownloadService::class.java))
            view.findNavController().navigate(R.id.action_installTo_homeFragment)
        }
    }

    @ExperimentalStdlibApi
    private fun loadBoxes(ll: LinearLayout) = CoroutineScope(Dispatchers.Main).launch {
        if (!langs.contains("null")) {
            langs.forEach { lang ->
                val loc = Locale(lang)
                val box: MaterialCheckBox = MaterialCheckBox(activity).apply {
                    tag = lang
                    text = loc.getDisplayLanguage(loc).capitalize(Locale.ROOT)
                    textSize = 18F
                    typeface = activity?.let { ResourcesCompat.getFont(it, R.font.exo_bold) }
                }
                ll.addView(box, MATCH_PARENT, WRAP_CONTENT)
            }
        }
    }

}
