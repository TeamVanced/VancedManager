package com.vanced.manager.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.crowdin.platform.Crowdin
import com.crowdin.platform.LoadingStateListener
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.tabs.TabLayout
import com.google.firebase.messaging.FirebaseMessaging
import com.vanced.manager.R
import com.vanced.manager.databinding.ActivityMainBinding
import com.vanced.manager.ui.dialogs.DialogContainer
import com.vanced.manager.ui.fragments.UpdateCheckFragment
import com.vanced.manager.utils.AppUtils.installing
import com.vanced.manager.utils.InternetTools
import com.vanced.manager.utils.PackageHelper
import com.vanced.manager.utils.ThemeHelper.setFinalTheme
import com.vanced.manager.adapter.SectionVariantAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val navHost by lazy { findNavController(R.id.bottom_nav_host) }

    private val loadingObserver = object : LoadingStateListener {
        val tag = "VMLocalisation"
        override fun onDataChanged() {
            Log.d(tag, "Loaded data")
        }

        override fun onFailure(throwable: Throwable) {
            Log.d(tag, "Failed to load data")
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setFinalTheme(this)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        with(binding) {
            lifecycleOwner = this@MainActivity
            setSupportActionBar(homeToolbar)
            val appBarConfiguration = AppBarConfiguration(navHost.graph)
            homeToolbar.setupWithNavController(navHost, appBarConfiguration)
            mainViewpager.adapter = SectionVariantAdapter()
            TabLayoutMediator(mainTablayout, mainViewpager) { tab, position ->
                when (position) {
                    0 -> tab.text = "nonroot"
                    1 -> tab.text = "Music"
                    2 -> tab.text = "root"
                }
            }.attach()
            
            when (variant) {
                "nonroot" -> mainTablayout.getTabAt(0).select()
                "music" -> mainTablayout.getTabAt(1).select()
                "nonroot" -> mainTablayout.getTabAt(2).select()
            }
            
            mainTablayout.setOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {            
                
                override fun onTabSelected(tab: TabLayout.Tab) {
                    getDefaultSharedPreferences(requireActivity()).edit().apply {
                        when (tab.position) {
                            0 -> putString("vanced_variant", "nonroot")
                            0 -> putString("vanced_variant", "music")
                            0 -> putString("vanced_variant", "root")
                        }
                    }
                    
                }
                
                override fun onTabUnselected(tab: TabLayout.Tab) {}
                
                override fun onTabReselected(tab: TabLayout.Tab) {
                    getDefaultSharedPreferences(requireActivity()).edit().apply {
                        when (tab.position) {
                            0 -> putString("vanced_variant", "nonroot")
                            0 -> putString("vanced_variant", "music")
                            0 -> putString("vanced_variant", "root")
                        }
                    }
                }
                
            }
            
        }

        navHost.addOnDestinationChangedListener { _, currFrag: NavDestination, _ ->
            setDisplayHomeAsUpEnabled(currFrag.id != R.id.home_fragment)
            val tabHide = AnimationUtils.loadAnimation(this, R.anim.tablayout_exit)
            val tabShow = AnimationUtils.loadAnimation(this, R.anim.tablayout_enter)
            if (currFrag.id != R.id.home_fragment) {
                binding.variantTabContainer.startAnimation(tabHide)
                binding.variantTabContainer.visibility = View.GONE
            } else {
                binding.variantTabContainer.visibility = View.VISIBLE
                binding.variantTabContainer.startAnimation(tabShow)
            }
        }

        initDialogs()

    }

    override fun onPause() {
        super.onPause()
        Crowdin.unregisterDataLoadingObserver(loadingObserver)
    }

    override fun onResume() {
        setFinalTheme(this)
        super.onResume()
        Crowdin.registerDataLoadingObserver(loadingObserver)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (installing) {
            return false
        }
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            R.id.toolbar_about -> {
                navHost.navigate(R.id.toAboutFragment)
                return true
            }
            R.id.toolbar_settings -> {
                navHost.navigate(R.id.action_settingsFragment)
                return true
            }
            R.id.dev_settings -> {
                navHost.navigate(R.id.toDevSettingsFragment)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }

        return false
    }

    private fun setDisplayHomeAsUpEnabled(isNeeded: Boolean) {
        binding.homeToolbar.navigationIcon = if (isNeeded) getDrawable(R.drawable.ic_keyboard_backspace_black_24dp) else null
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(Crowdin.wrapContext(newBase))
    }

    private fun initDialogs() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val variant = prefs.getString("vanced_variant", "nonroot")
        val showRootDialog = prefs.getBoolean("show_root_dialog", true)

        when {
            prefs.getBoolean("firstStart", true) -> {
                DialogContainer.showSecurityDialog(this)
                with(FirebaseMessaging.getInstance()) {
                    subscribeToTopic("Vanced-Update")
                    subscribeToTopic("MicroG-Update")
                }
            }
            !prefs.getBoolean("statement", true) -> DialogContainer.statementFalse(this)
            variant == "root" -> {

                if (PackageHelper.getPackageVersionName(
                        "com.google.android.youtube",
                        packageManager
                    ) == "14.21.54")
                    DialogContainer.basicDialog(
                        getString(R.string.hold_on),
                        getString(R.string.magisk_vanced),
                        this
                    )
            }
        }

        checkUpdates()
    }

    private fun checkUpdates() {
        CoroutineScope(Dispatchers.Main).launch {
            if (InternetTools.isUpdateAvailable()) {
                UpdateCheckFragment().show(supportFragmentManager, "UpdateCheck")
            }
        }
    }

}
