package com.vanced.manager.ui

import android.content.*
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.vanced.manager.R
import com.vanced.manager.core.Main
import com.vanced.manager.databinding.ActivityMainBinding
import com.vanced.manager.ui.dialogs.DialogContainer.installAlertBuilder
import com.vanced.manager.ui.dialogs.DialogContainer.launchVanced
import com.vanced.manager.utils.ThemeHelper.setFinalTheme

class MainActivity : Main() {

    private var isParent = true
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        setFinalTheme(this)
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this

        val toolbar: Toolbar = findViewById(R.id.home_toolbar)
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
                INSTALL_COMPLETED -> launchVanced(this@MainActivity)
                INSTALL_BLOCKED -> installAlertBuilder(getString(R.string.installation_blocked), this@MainActivity)
                INSTALL_FAILED -> installAlertBuilder(getString(R.string.installation_failed), this@MainActivity)
                INSTALL_ABORTED -> installAlertBuilder(getString(R.string.installation_aborted), this@MainActivity)
                INSTALL_STORAGE -> installAlertBuilder(getString(R.string.installation_storage), this@MainActivity)
                INSTALL_CONFLICT -> installAlertBuilder(getString(R.string.installation_conflict), this@MainActivity)
                INSTALL_INVALID -> installAlertBuilder(getString(R.string.installation_invalid), this@MainActivity)

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
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val devSettings = prefs.getBoolean("devSettings", false)
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
            R.id.secret_settings -> {
                navHost.navigate(R.id.toSecretSettingsFragment)
                return true
            }
            R.id.dev_settings -> {
                return if (devSettings) {
                    navHost.navigate(R.id.toDevSettingsFragment)
                    true
                } else
                    false

            }
            else -> super.onOptionsItemSelected(item)
        }
        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun setDisplayHomeAsUpEnabled(isNeeded: Boolean) {
        val toolbar: Toolbar = findViewById(R.id.home_toolbar)
        when {
            isNeeded -> toolbar.setNavigationIcon(R.drawable.ic_keyboard_backspace_black_24dp)
            else -> toolbar.navigationIcon = null
        }
    }

    private fun registerReceivers() {
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, IntentFilter(
            INSTALL_COMPLETED
        ))
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, IntentFilter(
            INSTALL_ABORTED
        ))
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, IntentFilter(
            INSTALL_BLOCKED
        ))
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, IntentFilter(
            INSTALL_STORAGE
        ))
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, IntentFilter(
            INSTALL_CONFLICT
        ))
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, IntentFilter(
            INSTALL_FAILED
        ))
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, IntentFilter(
            INSTALL_INVALID
        ))

    }

    companion object {
        const val INSTALL_COMPLETED = "Installation completed"
        const val INSTALL_ABORTED = "user aborted installation"
        const val INSTALL_BLOCKED = "user blocked installation"
        const val INSTALL_STORAGE = "there was an error with storage.\n Hold up how is that even possible?"
        const val INSTALL_CONFLICT = "app conflicts with already installed app"
        const val INSTALL_FAILED = "it just failed idk"
        const val INSTALL_INVALID = "apk files are invalid"
    }
}
