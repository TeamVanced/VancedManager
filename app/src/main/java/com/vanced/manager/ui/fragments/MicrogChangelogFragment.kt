package com.vanced.manager.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.dezlum.codelabs.getjson.GetJson
import com.vanced.manager.R

class MicrogChangelogFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_microg_changelog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val changelogTxt = view.findViewById<TextView>(R.id.microg_changelog)

        val checkUrl = GetJson().AsJSONObject("https://vanced.app/api/v1/microg.json")
        val changelog = checkUrl.get("changelog").asString

        if (GetJson().isConnected(activity))
            changelogTxt.text = changelog
    }
}
