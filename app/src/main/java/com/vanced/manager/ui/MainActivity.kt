package com.vanced.manager.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import com.crowdin.platform.Crowdin
import com.crowdin.platform.LoadingStateListener
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.messaging.FirebaseMessaging
import com.topjohnwu.superuser.Shell
import com.vanced.manager.R
import com.vanced.manager.adapter.VariantAdapter
import com.vanced.manager.databinding.ActivityMainBinding
import com.vanced.manager.ui.dialogs.DialogContainer
import com.vanced.manager.ui.fragments.UpdateCheckFragment
import com.vanced.manager.utils.AppUtils.installing
import com.vanced.manager.utils.InternetTools
import com.vanced.manager.utils.PackageHelper
import com.vanced.manager.utils.ThemeHelper.setFinalTheme
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
            if (tab.position == 1 && !Shell.rootAccess()) {
                Toast.makeText(this@MainActivity, getString(R.string.root_not_granted), Toast.LENGTH_SHORT).show()
                return
            }

            getDefaultSharedPreferences(this@MainActivity).edit().putString("vanced_variant", 
                when (tab.position) {
                    1 -> "root"
                    else -> "nonroot"
                }
            ).apply()

        }

        override fun onTabUnselected(tab: TabLayout.Tab) {
            return
        }

        override fun onTabReselected(tab: TabLayout.Tab) {
            return
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setFinalTheme(this)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        with(binding) {
            lifecycleOwner = this@MainActivity
            setSupportActionBar(homeToolbar)
            mainViewpager.adapter = VariantAdapter(this@MainActivity)
            mainViewpager.isUserInputEnabled = false
            TabLayoutMediator(mainTablayout, mainViewpager) { tab, position ->
                tab.text = if (position == 1) "root" else "nonroot"
            }.attach()
            
            mainTablayout.getTabAt(
                if (getDefaultSharedPreferences(this@MainActivity).getString("vanced_variant", "nonroot") == "root")
                    1
                else
                    0
            )?.select()
            
        }

        initDialogs()

    }

    override fun onPause() {
        super.onPause()
        Crowdin.unregisterDataLoadingObserver(loadingObserver)
        binding.mainTablayout.removeOnTabSelectedListener(tabListener)
    }

    override fun onResume() {
        setFinalTheme(this)
        super.onResume()
        Crowdin.registerDataLoadingObserver(loadingObserver)
        binding.mainTablayout.addOnTabSelectedListener(tabListener)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (installing) {
            return false
        }
        when (item.itemId) {
            android.R.id.home -> {
                return false
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
        prefs.getBoolean("show_root_dialog", true)

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
