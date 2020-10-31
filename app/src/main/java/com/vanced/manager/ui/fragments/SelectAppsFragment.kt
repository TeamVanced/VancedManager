package com.vanced.manager.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import androidx.recyclerview.widget.LinearLayoutManager
import com.vanced.manager.R
import com.vanced.manager.adapter.SelectAppsAdapter
import com.vanced.manager.databinding.FragmentSelectAppsBinding

class SelectAppsFragment : Fragment() {

    private lateinit var binding: FragmentSelectAppsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_select_apps, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val selectAdapter = SelectAppsAdapter(requireActivity())
        val prefs = getDefaultSharedPreferences(requireActivity())
        binding.selectAppsRecycler.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            setHasFixedSize(true)
            adapter = selectAdapter
        }

        binding.selectAppsFab.setOnClickListener {
            if (selectAdapter.apps.all { app -> !app.isChecked }) {
                Toast.makeText(requireActivity(), R.string.select_at_least_one_app, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            selectAdapter.apps.forEach { app ->
                prefs.edit().putBoolean("enable_${app.tag}", app.isChecked).apply()
            }

            findNavController().navigate(SelectAppsFragmentDirections.selectAppsToGrantRoot())
        }
    }

}