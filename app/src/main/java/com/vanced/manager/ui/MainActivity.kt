package com.vanced.manager.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
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
    
    private val loadingObserver = object : LoadingStateListener {
        val tag = "VMLocalisation"
        override fun onDataChanged() {
            Log.d(tag, "Loaded data")
        }

        override fun onFailure(throwable: Throwable) {
            Log.d(tag, "Failed to load data")
        }

    }

    private val tabListener = object : TabLayout.OnTabSelectedListener {

        override fun onTabSelected(tab: TabLayout.Tab) {
            getDefaultSharedPreferences(this@MainActivity).edit().apply {
                when (tab.position) {
                    0 -> putString("vanced_variant", "nonroot").apply()
                    1 -> putString("vanced_variant", "music").apply()
                    2 -> putString("vanced_variant", "root").apply()
                }
            }

        }

        override fun onTabUnselected(tab: TabLayout.Tab) {
            TODO("There's nothing to do here actually")
        }

        override fun onTabReselected(tab: TabLayout.Tab) {
            getDefaultSharedPreferences(this@MainActivity).edit().apply {
                when (tab.position) {
                    0 -> putString("vanced_variant", "nonroot").apply()
                    1 -> putString("vanced_variant", "music").apply()
                    2 -> putString("vanced_variant", "root").apply()
                }
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setFinalTheme(this)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        with(binding) {
            lifecycleOwner = this@MainActivity
            setSupportActionBar(homeToolbar)
            mainViewpager.adapter = SectionVariantAdapter(this@MainActivity)
            TabLayoutMediator(mainTablayout, mainViewpager) { tab, position ->
                when (position) {
                    0 -> tab.text = "nonroot"
                    1 -> tab.text = "Music"
                    2 -> tab.text = "root"
                }
            }.attach()
            
            when (getDefaultSharedPreferences(this@MainActivity).getString("vanced_variant", "nonroot")) {
                "nonroot" -> mainTablayout.getTabAt(0)?.select()
                "music" -> mainTablayout.getTabAt(1)?.select()
                "root" -> mainTablayout.getTabAt(2)?.select()
            }
            
            mainTablayout.addOnTabSelectedListener(tabListener)
            
        }

        initDialogs()

    }

    override fun onPause() {
        super.onPause()
        binding.mainTablayout.removeOnTabSelectedListener(tabListener)
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
                return false
            }
            R.id.toolbar_settings -> {
                return false
            }
            R.id.dev_settings -> {
                return false
            }
            else -> super.onOptionsItemSelected(item)
        }

        return false
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(Crowdin.wrapContext(newBase))
    }

    private fun initDialogs() {
        val prefs = getDefaultSharedPreferences(this)
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
