package com.vanced.manager.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.edit
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import com.topjohnwu.superuser.Shell
import com.vanced.manager.R
import com.vanced.manager.core.ui.base.BindingFragment
import com.vanced.manager.databinding.FragmentGrantRootBinding
import com.vanced.manager.ui.MainActivity

class GrantRootFragment : BindingFragment<FragmentGrantRootBinding>() {

    override fun binding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentGrantRootBinding.inflate(inflater, container, false)

    override fun otherSetups() {
        bindData()
    }

    private fun bindData() {
        with(binding) {
            grantRootFinishFab.setOnClickListener { navigateToFirstLaunch() }
            grantRootFab.setOnClickListener { grantRoot() }
        }
    }

    private fun navigateToFirstLaunch() {
        val intent = Intent(requireActivity(), MainActivity::class.java)
        intent.putExtra("firstLaunch", true)
        startActivity(intent)
        requireActivity().finish()
    }

    private fun grantRoot() {
        if (Shell.rootAccess()) {
            getDefaultSharedPreferences(requireActivity()).edit {
                putString(
                    "vanced_variant",
                    "root"
                )
            }
            navigateToFirstLaunch()
        } else {
            Toast.makeText(requireActivity(), R.string.root_not_granted, Toast.LENGTH_SHORT).show()
        }
    }
}