package com.vanced.manager.ui.fragments

import android.animation.LayoutTransition
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.button.MaterialButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.vanced.manager.R
import com.vanced.manager.adapter.SectionPageAdapter
import com.vanced.manager.adapter.SectionPageRootAdapter
import com.vanced.manager.core.fragments.Home
import com.vanced.manager.core.installer.RootAppUninstaller
import com.vanced.manager.databinding.FragmentHomeBinding
import com.vanced.manager.ui.viewmodels.HomeViewModel
import com.vanced.manager.utils.PackageHelper.installApp

class HomeFragment : Home() {

    private lateinit var binding: FragmentHomeBinding
    private var isExpanded: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.title = getString(R.string.title_home)
        setHasOptionsMenu(true)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel: HomeViewModel by viewModels()
        binding.viewModel = viewModel

        val variantPref = getDefaultSharedPreferences(activity).getString("vanced_variant", "nonroot")
        registerReceivers()

        if (variantPref == "root") {
            attachRootChangelog()
            if (viewModel.signatureStatusTxt.value != getString(R.string.signature_disabled)) {
                when (viewModel.signatureStatusTxt.value) {
                    getString(R.string.unavailable) -> disableVancedButton()
                    getString(R.string.signature_enabled) -> disableVancedButton()
                    else -> throw NotImplementedError("Error handling status")
                }
            }
        } else {
            attachNonrootChangelog()
            if (!viewModel.microgInstalled) {
                disableVancedButton()
            }
        }

