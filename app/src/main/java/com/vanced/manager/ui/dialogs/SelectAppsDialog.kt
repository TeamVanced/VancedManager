package com.vanced.manager.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.edit
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import androidx.recyclerview.widget.LinearLayoutManager
import com.vanced.manager.R
import com.vanced.manager.adapter.SelectAppsAdapter
import com.vanced.manager.core.ui.base.BindingBottomSheetDialogFragment
import com.vanced.manager.databinding.DialogSelectAppsBinding

class SelectAppsDialog : BindingBottomSheetDialogFragment<DialogSelectAppsBinding>() {

    companion object {

        fun newInstance(): SelectAppsDialog = SelectAppsDialog().apply {
            arguments = Bundle()
        }
    }

    private val prefs by lazy { getDefaultSharedPreferences(requireActivity()) }

    override fun binding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = DialogSelectAppsBinding.inflate(inflater, container, false)

    override fun otherSetups() {
        bindData()
    }

    private fun bindData() {
        with(binding) {
            val ad = SelectAppsAdapter(requireActivity())
            selectAppsRecycler.apply {
                layoutManager = LinearLayoutManager(requireActivity())
                adapter = ad
                setHasFixedSize(true)
            }
            selectAppsSave.setOnClickListener {
                if (ad.apps.all { app -> !app.isChecked }) {
                    Toast.makeText(
                        requireActivity(),
                        R.string.select_at_least_one_app,
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
                prefs.edit {
                    ad.apps.forEach { app ->
                        putBoolean("enable_${app.tag}", app.isChecked)
                        putBoolean("${app.tag}_notifs", app.isChecked)
                    }
                }
                dismiss()
            }
        }
    }
}