package com.vanced.manager.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.vanced.manager.core.ui.base.BindingFragment
import com.vanced.manager.databinding.FragmentWelcomeBinding

class WelcomeFragment : BindingFragment<FragmentWelcomeBinding>() {

    override fun binding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentWelcomeBinding.inflate(inflater, container, false)

    override fun otherSetups() {
        bindData()
    }

    private fun bindData() {
        binding.welcomeGetStarted.setOnClickListener { navigateToWelcome() }
    }

    private fun navigateToWelcome() {
        findNavController().navigate(WelcomeFragmentDirections.welcomeToSelectApps())
    }
}