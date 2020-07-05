package com.vanced.manager.ui.fragments

import android.content.*
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import com.google.android.material.tabs.TabLayoutMediator
import com.vanced.manager.R
import com.vanced.manager.adapter.*
import com.vanced.manager.core.fragments.Home
import com.vanced.manager.databinding.FragmentHomeBinding
import com.vanced.manager.ui.viewmodels.HomeViewModel

class HomeFragment : Home() {

    private lateinit var binding: FragmentHomeBinding
    private var isExpanded: Boolean = false
    private val viewModel: HomeViewModel by viewModels()
    private val localBroadcastManager by lazy { LocalBroadcastManager.getInstance(requireActivity()) }

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
        binding.viewModel = viewModel

        val variantPref = getDefaultSharedPreferences(activity).getString("vanced_variant", "nonroot")
        registerReceivers()

        if (variantPref == "root")
            attachRootChangelog()
        else {
            attachNonrootChangelog()
            if (!viewModel.microgInstalled.value!!) {
                disableVancedButton()
            }
        }

        binding.includeChangelogsLayout.changelogButton.setOnClickListener {
            cardExpandCollapse()
        }

        binding.includeVancedLayout.vancedCard.setOnLongClickListener {
            val clip = requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clip.setPrimaryClip(ClipData.newPlainText("vanced", this.viewModel.vancedInstalledVersion.value))
            versionToast("Vanced")
            true
        }

        binding.includeMicrogLayout.microgCard.setOnLongClickListener {
            val clip = requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clip.setPrimaryClip(ClipData.newPlainText("microg", viewModel.microgInstalledVersion.value))
            versionToast("MicroG")
            true
        }
    }

    private fun versionToast(name: String) {
        Toast.makeText(activity, getString(R.string.version_toast, name), Toast.LENGTH_LONG).show()
    }

    private fun cardExpandCollapse() {
        with(binding.includeChangelogsLayout) {
            viewpager.visibility = if (isExpanded) View.GONE else View.VISIBLE
            tablayout.visibility = if (isExpanded) View.GONE else View.VISIBLE
            changelogButton.animate().apply {
                rotation(if (isExpanded) 0F else 180F)
                interpolator = AccelerateDecelerateInterpolator()
            }
            isExpanded = !isExpanded
        }
    }

    override fun onPause() {
        localBroadcastManager.unregisterReceiver(broadcastReceiver)
        super.onPause()
    }

    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                MICROG_DOWNLOADED -> {
                    binding.includeMicrogLayout.microgInstalling.visibility = View.VISIBLE
                    //activity?.let { installApp(it, it.filesDir.path + "/microg.apk", "com.mgoogle.android.gms") }
                }
                VANCED_DOWNLOADED -> {
                    binding.includeVancedLayout.vancedInstalling.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun registerReceivers() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(VANCED_DOWNLOADED)
        intentFilter.addAction(MICROG_DOWNLOADED)
        localBroadcastManager.registerReceiver(broadcastReceiver, intentFilter)
    }

    private fun attachNonrootChangelog() {
        val sectionPageRootAdapter = SectionPageAdapter(this)
        with(binding.includeChangelogsLayout) {
            viewpager.adapter = sectionPageRootAdapter
            TabLayoutMediator(tablayout, viewpager) { tab, position ->
                when (position) {
                    0 -> tab.text = "Vanced"
                    1 -> tab.text = "MicroG"
                    2 -> tab.text = "Manager"
                }
            }.attach()
        }
    }

    private fun attachRootChangelog() {
        val sectionPageRootAdapter = SectionPageRootAdapter(this)
        with(binding.includeChangelogsLayout) {
            viewpager.adapter = sectionPageRootAdapter
            TabLayoutMediator(tablayout, viewpager) { tab, position ->
                when (position) {
                    0 -> tab.text = "Vanced"
                    1 -> tab.text = "Manager"
                }
            }.attach()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun disableVancedButton() {
        binding.includeVancedLayout.vancedInstallbtn.apply {
            icon = null
            isEnabled = false
            backgroundTintList = ColorStateList.valueOf(Color.DKGRAY)
            setTextColor(ColorStateList.valueOf(Color.GRAY))
        }
    }

    companion object {
        const val VANCED_DOWNLOADED = "vanced_downloaded"
        const val MICROG_DOWNLOADED = "microg_downloaded"
    }
}

