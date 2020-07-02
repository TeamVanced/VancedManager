package com.vanced.manager.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.appbar.MaterialToolbar
import com.vanced.manager.R
import com.vanced.manager.core.Main
import com.vanced.manager.databinding.ActivityMainBinding
import com.vanced.manager.ui.dialogs.DialogContainer.installAlertBuilder
import com.vanced.manager.ui.dialogs.DialogContainer.launchVanced
import com.vanced.manager.ui.dialogs.DialogContainer.regularPackageInstalled
import com.vanced.manager.utils.ThemeHelper.setFinalTheme

class MainActivity : Main() {

    private var isParent = true
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        setFinalTheme(this)
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this

        val toolbar: MaterialToolbar = findViewById(R.id.home_toolbar)
        setSupportActionBar(toolbar)

        val navHost = findNavController(R.id.bottom_nav_host)
        val appBarConfiguration = AppBarConfiguration(navHost.graph)
        toolbar.setupWithNavController(navHost, appBarConfiguration)

        navHost.addOnDestinationChangedListener{_, currFrag: NavDestination, _ ->
            isParent = when (currFrag.id) {
                R.id.home_fragment -> true
                else -> false
            }

            setDisplayHomeAsUpEnabled(!isParent)

        }

        registerReceivers()

    }

    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                INSTALL_COMPLETED -> {
                    if (intent.getStringExtra("package") == "split")
                        launchVanced(this@MainActivity)
                    else
                        regularPackageInstalled(getString(R.string.successfully_installed, "MicroG"), this@MainActivity)
                }
                INSTALL_FAILED -> installAlertBuilder(intent.getStringExtra("errorMsg") as String, this@MainActivity)
                APP_UNINSTALLED -> {
                    restartActivity()
                    Log.d("VMpm", "test")
                }
                APP_NOT_UNINSTALLED -> installAlertBuilder(getString(R.string.failed_uninstall, intent.getStringExtra("pkgName")), this@MainActivity)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver)
    }

    override fun onResume() {
        setFinalTheme(this)
        super.onResume()
        registerReceivers()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navHost = findNavController(R.id.bottom_nav_host)
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
        val toolbar: MaterialToolbar = findViewById(R.id.home_toolbar)
        when {
            isNeeded -> toolbar.setNavigationIcon(R.drawable.ic_keyboard_backspace_black_24dp)
            else -> toolbar.navigationIcon = null
        }
    }

    private fun registerReceivers() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(INSTALL_COMPLETED)
        intentFilter.addAction(INSTALL_FAILED)
        intentFilter.addAction(APP_UNINSTALLED)
        intentFilter.addAction(APP_NOT_UNINSTALLED)
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, intentFilter)

    }

    fun restartActivity() {
        startActivity(Intent(this@MainActivity, MainActivity::class.java))
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }

    companion object {
        const val INSTALL_COMPLETED = "Installation completed"
        const val INSTALL_FAILED = "it just failed idk"
        const val APP_UNINSTALLED = "App uninstalled"
        const val APP_NOT_UNINSTALLED = "App not uninstalled"
    }
}
