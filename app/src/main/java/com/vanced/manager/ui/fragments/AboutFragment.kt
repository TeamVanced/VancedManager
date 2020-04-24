package com.vanced.manager.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.vanced.manager.R
import com.vanced.manager.core.fragments.About

/**
 * A simple [Fragment] subclass.
 */
class AboutFragment : About() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.title = getString(R.string.title_about)
        return inflater.inflate(R.layout.fragment_about, container, false)
    }

}
