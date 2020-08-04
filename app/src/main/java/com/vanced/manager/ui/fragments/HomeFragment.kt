package com.vanced.manager.ui.fragments

import android.content.*
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.findNavController
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.topjohnwu.superuser.Shell
import com.vanced.manager.R
import com.vanced.manager.adapter.*
import com.vanced.manager.core.downloader.MicrogDownloadService
import com.vanced.manager.core.downloader.VancedDownloadService
import com.vanced.manager.databinding.FragmentHomeBinding
import com.vanced.manager.ui.MainActivity
import com.vanced.manager.ui.viewmodels.HomeViewModel
import com.vanced.manager.utils.PackageHelper

class HomeFragment : Fragment(), View.OnClickListener {

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

        with(binding) {
            rootSwitch.setOnClickListener(this@HomeFragment)
            nonrootSwitch.setOnClickListener(this@HomeFragment)

            includeVancedLayout.vancedInstallbtn.setOnClickListener(this@HomeFragment)
            includeVancedLayout.vancedUninstallbtn.setOnClickListener(this@HomeFragment)
            includeMicrogLayout.microgInstallbtn.setOnClickListener(this@HomeFragment)
            includeMicrogLayout.microgUninstallbtn.setOnClickListener(this@HomeFragment)
        }

        binding.includeChangelogsLayout.changelogButton.setOnClickListener {
            cardExpandCollapse()
        }

        binding.includeVancedLayout.vancedCard.setOnLongClickListener {
            versionToast("Vanced", viewModel.vancedInstalledVersion.get())
            true
        }

        binding.includeMicrogLayout.microgCard.setOnLongClickListener {
            versionToast("MicroG", viewModel.microgInstalledVersion.get())
            true
        }

        with(binding.includeChangelogsLayout) {
            viewpager.adapter = if (variantPref == "root") SectionPageRootAdapter(this@HomeFragment) else SectionPageAdapter(this@HomeFragment)
            TabLayoutMediator(tablayout, viewpager) { tab, position ->
                if (variantPref == "root")
                    when (position) {
                        0 -> tab.text = "Vanced"
                        1 -> tab.text = "Manager"
                    }
                else
                    when (position) {
                        0 -> tab.text = "Vanced"
                        1 -> tab.text = "MicroG"
                        2 -> tab.text = "Manager"
                    }

            }.attach()
        }
        viewModel.fetchData()
    }

    override fun onClick(v: View?) {
        val prefs = activity?.getSharedPreferences("installPrefs", Context.MODE_PRIVATE)
        val variant = getDefaultSharedPreferences(activity).getString("vanced_variant", "nonroot")
        val vancedPkgName =
            if (variant == "root") {
                "com.google.android.youtube"
            } else {
                "com.vanced.android.youtube"
            }

        when (v?.id) {
            R.id.vanced_installbtn -> {
                if (viewModel.microgInstalled.get()!!) {
                    if (prefs?.getBoolean("valuesModified", false)!!) {
                        activity?.startService(
                            Intent(
                                activity,
                                VancedDownloadService::class.java
                            )
                        )
                    } else {
                        view?.findNavController()?.navigate(R.id.toInstallThemeFragment)
                    }
                } else
                    Snackbar.make(binding.homeRefresh, R.string.no_microg, Snackbar.LENGTH_LONG)
                        .setAction(R.string.install) {
                            activity?.startService(Intent(activity, MicrogDownloadService::class.java))
                        }.show()

            }
            R.id.microg_installbtn -> {
                activity?.startService(Intent(activity, MicrogDownloadService::class.java))
            }
            R.id.microg_uninstallbtn -> activity?.let {
                PackageHelper.uninstallApk(
                    "com.mgoogle.android.gms",
                    it
                )
            }
            R.id.vanced_uninstallbtn -> activity?.let {
                PackageHelper.uninstallApk(
                    vancedPkgName,
                    it
                )
            }
            R.id.nonroot_switch -> writeToVariantPref("nonroot", R.anim.slide_in_left, R.anim.slide_out_right)
            R.id.root_switch ->
                if (Shell.rootAccess()) {
                    writeToVariantPref("root", R.anim.slide_in_right, R.anim.slide_out_left)
                } else {
                    writeToVariantPref("nonroot", R.anim.slide_in_left, R.anim.slide_out_right)
                    Toast.makeText(activity, activity?.getString(R.string.root_not_granted), Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun writeToVariantPref(variant: String, animIn: Int, animOut: Int) {
        val prefs = getDefaultSharedPreferences(activity)
        if (prefs.getString("vanced_variant", "nonroot") != variant) {
            prefs.edit().putString("vanced_variant", variant).apply()
            startActivity(Intent(activity, MainActivity::class.java))
            activity?.overridePendingTransition(animIn, animOut)
            activity?.finish()
        } else
            Log.d("VMVariant", "$variant is already selected")
    }

    private fun versionToast(name: String, app: String?) {
        val clip = requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clip.setPrimaryClip(ClipData.newPlainText(name, app))
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

    override fun onResume() {
        super.onResume()
        registerReceivers()
    }

    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                MICROG_DOWNLOADED -> binding.includeMicrogLayout.microgInstalling.visibility = View.VISIBLE
                VANCED_DOWNLOADED -> binding.includeVancedLayout.vancedInstalling.visibility = View.VISIBLE
                REFRESH_HOME -> {
                    Log.d("VMRefresh", "Refreshing home page")
                    binding.includeMicrogLayout.microgInstalling.visibility = View.VISIBLE
                    //viewModel.fetchData()
                }
            }
        }
    }

    private fun registerReceivers() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(VANCED_DOWNLOADED)
        intentFilter.addAction(MICROG_DOWNLOADED)
        intentFilter.addAction(REFRESH_HOME)
        localBroadcastManager.registerReceiver(broadcastReceiver, intentFilter)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    /*
    private fun disableVancedButton() {
        binding.includeVancedLayout.vancedInstallbtn.apply {
            icon = null
            isEnabled = false
            backgroundTintList = ColorStateList.valueOf(Color.DKGRAY)
            setTextColor(ColorStateList.valueOf(Color.GRAY))
        }
    }
     */

    companion object {
        const val VANCED_DOWNLOADED = "vanced_downloaded"
        const val MICROG_DOWNLOADED = "microg_downloaded"
        const val REFRESH_HOME = "refresh_home"
    }
}

