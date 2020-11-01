package com.vanced.manager.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.vanced.manager.R
import com.vanced.manager.adapter.SelectAppsAdapter
import com.vanced.manager.databinding.DialogSelectAppsBinding

class SelectAppsDialog : BottomSheetDialogFragment() {

    private lateinit var binding: DialogSelectAppsBinding
    private val prefs by lazy { getDefaultSharedPreferences(requireActivity()) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_select_apps, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val ad = SelectAppsAdapter(requireActivity())
        binding.selectAppsRecycler.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = ad
            setHasFixedSize(true)
        }
        binding.selectAppsSave.setOnClickListener {
            if (ad.apps.all { app -> !app.isChecked }) {
                Toast.makeText(requireActivity(), R.string.select_at_least_one_app, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            ad.apps.forEach { app ->
                prefs.edit().putBoolean("enable_${app.tag}", app.isChecked).apply()
            }
            dismiss()
        }
    }

}