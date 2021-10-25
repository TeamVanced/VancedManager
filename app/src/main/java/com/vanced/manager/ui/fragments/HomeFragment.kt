package com.vanced.manager.ui.fragments

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.vanced.manager.BuildConfig.VERSION_CODE
import com.vanced.manager.R
import com.vanced.manager.adapter.ExpandableAppListAdapter
import com.vanced.manager.adapter.LinkAdapter
import com.vanced.manager.adapter.SponsorAdapter
import com.vanced.manager.core.ui.base.BindingFragment
import com.vanced.manager.core.ui.ext.showDialog
import com.vanced.manager.databinding.FragmentHomeBinding
import com.vanced.manager.ui.dialogs.AppInfoDialog
import com.vanced.manager.ui.dialogs.DialogContainer.installAlertBuilder
import com.vanced.manager.ui.viewmodels.HomeViewModel
import com.vanced.manager.utils.isFetching
import com.vanced.manager.utils.manager

class HomeFragment : BindingFragment<FragmentHomeBinding>() {

    companion object {
        const val INSTALL_FAILED = "INSTALL_FAILED"
        const val REFRESH_HOME = "REFRESH_HOME"
    }

    private val viewModel: HomeViewModel by viewModels()

    private val localBroadcastManager by lazy { LocalBroadcastManager.getInstance(requireActivity()) }

    override fun binding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentHomeBinding.inflate(inflater, container, false)

    override fun otherSetups() {
        bindData()
    }

    private fun bindData() {
        requireActivity().title = getString(R.string.title_home)
        setHasOptionsMenu(true)
        with(binding) {
            homeRefresh.setOnRefreshListener { viewModel.fetchData() }
            isFetching.observe(viewLifecycleOwner) { homeRefresh.isRefreshing = it }

            val prefs = PreferenceManager.getDefaultSharedPreferences(context)
            if (prefs.contains("LastVersionCode")) {
                if (prefs.getInt("LastVersionCode", -1) < VERSION_CODE) {
                    showDialog(
                        AppInfoDialog.newInstance(
                            appName = getString(R.string.app_name),
                            appIcon = R.mipmap.ic_launcher,
                            changelog = manager.value?.string("changelog")
                        )
                    )
                    prefs.edit().putInt("LastVersionCode", VERSION_CODE).apply()
                }
            } else prefs.edit().putInt("LastVersionCode", VERSION_CODE).apply()

            recyclerAppList.apply {
                layoutManager = LinearLayoutManager(requireActivity())
                adapter = ExpandableAppListAdapter(requireActivity(), viewModel /*, tooltip*/)
                setHasFixedSize(true)
            }

            recyclerSponsors.apply {
                val lm = FlexboxLayoutManager(requireActivity())
                lm.justifyContent = JustifyContent.SPACE_EVENLY
                layoutManager = lm
                setHasFixedSize(true)
                adapter = SponsorAdapter(requireActivity(), viewModel)
            }

            recyclerLinks.apply {
                val lm = FlexboxLayoutManager(requireActivity())
                lm.justifyContent = JustifyContent.SPACE_EVENLY
                layoutManager = lm
                setHasFixedSize(true)
                adapter = LinkAdapter(requireActivity(), viewModel)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
    }

    override fun onPause() {
        super.onPause()
        localBroadcastManager.unregisterReceiver(broadcastReceiver)
    }

    override fun onResume() {
        super.onResume()
        registerReceivers()
    }

    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                INSTALL_FAILED -> installAlertBuilder(
                    intent.getStringExtra("errorMsg").toString(),
                    intent.getStringExtra("fullErrorMsg"),
                    requireActivity()
                )
                REFRESH_HOME -> viewModel.fetchData()
            }
        }
    }

    private fun registerReceivers() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(INSTALL_FAILED)
        intentFilter.addAction(REFRESH_HOME)
        localBroadcastManager.registerReceiver(broadcastReceiver, intentFilter)
    }
}

