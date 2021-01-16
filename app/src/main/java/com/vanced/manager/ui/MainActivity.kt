package com.vanced.manager.ui

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import com.crowdin.platform.Crowdin
import com.crowdin.platform.LoadingStateListener
import com.google.firebase.messaging.FirebaseMessaging
import com.vanced.manager.BuildConfig.ENABLE_CROWDIN_AUTH
import com.vanced.manager.BuildConfig.VERSION_CODE
import com.vanced.manager.R
import com.vanced.manager.databinding.ActivityMainBinding
import com.vanced.manager.ui.dialogs.DialogContainer
import com.vanced.manager.ui.dialogs.ManagerUpdateDialog
import com.vanced.manager.ui.dialogs.URLChangeDialog
import com.vanced.manager.ui.fragments.HomeFragmentDirections
import com.vanced.manager.ui.fragments.SettingsFragmentDirections
import com.vanced.manager.utils.*


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private val navHost by lazy { findNavController(R.id.nav_host) }

    private val loadingObserver = object : LoadingStateListener {
        val tag = "VMLocalisation"
        override fun onDataChanged() {
            Log.d(tag, "Loaded data")
        }

        override fun onFailure(throwable: Throwable) {
            Log.d(tag, "Failed to load data: $throwable")
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setFinalTheme()
        super.onCreate(savedInstanceState)
        if (ENABLE_CROWDIN_AUTH)
            authCrowdin()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            setSupportActionBar(toolbar)
            toolbar.setupWithNavController(
                this@MainActivity.navHost,
                AppBarConfiguration(this@MainActivity.navHost.graph)
            )
        }
        navHost.addOnDestinationChangedListener { _, currFrag: NavDestination, _ ->
            setDisplayHomeAsUpEnabled(currFrag.id != R.id.home_fragment)
        }

        initDialogs(intent.getBooleanExtra("firstLaunch", false))
        manager.observe(this) {
            if (manager.value?.int("versionCode") ?: 0 > VERSION_CODE) {
                ManagerUpdateDialog.newInstance(false).show(this)
            }
        }
    }

    override fun onBackPressed() {
        if (!navHost.popBackStack())
            finish()
    }

    private fun setDisplayHomeAsUpEnabled(isNeeded: Boolean) {
        binding.toolbar.navigationIcon = if (isNeeded) ContextCompat.getDrawable(this, R.drawable.ic_keyboard_backspace_black_24dp) else null
    }

    override fun onPause() {
        super.onPause()
        Crowdin.unregisterDataLoadingObserver(loadingObserver)
    }

    override fun onResume() {
        setFinalTheme()
        super.onResume()
        Crowdin.registerDataLoadingObserver(loadingObserver)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                return true
            }
            R.id.toolbar_about -> {
                navHost.navigate(HomeFragmentDirections.toAboutFragment())
                return true
            }
            R.id.toolbar_settings -> {
                navHost.navigate(HomeFragmentDirections.toSettingsFragment())
                return true
            }
            R.id.toolbar_update_manager -> {
                ManagerUpdateDialog.newInstance(false).show(supportFragmentManager, "manager_update")
            }
            R.id.dev_settings -> {
                navHost.navigate(SettingsFragmentDirections.toDevSettingsFragment())
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }

        return false
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(Crowdin.wrapContext(LanguageContextWrapper.wrap(newBase)))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        onActivityResult(requestCode)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        recreate() //restarting activity to recreate viewmodels, otherwise some text won't update
    }

    override fun recreate() {
        //needed for setting language smh
        startActivity(Intent(this, this::class.java))
        finish()
    }

    private fun initDialogs(firstLaunch: Boolean) {
        val prefs = getDefaultSharedPreferences(this)
        val variant = prefs.getString("vanced_variant", "nonroot")
        prefs.getBoolean("show_root_dialog", true)

        if (intent?.data != null && intent.dataString?.startsWith("https") == true) {
            val urldialog = URLChangeDialog()
            val arg = Bundle()
            arg.putString("url", intent.dataString)
            urldialog.arguments = arg
            urldialog.show(this)
        }

        when {
            firstLaunch -> {
                DialogContainer.showSecurityDialog(this)
                with(FirebaseMessaging.getInstance()) {
                    subscribeToTopic("Vanced-Update")
                    subscribeToTopic("Music-Update")
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
    }

}
