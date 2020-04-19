package com.vanced.manager.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.findNavController

import com.vanced.manager.R
import kotlinx.android.synthetic.main.fragment_vanced_language_selection.view.*

class VancedLanguageSelectionFragment : Fragment() {

    override fun onStart() {
        super.onStart()
        activity?.title = getString(R.string.title_install)
    }

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
            view.findNavController().navigate(R.id.action_installTo_homeFragment)
        }

        fragmentManager
            .beginTransaction()
            .replace(R.id.language_choose_frame, LanguageScrollviewFragment())
            .commit()

    }

}
