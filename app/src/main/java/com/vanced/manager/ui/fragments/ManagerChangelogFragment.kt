package com.vanced.manager.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.dezlum.codelabs.getjson.GetJson
import com.vanced.manager.R

class ManagerChangelogFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_manager_changelog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val changelogTxt = view.findViewById<TextView>(R.id.manager_changelog)

        if (GetJson().isConnected(activity)) {
            val checkUrl = GetJson().AsJSONObject("https://vanced.app/api/v1/manager.json")
            val changelog = checkUrl.get("changelog").asString
            changelogTxt.text = changelog
        }
    }
}
