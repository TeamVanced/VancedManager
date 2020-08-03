package com.vanced.manager.ui.fragments

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
import com.vanced.manager.utils.InternetTools.baseUrl
import com.vanced.manager.utils.JsonHelper.getJsonArray
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*

class VancedLanguageSelectionFragment : Fragment() {

    private val langs: MutableList<String?> = runBlocking { getJsonArray("${PreferenceManager.getDefaultSharedPreferences(activity).getString("install_url", baseUrl)}/vanced.json").string("langs").value }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.title = getString(R.string.install)
        return inflater.inflate(R.layout.fragment_vanced_language_selection, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadBoxes(view.findViewById(R.id.lang_button_ll))
        view.findViewById<MaterialButton>(R.id.vanced_install_finish).setOnClickListener {
            val chosenLangs = mutableListOf("en")
            for (lang in langs) {
                if (view.findViewWithTag<MaterialCheckBox>(lang).isChecked) {
                    if (lang != null) {
                        chosenLangs.add(lang)
                    }
                }
            }
            PreferenceManager.getDefaultSharedPreferences(activity).edit()?.putString("lang", chosenLangs.joinToString())?.apply()
            view.findNavController().navigate(R.id.action_installTo_homeFragment)
        }
    }



    private fun loadBoxes(ll: LinearLayout) = CoroutineScope(Dispatchers.Main).launch {
        for (lang in langs) {
            if (lang != null) {
                val box: MaterialCheckBox = MaterialCheckBox(activity).apply {
                    tag = lang
                    text = Locale(lang).displayLanguage
                    textSize = 16F
                    typeface = activity?.let { ResourcesCompat.getFont(it, R.font.exo_bold) }
                }
                ll.addView(box, MATCH_PARENT, WRAP_CONTENT)
            }
        }
    }

}
