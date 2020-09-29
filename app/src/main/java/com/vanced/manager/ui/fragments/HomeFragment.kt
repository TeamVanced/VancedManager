package com.vanced.manager.ui.fragments

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.findNavController
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.topjohnwu.superuser.Shell
import com.vanced.manager.R
import com.vanced.manager.adapter.VariantAdapter
import com.vanced.manager.databinding.FragmentHomeBinding
import com.vanced.manager.ui.dialogs.DialogContainer.installAlertBuilder
import com.vanced.manager.ui.viewmodels.HomeViewModel
import com.vanced.manager.ui.viewmodels.HomeViewModelFactory
import com.vanced.manager.utils.AppUtils

open class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels {
        HomeViewModelFactory(requireActivity())
    }
    private val localBroadcastManager by lazy { LocalBroadcastManager.getInstance(requireActivity()) }

    private val tabListener = object : TabLayout.OnTabSelectedListener {

        override fun onTabSelected(tab: TabLayout.Tab) {
            if (tab.position == 1 && !Shell.rootAccess()) {
                Toast.makeText(requireActivity(), getString(R.string.root_not_granted), Toast.LENGTH_SHORT).show()
            }
            val variant = if (tab.position == 1) "root" else "nonroot"
            getDefaultSharedPreferences(requireActivity()).edit().putString("vanced_variant", variant).apply()
        }

        override fun onTabUnselected(tab: TabLayout.Tab) {
            return
        }

        override fun onTabReselected(tab: TabLayout.Tab) {
            return
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().title = getString(R.string.title_home)
        setHasOptionsMenu(true)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.navigateDestination.observe(viewLifecycleOwner, {
            val content = it.getContentIfNotHandled()
            if(content != null){
                view.findNavController().navigate(content)
            }
        })

        with(binding) {
            mainViewpager.adapter = VariantAdapter(viewModel, requireActivity())
            TabLayoutMediator(mainTablayout, mainViewpager) { tab, position ->
                val variants = arrayOf("nonroot", "root")
                tab.text = variants[position]
            }.attach()
            mainTablayout.getTabAt(if (getDefaultSharedPreferences(requireActivity()).getString("vanced_variant", "nonroot") == "root") 1 else 0)?.select()
        }

        AppUtils.installing.observe(viewLifecycleOwner, { value ->
            if (value) hideTab() else showTab()
        })

    }
    
    override fun onPause() {
        super.onPause()
        localBroadcastManager.unregisterReceiver(broadcastReceiver)
        binding.mainTablayout.removeOnTabSelectedListener(tabListener)
    }

    override fun onResume() {
        super.onResume()
        registerReceivers()
        binding.mainTablayout.addOnTabSelectedListener(tabListener)
    }

    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                INSTALL_FAILED -> installAlertBuilder(intent.getStringExtra("errorMsg") as String, requireActivity())
                REFRESH_HOME -> viewModel.fetchData()
            }
        }
    }

    private fun hideTab() {
        val tabHide = AnimationUtils.loadAnimation(requireActivity(), R.anim.tablayout_exit)
        with(binding) {
            if (mainTablayout.visibility != View.GONE) {
                mainTablayout.startAnimation(tabHide)
                mainTablayout.visibility = View.GONE
            }
            mainViewpager.isUserInputEnabled = false
        }
    }

    private fun showTab() {
        val tabShow = AnimationUtils.loadAnimation(requireActivity(), R.anim.tablayout_enter)
        with(binding) {
            if (mainTablayout.visibility != View.VISIBLE) {
                mainTablayout.visibility = View.VISIBLE
                mainTablayout.startAnimation(tabShow)
            }
            mainViewpager.isUserInputEnabled = true
        }
    }

    private fun registerReceivers() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(INSTALL_FAILED)
        intentFilter.addAction(REFRESH_HOME)
        localBroadcastManager.registerReceiver(broadcastReceiver, intentFilter)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    companion object {      
        const val INSTALL_FAILED = "install_failed"
        const val REFRESH_HOME = "refresh_home"
    }
}

