package com.vanced.manager.core.fragments

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.vanced.manager.R
import com.vanced.manager.ui.fragments.LanguageScrollviewFragment

open class LanguageInstall : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val finishButton = getView()?.findViewById(R.id.vanced_install_finish) as Button

        finishButton.setOnClickListener {
            view.findNavController().navigate(R.id.action_installTo_homeFragment)
        }

    }

}