package com.vanced.manager.core.fragments

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.vanced.manager.R

open class VariantInstall : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nextButton = getView()?.findViewById(R.id.vanced_next_to_language) as Button

        nextButton.setOnClickListener {
            view.findNavController().navigate(R.id.toInstallLanguageFragment)
        }
    }
}