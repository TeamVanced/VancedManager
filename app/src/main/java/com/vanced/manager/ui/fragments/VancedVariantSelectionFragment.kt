package com.vanced.manager.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.vanced.manager.R

class VancedVariantSelectionFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_vanced_variant_selection, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fragmentManager = activity?.supportFragmentManager

        val nextButton = getView()?.findViewById(R.id.vanced_next_to_language) as Button

        nextButton.setOnClickListener{
            fragmentManager
                ?.beginTransaction()
                ?.setCustomAnimations(R.animator.fragment_enter, R.animator.fragment_exit)
                ?.replace(R.id.vanced_install_frame, VancedLanguageSelectionFragment())
                ?.commit()
        }
    }
}


