package com.vanced.manager.ui.fragments

import android.content.*
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.vanced.manager.R
import com.vanced.manager.adapter.SectionPageAdapter
import com.vanced.manager.adapter.SectionPageRootAdapter
import com.vanced.manager.core.fragments.Home
import com.vanced.manager.databinding.FragmentHomeBinding
import com.vanced.manager.ui.viewmodels.HomeViewModel

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

        if (variantPref == "root")
            attachRootChangelog()
        else {
            attachNonrootChangelog()
            if (!viewModel.microgInstalled) {
                disableVancedButton()
            }
        }

        view.findViewById<ImageButton>(R.id.changelog_button).setOnClickListener {
            cardExpandCollapse()
        }

        view.findViewById<MaterialCardView>(R.id.vanced_card).setOnLongClickListener{
            val clip = activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clip.setPrimaryClip(ClipData.newPlainText("vanced", viewModel.vancedInstalledVersion.value))
            versionToast("Vanced")
            true
        }

        view.findViewById<MaterialCardView>(R.id.microg_card).setOnLongClickListener{
            val clip = activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clip.setPrimaryClip(ClipData.newPlainText("microg", viewModel.microgInstalledVersion.value))
            versionToast("MicroG")
            true
        }
    }

    private fun versionToast(name: String)
    {
        Toast.makeText(activity, getString(R.string.version_toast, name), Toast.LENGTH_LONG).show()
    }

    private fun cardExpandCollapse() {
        val viewPagerContainer = view?.findViewById<ViewPager2>(R.id.viewpager)
        val tabLayoutContainer = view?.findViewById<TabLayout>(R.id.tablayout)
        val arrow = view?.findViewById<ImageButton>(R.id.changelog_button)
        if (isExpanded) {
            viewPagerContainer?.visibility = View.GONE
            tabLayoutContainer?.visibility = View.GONE
            isExpanded = false
            arrow?.animate()?.rotation(0F)?.interpolator = AccelerateDecelerateInterpolator()
        } else {
            viewPagerContainer?.visibility = View.VISIBLE
            tabLayoutContainer?.visibility = View.VISIBLE
            isExpanded = true
            arrow?.animate()?.rotation(180F)?.interpolator = AccelerateDecelerateInterpolator()
        }
    }

    override fun onPause() {
        super.onPause()
        activity?.let { LocalBroadcastManager.getInstance(it).unregisterReceiver(broadcastReceiver) }
    }

    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                MICROG_DOWNLOADED -> {
                    view?.findViewById<ProgressBar>(R.id.microg_installing)?.visibility = View.VISIBLE
                    //activity?.let { installApp(it, it.filesDir.path + "/microg.apk", "com.mgoogle.android.gms") }
                }
                VANCED_DOWNLOADED -> {
                    view?.findViewById<ProgressBar>(R.id.vanced_installing)?.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun registerReceivers() {
        val intentFilter = IntentFilter()
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
        const val VANCED_DOWNLOADED = "Vanced downloaded"
        const val MICROG_DOWNLOADED = "MicroG downloaded"

    }

}