        view.findViewById<ViewGroup>(R.id.changelog_card).layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        view.findViewById<ImageButton>(R.id.changelog_button).setOnClickListener {
                cardExpandCollapse()
        }
    }

    private fun cardExpandCollapse() {
        val viewPagerContainer = view?.findViewById<ViewPager2>(R.id.viewpager)
        val tabLayoutContainer = view?.findViewById<TabLayout>(R.id.tablayout)
        val arrow = view?.findViewById<ImageButton>(R.id.changelog_button)
        if (isExpanded) {
            viewPagerContainer?.visibility = View.GONE
            tabLayoutContainer?.visibility = View.GONE
            isExpanded = false
            arrow?.startAnimation(RotateAnimation(180f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f))
        } else {
            viewPagerContainer?.visibility = View.VISIBLE
            tabLayoutContainer?.visibility = View.VISIBLE
            isExpanded = true
            arrow?.startAnimation(RotateAnimation(0f, 180f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f))
        }
    }

    override fun onPause() {
        super.onPause()
        activity?.let { LocalBroadcastManager.getInstance(it).unregisterReceiver(broadcastReceiver) }
    }

    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val viewModel: HomeViewModel by viewModels()
            when (intent.action) {
                SIGNATURE_DISABLED -> {
                    activity?.runOnUiThread {
                        viewModel.signatureStatusTxt.value = getString(R.string.signature_disabled)
                    }
                    val mIntent = Intent(activity, RootAppUninstaller::class.java)
                    mIntent.putExtra("Data", "com.vanced.stub")
                    activity?.startService(mIntent)
                    activity?.recreate()
                }
                SIGNATURE_ENABLED -> {
                    activity?.runOnUiThread {
                        viewModel.signatureStatusTxt.value = getString(R.string.signature_enabled)
                    }
                    activity?.recreate()
                }
                MICROG_DOWNLOADING -> {
                    val progress = intent.getIntExtra("microgProgress", 0)
                    val progressbar = view?.findViewById<ProgressBar>(R.id.microg_progress)
                    val downloadTxt = intent.getStringExtra("fileName")
                    val txt = view?.findViewById<TextView>(R.id.microg_downloading)
                    txt?.visibility = View.VISIBLE
                    txt?.text = downloadTxt
                    progressbar?.visibility = View.VISIBLE
                    progressbar?.progress = progress
                }
                VANCED_DOWNLOADING -> {
                    val progress = intent.getIntExtra("vancedProgress", 0)
                    val progressbar = view?.findViewById<ProgressBar>(R.id.vanced_progress)
                    val downloadTxt = intent.getStringExtra("fileName")
                    val txt = view?.findViewById<TextView>(R.id.vanced_downloading)
                    txt?.visibility = View.VISIBLE
                    txt?.text = downloadTxt
                    progressbar?.visibility = View.VISIBLE
                    progressbar?.progress = progress
                }
                MICROG_DOWNLOADED -> {
                    view?.findViewById<TextView>(R.id.microg_downloading)?.visibility = View.GONE
                    view?.findViewById<ProgressBar>(R.id.microg_progress)?.visibility = View.GONE
                    view?.findViewById<ProgressBar>(R.id.microg_installing)?.visibility =
                        View.VISIBLE
                    activity?.let { installApp(it, it.filesDir.path + "/microg.apk", "com.mgoogle.android.gms") }
                }
                VANCED_DOWNLOADED -> {
                    view?.findViewById<TextView>(R.id.vanced_downloading)?.visibility = View.GONE
                    view?.findViewById<ProgressBar>(R.id.vanced_progress)?.visibility = View.GONE
                    view?.findViewById<ProgressBar>(R.id.vanced_installing)?.visibility = View.VISIBLE
                }
                DOWNLOAD_ERROR -> {
                    val error = intent.getStringExtra("DownloadError") as String
                    Toast.makeText(activity, error, Toast.LENGTH_SHORT).show()
                    Log.d("VMDwnld", error)
                }
            }
        }
    }

    private fun registerReceivers() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(SIGNATURE_DISABLED)
        intentFilter.addAction(SIGNATURE_ENABLED)
        intentFilter.addAction(VANCED_DOWNLOADING)
        intentFilter.addAction(MICROG_DOWNLOADING)
        intentFilter.addAction(VANCED_DOWNLOADED)
        intentFilter.addAction(MICROG_DOWNLOADED)
        activity?.let {
            LocalBroadcastManager.getInstance(it).registerReceiver(broadcastReceiver, intentFilter)
        }

    }

    private fun attachNonrootChangelog() {
        val sectionPageAdapter = SectionPageAdapter(this)
        val tabLayout = view?.findViewById(R.id.tablayout) as TabLayout
        val viewPager = view?.findViewById(R.id.viewpager) as ViewPager2
        viewPager.adapter = sectionPageAdapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Vanced"
                1 -> tab.text = "MicroG"
                2 -> tab.text = "Manager"
            }
        }.attach()
    }

    private fun attachRootChangelog() {
        val sectionPageRootAdapter = SectionPageRootAdapter(this)
        val tabLayout = view?.findViewById(R.id.tablayout) as TabLayout
        val viewPager = view?.findViewById(R.id.viewpager) as ViewPager2
        viewPager.adapter = sectionPageRootAdapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Vanced"
                1 -> tab.text = "Manager"
            }
        }.attach()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
        super .onCreateOptionsMenu(menu, inflater)
    }

    private fun disableVancedButton() {
        val vancedinstallbtn = view?.findViewById<MaterialButton>(R.id.vanced_installbtn)
        vancedinstallbtn?.isEnabled = false
        vancedinstallbtn?.backgroundTintList = ColorStateList.valueOf(Color.DKGRAY)
        vancedinstallbtn?.setTextColor(ColorStateList.valueOf(Color.GRAY))
        vancedinstallbtn?.icon = null
    }

    companion object {
        const val SIGNATURE_DISABLED = "Signature verification disabled"
        const val SIGNATURE_ENABLED = "Signature verification enabled"
        const val VANCED_DOWNLOADING = "Vanced downloading"
        const val MICROG_DOWNLOADING = "MicroG downloading"
        const val VANCED_DOWNLOADED = "Vanced downloaded"
        const val MICROG_DOWNLOADED = "MicroG downloaded"
        const val DOWNLOAD_ERROR = "Error occurred"

    }

}

