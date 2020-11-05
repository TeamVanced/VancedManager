package com.vanced.manager.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.edit
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import com.topjohnwu.superuser.Shell
import com.vanced.manager.R
import com.vanced.manager.databinding.FragmentGrantRootBinding
import com.vanced.manager.ui.MainActivity

class GrantRootFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentGrantRootBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_grant_root, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.grantRootFinishFab.setOnClickListener(this)
        binding.grantRootFab.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.grant_root_fab -> {
                if (Shell.rootAccess()) {
                    getDefaultSharedPreferences(requireActivity()).edit { putString("vanced_variant", "root") }
                } else {
                    Toast.makeText(requireActivity(), R.string.root_not_granted, Toast.LENGTH_SHORT).show()
                }
            }
            R.id.grant_root_finish_fab -> {
                val intent = Intent(requireActivity(), MainActivity::class.java)
                intent.putExtra("firstLaunch", true)
                startActivity(intent)
                requireActivity().finish()
            }
        }
    }

}