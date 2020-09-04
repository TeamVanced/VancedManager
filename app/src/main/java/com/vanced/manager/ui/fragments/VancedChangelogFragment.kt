package com.vanced.manager.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.vanced.manager.R
import com.vanced.manager.databinding.FragmentVancedChangelogBinding
import com.vanced.manager.ui.viewmodels.HomeViewModel

class VancedChangelogFragment : Fragment() {

    private lateinit var binding: FragmentVancedChangelogBinding
    private val viewModel: HomeViewModel by viewModels()
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_vanced_changelog, container, false)
        binding.viewModel = viewModel
        return binding.root
    }
    
}
