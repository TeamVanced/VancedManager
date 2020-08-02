package com.vanced.manager.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.CheckBox
import android.widget.LinearLayout
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.preference.PreferenceManager
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import com.github.kittinunf.fuel.coroutines.awaitString
import com.github.kittinunf.fuel.httpGet
import com.google.android.material.button.MaterialButton
import com.vanced.manager.R
import com.vanced.manager.utils.InternetTools.baseUrl
import kotlinx.coroutines.runBlocking
import java.util.*

class VancedLanguageSelectionFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.title = getString(R.string.install)
        return inflater.inflate(R.layout.fragment_vanced_language_selection, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        runBlocking {
            loadBoxes(view.findViewById(R.id.lang_button_ll))
        }
        view.findViewById<MaterialButton>(R.id.vanced_install_finish).setOnClickListener {
            runBlocking {
                val chosenLangs = mutableListOf("en")
                for (lang in getLangs()!!) {
                    if (view.findViewWithTag<CheckBox>(lang).isChecked) {
                        chosenLangs.add(lang)
                    }
                }
                PreferenceManager.getDefaultSharedPreferences(activity).edit()?.putString("lang", chosenLangs.joinToString())?.apply()
                view.findNavController().navigate(R.id.action_installTo_homeFragment)
            }

        }
    }

    private suspend fun getLangs(): Array<String>? {
        val langObj = Parser.default().parse(
            StringBuilder(
                "https://${PreferenceManager.getDefaultSharedPreferences(activity).getString("update_url", baseUrl)}/vanced.json".httpGet().awaitString()
            )
        ) as JsonObject
        return langObj.array<String>("langs")?.toTypedArray()
    }

    private suspend fun loadBoxes(ll: LinearLayout) {
        if (getLangs() != null) {
            for (lang in getLangs()!!) {
                val box: CheckBox = CheckBox(activity).apply {
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
