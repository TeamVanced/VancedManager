package com.vanced.manager.ui.fragments

import android.content.Context
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
import com.google.android.material.button.MaterialButton
import com.google.android.material.checkbox.MaterialCheckBox
import com.vanced.manager.R
import com.vanced.manager.core.App
import com.vanced.manager.core.downloader.VancedDownloader.downloadVanced
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
        val app = activity?.application as App
        langs = app.vanced?.array<String>("langs")?.value ?: mutableListOf("null")
        loadBoxes(view.findViewById(R.id.lang_button_ll))
        view.findViewById<MaterialButton>(R.id.vanced_install_finish).setOnClickListener {
            val chosenLangs = mutableListOf("en")
            if (!langs.contains("null"))
                langs.forEach { lang ->
                    if (view.findViewWithTag<MaterialCheckBox>(lang).isChecked) {
                        chosenLangs.add(lang)
                    }
                }
            with(requireActivity()) {
                getSharedPreferences("installPrefs", Context.MODE_PRIVATE)?.edit()?.apply {
                    putString("lang", chosenLangs.joinToString())?.apply()
                    putBoolean("valuesModified", true).apply()
                }
                downloadVanced(requireActivity())
            }
            view.findNavController().navigate(R.id.action_installTo_homeFragment)
        }
    }

    @ExperimentalStdlibApi
    private fun loadBoxes(ll: LinearLayout) {
        CoroutineScope(Dispatchers.Main).launch {
            if (this@VancedLanguageSelectionFragment::langs.isInitialized) {
                if (!langs.contains("null")) {
                    langs.forEach { lang ->
                        val loc = Locale(lang)
                        val box: MaterialCheckBox = MaterialCheckBox(requireActivity()).apply {
                            tag = lang
                            text = loc.getDisplayLanguage(loc).capitalize(Locale.ROOT)
                            textSize = 18F
                            typeface = ResourcesCompat.getFont(requireActivity(), R.font.exo_bold)
                        }
                        ll.addView(box, MATCH_PARENT, WRAP_CONTENT)
                    }
                }
            } else
                loadBoxes(ll)
        }
    }

}
