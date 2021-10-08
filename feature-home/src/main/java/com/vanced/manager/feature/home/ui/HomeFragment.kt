package com.vanced.manager.feature.home.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.vanced.manager.core.ui.base.BindingFragment
import com.vanced.manager.feature.home.databinding.FragmentHomeBinding
import com.vanced.manager.feature.home.presentation.HomeViewModel
import com.vanced.manager.feature.home.ui.bind.bindData
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BindingFragment<FragmentHomeBinding>() {

    private val viewModel: HomeViewModel by viewModel()

    override fun binding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentHomeBinding.inflate(inflater, container, false)

    override fun otherSetups() {
        bindData(binding, viewModel)
    }
}