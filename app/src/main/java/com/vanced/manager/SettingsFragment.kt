package com.vanced.manager

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val settingsfragment = inflater.inflate(R.layout.fragment_home, container, false)
        val toolbar = view?.findViewById<Toolbar>(R.id.home_toolbar)

        toolbar?.title = "Settings"
        toolbar?.inflateMenu(R.menu.toolbar_menu)
        toolbar?.setOnMenuItemClickListener {
            if (it.itemId == R.id.about) {
                val intent = Intent()
                intent.component = ComponentName(
                    "com.vanced.manager",
                    "com.vanced.manager.ui.AboutActivity"
                )
                startActivity(intent)
            }
            true
        }

        return settingsfragment
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

       val themeSwitch: ListPreference? = findPreference("theme_modes")
        themeSwitch?.setOnPreferenceChangeListener { _, _ ->
            val currentVal: String = themeSwitch.value
            when (currentVal.toInt()){
                 0 -> {
                     AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                     activity?.recreate()
                 }
                 1 -> {
                     AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                     activity?.recreate()
                 }
                else ->{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    activity?.recreate()
                }
            }
            true
    }


    }
}