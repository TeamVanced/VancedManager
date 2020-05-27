package com.vanced.manager.ui

import android.content.*
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.vanced.manager.R
import com.vanced.manager.core.Main

class MainActivity : Main() {

    private var isParent = true

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, IntentFilter(
            INSTALL_COMPLETED
        ))

    }

    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                INSTALL_COMPLETED -> launchVanced()
                else -> intent.action?.let { alertBuilder(it) }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver)
    }

    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver)
    }

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, IntentFilter(
            INSTALL_COMPLETED
        ))
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

    private fun setDisplayHomeAsUpEnabled(isNeeded: Boolean) {
        val toolbar: Toolbar = findViewById(R.id.home_toolbar)
        when {
            isNeeded -> toolbar.setNavigationIcon(R.drawable.ic_keyboard_backspace_black_24dp)
            else -> toolbar.navigationIcon = null
        }
    }

    private fun alertBuilder(msg: String) {
        AlertDialog.Builder(this)
            .setTitle("Error")
            .setMessage("Operation failed because $msg")
            .setPositiveButton(getString(R.string.close)) { dialog, _ ->
                run {
                    dialog.dismiss()
                    recreate()
                }
            }
            .create()
            .show()
    }

    private fun launchVanced() {
        val intent = Intent()
        intent.component = ComponentName("com.vanced.android.youtube", "com.vanced.android.youtube.HomeActivity")
        AlertDialog.Builder(this)
            .setTitle("Success!")
            .setMessage("Vanced has been successfully installed, do you want to launch it now?")
            .setPositiveButton("Launch") {
                    _, _ -> startActivity(intent)
            }
            .setNegativeButton("Cancel") {
                    dialog, _ ->
                run {
                    dialog.dismiss()
                    recreate()
                }
            }
            .create()
            .show()
    }

    companion object {
        const val BLANK_INTENT = "BLANK"
        const val INSTALL_COMPLETED = "Installation completed"
        const val INSTALL_ABORTED = "user aborted installation"
        const val INSTALL_BLOCKED = "user blocked installation"
        const val INSTALL_STORAGE = "there was an error with storage.\n Hold up how is that even possible?"
        const val INSTALL_CONFLICT = "app conflicts with already installed app"
        const val INSTALL_FAILED = "it just failed idk"
        const val INSTALL_INVALID = "apk files are invalid"

    }
}
