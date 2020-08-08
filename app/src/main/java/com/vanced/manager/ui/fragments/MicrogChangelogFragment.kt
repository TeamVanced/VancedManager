package com.vanced.manager.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.vanced.manager.R
import com.vanced.manager.utils.InternetTools
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MicrogChangelogFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_microg_changelog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        CoroutineScope(Dispatchers.Main).launch {
            val baseUrl = PreferenceManager.getDefaultSharedPreferences(activity).getString("install_url", InternetTools.baseUrl)
            view.findViewById<TextView>(R.id.microg_changelog).text = InternetTools.getObjectFromJson("$baseUrl/microg.json", "changelog")
        }
    }
}
