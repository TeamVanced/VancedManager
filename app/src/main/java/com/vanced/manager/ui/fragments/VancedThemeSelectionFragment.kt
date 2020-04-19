package com.vanced.manager.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.navigation.findNavController
import com.vanced.manager.R

class VancedThemeSelectionFragment : Fragment() {

    override fun onStart() {
        super.onStart()
        activity?.title = getString(R.string.title_install)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_vanced_theme_selection, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fragmentManager = activity?.supportFragmentManager

        val nextButton = getView()?.findViewById(R.id.vanced_next_to_variant) as Button

        nextButton.setOnClickListener{
            view.findNavController().navigate(R.id.toInstallVariantFragment)
        }
    }

}
