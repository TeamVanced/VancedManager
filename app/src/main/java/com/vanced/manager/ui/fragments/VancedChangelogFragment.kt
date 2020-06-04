package com.vanced.manager.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.dezlum.codelabs.getjson.GetJson
import com.vanced.manager.R

class VancedChangelogFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_vanced_changelog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val changelogTxt = view.findViewById<TextView>(R.id.vanced_changelog)

        val checkUrl = GetJson().AsJSONObject("https://vanced.app/api/v1/changelog/15_05_54.json")
        val changelog = checkUrl.get("message").asString

        if (GetJson().isConnected(activity))
            changelogTxt.text = changelog
    }

}
