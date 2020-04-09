package com.vanced.manager.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

import com.vanced.manager.R

class VancedLanguageSelectionFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_vanced_language_selection, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fragmentManager = childFragmentManager

        val finishButton = getView()?.findViewById(R.id.vanced_install_finish) as Button

        finishButton.setOnClickListener {
            activity?.finish()
        }

        fragmentManager
            .beginTransaction()
            .replace(R.id.language_choose_frame, LanguageScrollviewFragment())
            .commit()

    }

}
